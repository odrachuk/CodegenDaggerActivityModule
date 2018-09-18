package com.softsandr.codegen.utils

import com.softsandr.codegen.exception.ClassPackageNotFoundException
import javax.annotation.processing.Messager
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

internal object Utils {

    @Throws(ClassPackageNotFoundException::class)
    fun getPackageName(elementUtils: Elements, typeElement: TypeElement): String {
        val pkg = elementUtils.getPackageOf(typeElement)

        if (pkg.isUnnamed) {
            throw ClassPackageNotFoundException(typeElement.simpleName.toString())
        }

        return pkg.qualifiedName.toString()
    }


    fun isValidClass(annotatedClass: TypeElement, clazz: Class<*>, messager: Messager? = null): Boolean {
        // if class is not a public class
        if (!annotatedClass.modifiers.contains(Modifier.PUBLIC)) {
            val message = String.format("Classes annotated with %s must be public.",
                    "@" + clazz.simpleName)
            println("Classes annotated with %s must be public.")
            messager?.printMessage(Diagnostic.Kind.ERROR, message, annotatedClass)
            return false
        }

        // if the class is a abstract class
        if (annotatedClass.modifiers.contains(Modifier.ABSTRACT)) {
            val message = String.format("Classes annotated with %s must not be abstract.",
                    "@" + clazz.simpleName)
            println("Classes annotated with %s must not be abstract.")
            messager?.printMessage(Diagnostic.Kind.ERROR, message, annotatedClass)
            return false
        }

        return true
    }
}
