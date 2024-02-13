plugins {
	id("java-gradle-plugin")
	alias(libs.plugins.kotlin.jvm)
}

group = "de.onecode"
version = libs.versions.build.common.get()

gradlePlugin {
	plugins {
		create("maven-publish-jvm") {
			id = "de.onecode.publish-jvm"
			implementationClass = "de.onecode.build.publish.MavenPublishJvmPlugin"
		}
		create("maven-publish-android") {
			id = "de.onecode.publish-android"
			implementationClass = "de.onecode.build.publish.MavenPublishAndroidPlugin"
		}
	}
}

dependencies {
	implementation(libs.android.library.plugin)
}