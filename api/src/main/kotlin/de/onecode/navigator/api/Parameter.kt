package de.onecode.navigator.api

import kotlin.reflect.KClass

@Repeatable
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Parameter(val name: String, val type: KClass<*>)