import de.onecode.build.publish.MavenPublishExtension

plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.publish.jvm)
}

java {
	sourceCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.java.get()}")
	targetCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.java.get()}")
}

configure<MavenPublishExtension> {
	name = "Compass API"
	description = "The API of Compass, containing all available annotations usable to configure your apps navigation"
	artifactId = "compass-api"
	version = libs.versions.compass.get()
}
