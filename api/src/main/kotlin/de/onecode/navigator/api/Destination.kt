package de.onecode.navigator.api

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Destination(val name: String = "")