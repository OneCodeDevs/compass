@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.ksp)
}

android {
	namespace = "de.onecode.navigator.demo.destinations"
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
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
	}
	sourceSets.configureEach {
		kotlin.srcDir("${layout.buildDirectory}/generated/ksp/$name/kotlin/")
	}
}

dependencies {
	implementation(libs.android.core.ktx)

	implementation(platform(libs.compose.bom))
	implementation(libs.compose.material3)
	implementation(libs.compose.navigation)

	implementation(project(":compose-navigator-api"))
	implementation(project(":compose-navigator-runtime"))
	ksp(project(":compose-navigator-ksp"))
}