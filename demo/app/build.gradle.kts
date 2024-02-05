@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.ksp)
}

android {
	namespace = "de.onecode.navigator.demo"
	compileSdk = libs.versions.android.sdk.target.get().toInt()

	defaultConfig {
		applicationId = "de.onecode.navigator.demo"
		minSdk = libs.versions.android.sdk.min.get().toInt()
		targetSdk = libs.versions.android.sdk.min.get().toInt()
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
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
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
	sourceSets.configureEach {
		kotlin.srcDir("${layout.buildDirectory}/generated/ksp/$name/kotlin/")
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
	implementation(project(":compose-navigator-api"))
	implementation(project(":compose-navigator-runtime"))
	ksp(project(":compose-navigator-ksp"))

	debugImplementation(libs.compose.ui.tooling)
	debugImplementation(libs.compose.ui.test.manifest)
}