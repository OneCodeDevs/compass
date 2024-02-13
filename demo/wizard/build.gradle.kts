plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.ksp)
}

android {
	namespace = "de.onecode.compass.demo.wizard"
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
}

dependencies {
	implementation(libs.android.core.ktx)

	implementation(platform(libs.compose.bom))
	implementation(libs.compose.material3)
	implementation(libs.compose.navigation)

	implementation(project(":compass:api"))
	implementation(project(":compass:runtime"))
	ksp(project(":compass:ksp"))
}