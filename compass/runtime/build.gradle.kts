import de.onecode.build.publish.MavenPublishExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.publish.android)
}

android {
	archivesName.set("compass-runtime")
	namespace = "de.onecode.compass.runtime"
}

dependencies {
	implementation(libs.compose.navigation)
}

configure<MavenPublishExtension> {
	name = "Compass Runtime"
	description = "Contains needed runtime functions to make Compass work."
	artifactId = "compass-runtime"
	version = libs.versions.compass.get()
}
