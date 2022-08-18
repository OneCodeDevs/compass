package io.redandroid.navigator.api

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Repeatable
annotation class Parameter(val name: String, val type: KClass<*>)