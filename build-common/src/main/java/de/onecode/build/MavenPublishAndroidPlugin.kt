package de.onecode.build

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar

class MavenPublishAndroidPlugin : Plugin<Project> {
	override fun apply(target: Project): Unit = with(target) {
		val extension = extensions.create("maven-publish-android", MavenPublishExtension::class.java)
		afterEvaluate {
			val localProperties = loadProperties()

			val name = extension.name
			val description = extension.description
			val artifactId = extension.artifactId
			val version = extension.version

			applyPlugins()
			makePublishDependOnBuild()

			val android = extensions.getByName("android") as com.android.build.gradle.LibraryExtension

			val sourcesJar = tasks.register("sourcesJar", Jar::class.java) { jar ->
				jar.from(android.sourceSets.getByName("main").java.getSourceFiles())
				jar.archiveClassifier.set("sources")
			}

			val javadoc = tasks.register("javadoc", Javadoc::class.java) { javaDoc ->
				javaDoc.source = android.sourceSets.getByName("main").java.getSourceFiles()
				javaDoc.classpath += project.files(android.bootClasspath)
			}

			val javadocJar = tasks.register("javadocJar", Jar::class.java) { jar ->
				jar.dependsOn(javadoc)
				jar.from(javadoc)
				jar.archiveClassifier.set("javadoc")
			}

			configurePublish(name, description, artifactId, version, localProperties) {
				artifact("${layout.buildDirectory.asFile.get().absolutePath}/outputs/aar/${artifactId}-release.aar")
				artifact(sourcesJar)
				artifact(javadocJar)
			}

			configureSigning()
			tasks.named("signMavenPublication") {
				it.dependsOn("bundleReleaseAar")
			}
		}
	}
}
