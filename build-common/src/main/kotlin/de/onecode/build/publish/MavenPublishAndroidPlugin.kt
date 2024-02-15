package de.onecode.build.publish

import de.onecode.build.common.loadProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class MavenPublishAndroidPlugin : Plugin<Project> {
	override fun apply(target: Project): Unit = with(target) {
		val extension = extensions.create<MavenPublishExtension>("maven-publish-android")
		afterEvaluate {
			val localProperties = loadProperties()

			val name = extension.name
			val description = extension.description
			val artifactId = extension.artifactId
			val version = extension.version

			applyPlugins()
			makePublishDependOnBuild()

			val android = extensions.getByName("android") as com.android.build.gradle.LibraryExtension

			val sourcesJar = tasks.register<Jar>("sourcesJar") {
				from(android.sourceSets.getByName("main").java.getSourceFiles())
				archiveClassifier.set("sources")
			}

			val javadoc = tasks.register<Javadoc>("javadoc") {
				source = android.sourceSets.getByName("main").java.getSourceFiles()
				classpath += project.files(android.bootClasspath)
			}

			val javadocJar = tasks.register<Jar>("javadocJar") {
				dependsOn(javadoc)
				from(javadoc)
				archiveClassifier.set("javadoc")
			}

			configurePublish(name, description, artifactId, version, localProperties) {
				artifact("${layout.buildDirectory.asFile.get().absolutePath}/outputs/aar/${artifactId}-release.aar")
				artifact(sourcesJar)
				artifact(javadocJar)
			}

			configureSigning()
			tasks.named("signMavenPublication") {
				dependsOn("bundleReleaseAar")
			}
		}
	}
}
