import de.onecode.build.publish.MavenPublishExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.publish.jvm)
}

java {
	archivesName.set("compass-api")
	sourceCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.java.get()}")
	targetCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.java.get()}")
}

configure<MavenPublishExtension> {
	name = "Compass API"
	description = "The API of Compass, containing all available annotations usable to configure your apps navigation"
	artifactId = "compass-api"
	version = libs.versions.compass.get()
}
