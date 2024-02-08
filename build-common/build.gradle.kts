plugins {
	id("java-gradle-plugin")
	alias(libs.plugins.kotlin.jvm)
}

group = "de.onecode"
version = "1.0.0"

gradlePlugin {
	plugins {
		create("maven-publish-jvm") {
			id = "de.onecode.publish-jvm"
			implementationClass = "de.onecode.build.MavenPublishJvmPlugin"
		}
		create("maven-publish-android") {
			id = "de.onecode.publish-android"
			implementationClass = "de.onecode.build.MavenPublishAndroidPlugin"
		}
	}
}

dependencies {
	implementation(libs.android.library.plugin)
}