package de.onecode.navigator.api

import kotlin.reflect.KClass

@Repeatable
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Navigation(val to: KClass<*>)