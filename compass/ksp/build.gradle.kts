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
	implementation(project(":compass:api"))

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
	name = "Compass KSP"
	description = "The KSP code generator for Compass. It used the annotations for Compass API and generated the necessary code configured by the Compass annotations."
	artifactId = "compass-ksp"
	version = libs.versions.compass.get()
}
