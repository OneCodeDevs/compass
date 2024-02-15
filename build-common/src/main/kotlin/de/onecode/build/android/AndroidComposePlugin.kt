package de.onecode.build.android

import com.android.build.gradle.TestedExtension
import de.onecode.build.common.composeCompiler
import de.onecode.build.common.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("UnstableApiUsage")
class AndroidComposePlugin : Plugin<Project> {
	override fun apply(target: Project): Unit = with(target) {
		val versionCatalog = versionCatalog()
		configure<TestedExtension> {
			buildFeatures.compose = true
			composeOptions {
				kotlinCompilerExtensionVersion = versionCatalog.composeCompiler
			}
		}
	}
}