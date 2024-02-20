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
		create("android-app") {
			id = "de.onecode.android-app"
			implementationClass = "de.onecode.build.android.AndroidAppPlugin"
		}
		create("android-library") {
			id = "de.onecode.android-library"
			implementationClass = "de.onecode.build.android.AndroidLibraryPlugin"
		}
		create("android-compose") {
			id = "de.onecode.android-compose"
			implementationClass = "de.onecode.build.android.AndroidComposePlugin"
		}
	}
}
dependencies {
	implementation(gradleApi())
	implementation(libs.android.library.plugin)
	implementation(libs.android.application.plugin)
	implementation(libs.kotlin.jvm.plugin)
	implementation(libs.detekt)
}