package io.redandroid.navigator.api

import kotlin.reflect.KClass

@Repeatable
@Target(AnnotationTarget.CLASS)
annotation class Navigation(val to: KClass<*>)