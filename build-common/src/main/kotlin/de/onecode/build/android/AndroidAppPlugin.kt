package de.onecode.build.android

import com.android.build.api.dsl.ApplicationExtension
import de.onecode.build.common.androidMinSdk
import de.onecode.build.common.androidTargetSdk
import de.onecode.build.common.configureDetekt
import de.onecode.build.common.java
import de.onecode.build.common.versionCatalog
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

class AndroidAppPlugin : Plugin<Project> {
	override fun apply(target: Project): Unit = with(target) {
		plugins.apply("com.android.application")
		plugins.apply("org.jetbrains.kotlin.android")
		plugins.apply("io.gitlab.arturbosch.detekt")

		val versionCatalog = versionCatalog()

		configure<ApplicationExtension> {
			compileSdk = versionCatalog.androidTargetSdk

			defaultConfig {
				minSdk = versionCatalog.androidMinSdk
				targetSdk = versionCatalog.androidTargetSdk
				testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

				vectorDrawables {
					useSupportLibrary = true
				}
			}
			lint {
				targetSdk = versionCatalog.androidTargetSdk
			}

			buildTypes {
				release {
					isMinifyEnabled = false
					proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
				}
			}
			compileOptions {
				sourceCompatibility = JavaVersion.valueOf("VERSION_${versionCatalog.java}")
				targetCompatibility = JavaVersion.valueOf("VERSION_${versionCatalog.java}")
			}

			packaging {
				resources {
					excludes += "/META-INF/{AL2.0,LGPL2.1}"
				}
			}

			kotlinExtension.apply {
				jvmToolchain(versionCatalog.java.toInt())
			}
		}

		configureDetekt()
	}
}