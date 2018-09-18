package com.softsandr.codegen.processor

import com.google.auto.service.AutoService
import com.softsandr.codegen.annotation.ActivityModuleClass
import com.softsandr.codegen.annotation.GenerateActivityModule
import com.softsandr.codegen.processor.ActivityModuleGenerator.ACT_MODULE_ANNOTATION
import com.softsandr.codegen.processor.FragmentModuleGenerator.FRG_MODULE_ANNOTATION
import com.softsandr.codegen.processor.FragmentModuleGenerator.filterFragmentElements
import com.softsandr.codegen.utils.Utils
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypesException

/**
 * This annotation processor is used for generating Dagger2's modules for Activities and Fragments
 * that a DispatchingAndroidInjector will use for generating target sub-components for them.
 *
 * The processor expects that an Activity is annotated by [GenerateActivityModule]
 * and a Fragment is annotated by [GenerateFragmentModule] respectively.
 *
 * The processor uses [GenerateActivityModule.fragments] parameter to build dependencies between
 * Activities and Fragments.
 */
@AutoService(Processor::class)
class DaggerModulesProcessor : AbstractProcessor() {

    // set of modules that are generated already
    // for preventing kapt's exceptions about attempts of re-creating files
    private val generatedTypes: MutableSet<String> = mutableSetOf()
    // used as a logger for kapt
    private lateinit var messager: Messager

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        messager = processingEnv.messager
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(ACT_MODULE_ANNOTATION.canonicalName, FRG_MODULE_ANNOTATION.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        // getting list of all Fragments that use GenerateFragmentModule annotation
        val fragmentElements = roundEnv
                .getElementsAnnotatedWith(FRG_MODULE_ANNOTATION) // filtering Fragments that have GenerateFragmentModule annotation
                .asSequence()
                .filterIsInstance<TypeElement>() // making sure that GenerateFragmentModule annotation was used for the type, not for a method or a field
                .filter { Utils.isValidClass(it, FRG_MODULE_ANNOTATION, messager) }
                .toList() // making sure that Fragment's class is public and not abstract

        // getting list of all Activities that use GenerateActivityModule annotation
        val activityElements = roundEnv
                .getElementsAnnotatedWith(ACT_MODULE_ANNOTATION) // filtering Activities that have GenerateActivityModule annotation
                .asSequence()
                .filterIsInstance<TypeElement>() // making sure that GenerateActivityModule annotation was used for the type
                .filter { Utils.isValidClass(it, ACT_MODULE_ANNOTATION, messager) } // making sure that Activity's class is public and not abstract
                .map { it ->
                    val annotation = it.getAnnotation(ACT_MODULE_ANNOTATION) // instance of the GenerateAcitvityModule
                    // returning of a wrapper-class
                    return@map ActivityModuleClass(it, parseActivityModules(annotation),
                            filterFragmentElements(fragmentElements, parseActivityFragments(annotation)))
                }
                .toList()

        // generating *DaggerModule .java-files with JavaPoet.
        // calling this method generates modules as for all Activities as for all Fragments
        ActivityModuleGenerator.generateModulesClasses(processingEnv, activityElements, generatedTypes)

        return true
    }

    /**
     * The method is used for parsing [GenerateActivityModule.modules] as a list of any possible
     * external Dagger2's modules that should be described in @ContributesAndroidInjector annotation
     * for an Activity
     */
    private fun parseActivityModules(annotation: GenerateActivityModule?): List<String> {
        val modules: List<String>? = try {
            annotation?.modules?.map { it -> it.qualifiedName + ".class" }
        } catch (ex: MirroredTypesException) {
            // if the modules parameter has more than one element the javax generates such exception
            // the exception has reference on types - typeMirrors
            ex.typeMirrors?.map { it -> it.toString() + ".class" }
        }
        return modules ?: emptyList()
    }

    /**
     * The method is used for parsing [GenerateActivityModule.fragments] as a list of all Fragments
     * that can be instantiated from an Activity and should have own Dagger2's modules described
     * in @ContributesAndroidInjector of the Activity
     */
    private fun parseActivityFragments(annotation: GenerateActivityModule?): List<String> {
        val fragments: List<String>? = try {
            annotation?.fragments?.map { it -> it.qualifiedName.toString() }
        } catch (ex: MirroredTypesException) {
            ex.typeMirrors?.map { it -> it.toString() }
        }
        return fragments ?: emptyList()
    }

    companion object Constants {
        internal const val MODULE_SUFFIX = "DaggerModule" // just a suffix for all modules generated from here

        // I don't want the GenerateActivityModule and GenerateFragmentModule annotations to be more hard in use -
        // let's use these simple constants. But in future it will not be a problem to implement it as arguments of the annotations
        internal const val SCOPE_PACKAGE = "com.softsandr.codegen.di"
        internal const val ACTIVITY_SCOPE = "ActivityScope"
        internal const val FRAGMENT_SCOPE = "FragmentScope"
    }
}