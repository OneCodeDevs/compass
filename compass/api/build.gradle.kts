import de.onecode.build.jvm.KotlinConfigExtension
import de.onecode.build.publish.MavenPublishExtension

plugins {
	alias(libs.plugins.kotlin.config.jvm)
	alias(libs.plugins.publish.jvm)
}

configure<KotlinConfigExtension> {
	artifactName = "compass-api"
}

configure<MavenPublishExtension> {
	name = "Compass API"
	description = "The API of Compass, containing all available annotations usable to configure your apps navigation"
	artifactId = "compass-api"
	version = libs.versions.compass.get()
}
