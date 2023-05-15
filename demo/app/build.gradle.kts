@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.ksp)
}

android {
	namespace = "de.onecode.navigator.demo"
	compileSdk = 33

	defaultConfig {
		applicationId = "de.onecode.navigator.demo"
		minSdk = 21
		targetSdk = 33
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.4.7"
	}
	packagingOptions {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
	sourceSets.configureEach {
		kotlin.srcDir("$buildDir/generated/ksp/$name/kotlin/")
	}
}

dependencies {

	implementation(libs.android.core.ktx)
	implementation(libs.android.lifecycle.runtime.ktx)
	implementation(libs.android.activity.compose)

	implementation(platform(libs.compose.bom))
	implementation(libs.compose.ui)
	implementation(libs.compose.material3)
	implementation(libs.compose.ui.preview)
	implementation(libs.compose.navigation)

	implementation(project(":demo:wizard"))
	implementation(project(":api"))
	implementation(project(":runtime"))
	ksp(project(":ksp"))

	debugImplementation(libs.compose.ui.tooling)
	debugImplementation(libs.compose.ui.test.manifest)
}