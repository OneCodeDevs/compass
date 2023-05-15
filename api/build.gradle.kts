@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.kotlin.jvm)
}

java {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}