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
	name = "Compose Navigator API"
	description = "The API part of the compose Navigator"
	artifactId = "compose-navigator-api"
	version = libs.versions.compose.navigator.get()
}
