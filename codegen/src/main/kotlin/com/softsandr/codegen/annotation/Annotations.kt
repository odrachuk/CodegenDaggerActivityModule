package com.softsandr.codegen.annotation

import kotlin.reflect.KClass


/**
 * Use this annotation for an Activity. All parameters are optional.
 * @param fragments is used for list of comma separated classes of Fragments.
 * @param modules is used for list of comma separated classes of existent Dagger's modules that are not
 * modules of Fragments
 */
@Target(AnnotationTarget.CLASS)
annotation class GenerateActivityModule(val fragments: Array<KClass<*>> = emptyArray(),
                                        val modules: Array<KClass<*>> = emptyArray())

/**
 * Use this annotation for a Fragment. All parameters are optional.
 * @param modules is used for list of comma separated classes of existent Dagger's modules that are not
 * modules of Fragments or Activities
 */
@Target(AnnotationTarget.CLASS)
annotation class GenerateFragmentModule(val modules: Array<KClass<*>> = emptyArray())