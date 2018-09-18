package com.softsandr.codegen.annotation

import javax.lang.model.element.TypeElement

interface ModuleClass {
    val element: TypeElement
    val dependencies: List<String>
}

/**
 * A wrapper for the information are necessary during generating java-files with modules
 * for Activities and their Fragments
 */
internal class ActivityModuleClass(override val element: TypeElement,
                                   override val dependencies: List<String> = emptyList(),
                                   val fragments: List<FragmentModuleClass> = emptyList()) : ModuleClass

/**
 * A wrapper for the information are necessary during generating a java-file with module for a Fragment
 */
internal class FragmentModuleClass(override val element: TypeElement,
                                   override val dependencies: List<String> = emptyList()) : ModuleClass
