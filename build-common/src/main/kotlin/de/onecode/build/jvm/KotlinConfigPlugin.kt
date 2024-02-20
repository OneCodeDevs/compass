package de.onecode.build.jvm

import de.onecode.build.common.configureDetekt
import de.onecode.build.common.java
import de.onecode.build.common.versionCatalog
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

class KotlinConfigPlugin : Plugin<Project> {
	override fun apply(target: Project): Unit = with(target) {
		plugins.apply("org.jetbrains.kotlin.jvm")
		plugins.apply("io.gitlab.arturbosch.detekt")

		val extension = extensions.create("kotlin-config", KotlinConfigExtension::class.java)

		afterEvaluate {
			archivesName.set(extension.artifactName)
			val java = versionCatalog().java

			configure<JavaPluginExtension> {
				sourceCompatibility = JavaVersion.valueOf("VERSION_$java")
				targetCompatibility = JavaVersion.valueOf("VERSION_$java")
			}

			configureDetekt()
		}

	}
}