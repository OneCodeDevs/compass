import de.onecode.build.publish.MavenPublishExtension

plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.publish.jvm)
}

java {
	sourceCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.java.get()}")
	targetCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.java.get()}")
}

dependencies {
	implementation(project(":compose-navigator-api"))

	implementation(libs.ksp.api)
	implementation(libs.kotlinpoet)
	implementation(libs.kotlinpoet.ksp)

	testImplementation(platform(libs.junit.bom))
	testImplementation(libs.junit.jupiter)
	testImplementation(libs.google.truth)
	testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.named<Test>("test") {
	useJUnitPlatform()
}

configure<MavenPublishExtension> {
	name = "Compose Navigator KSP"
	description = "The KSP part of the compose Navigator"
	artifactId = "compose-navigator-ksp"
	version = libs.versions.compose.navigator.get()
}
