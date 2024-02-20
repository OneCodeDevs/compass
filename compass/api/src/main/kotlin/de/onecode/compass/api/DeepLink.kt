package de.onecode.compass.api

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
@Repeatable
annotation class DeepLink(val schema: String, val host: String, val path: String = "")
