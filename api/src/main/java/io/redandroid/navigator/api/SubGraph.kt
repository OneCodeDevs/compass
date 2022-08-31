package io.redandroid.navigator.api

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class SubGraph(
	val name: String = "",
	vararg val destinations: KClass<*>
)
