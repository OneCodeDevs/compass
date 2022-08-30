package io.redandroid.navigator.api

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Destination(val name: String = "")