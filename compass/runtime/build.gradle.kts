import de.onecode.build.publish.MavenPublishExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.publish.android)
}

android {
	namespace = "de.onecode.compass.runtime"
	compileSdk = libs.versions.android.sdk.target.get().toInt()

	defaultConfig {
		minSdk = libs.versions.android.sdk.min.get().toInt()
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		archivesName = "compass-runtime"
	}

	lint {
		targetSdk = libs.versions.android.sdk.target.get().toInt()
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.java.get()}")
		targetCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.java.get()}")
	}
	kotlinOptions {
		jvmTarget = libs.versions.java.get()
	}
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
