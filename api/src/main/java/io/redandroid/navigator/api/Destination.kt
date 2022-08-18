package io.redandroid.navigator.api

@Target(AnnotationTarget.CLASS)
annotation class Destination(val isHome: Boolean = false)