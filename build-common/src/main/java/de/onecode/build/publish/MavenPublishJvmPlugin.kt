package de.onecode.build.publish

import de.onecode.build.common.loadProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension

class MavenPublishJvmPlugin : Plugin<Project> {
	override fun apply(target: Project): Unit = with(target) {
		val extension = extensions.create("maven-publish-jvm", MavenPublishExtension::class.java)
		afterEvaluate {
			val localProperties = loadProperties()

			val name = extension.name
			val description = extension.description
			val artifactId = extension.artifactId
			val version = extension.version

			applyPlugins()
			makePublishDependOnBuild()

			extensions.configure(JavaPluginExtension::class.java) {
				it.withJavadocJar()
				it.withSourcesJar()
			}

			configurePublish(name, description, artifactId, version, localProperties) {
				from(components.getByName("kotlin"))
				artifact(tasks.getByName("sourcesJar"))
				artifact(tasks.getByName("javadocJar"))
			}
			configureSigning()
		}
	}
}
