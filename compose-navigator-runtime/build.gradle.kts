import de.onecode.build.MavenPublishExtension

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.publish.android)
}

android {
	namespace = "de.oencode.navigator.runtime"
	compileSdk = libs.versions.android.sdk.target.get().toInt()

	defaultConfig {
		minSdk = libs.versions.android.sdk.min.get().toInt()
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

extensions.configure<MavenPublishExtension> {
	name = "Compose Navigator Runtime"
	description = "The Runtime part of the compose Navigator"
	artifactId = "compose-navigator-runtime"
	version = libs.versions.compose.navigator.get()
}
