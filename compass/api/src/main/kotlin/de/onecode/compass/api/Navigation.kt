package de.onecode.compass.api

import kotlin.reflect.KClass

@Repeatable
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Navigation(val to: KClass<*>)