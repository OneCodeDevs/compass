@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.ksp)
}

android {
	namespace = "io.redandroid.navigator.demo.wizard"
	compileSdk = 33

	defaultConfig {
		minSdk = 21
		targetSdk = 33

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
	sourceSets.configureEach {
		kotlin.srcDir("$buildDir/generated/ksp/$name/kotlin/")
	}
}

dependencies {
	implementation(libs.android.core.ktx)

	implementation(platform(libs.compose.bom))
	implementation(libs.compose.material3)
	implementation(libs.compose.navigation)

	implementation(project(":api"))
	implementation(project(":runtime"))
	ksp(project(":ksp"))
}