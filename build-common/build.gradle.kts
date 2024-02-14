plugins {
	`java-gradle-plugin`
	`kotlin-dsl`
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
		create("koltin-config-jvm") {
			id = "de.onecode.koltin-config-jvm"
			implementationClass = "de.onecode.build.jvm.KotlinConfigPlugin"
		}
	}
}
dependencies {
	implementation(gradleApi())
	implementation(libs.android.library.plugin)
	implementation(libs.kotlin.jvm.plugin)
}