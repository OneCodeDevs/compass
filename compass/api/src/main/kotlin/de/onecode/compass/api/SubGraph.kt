package de.onecode.compass.api

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class SubGraph(
	val name: String = "",
	vararg val destinations: KClass<*>
)
