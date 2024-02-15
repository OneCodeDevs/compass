package de.onecode.build.publish

import de.onecode.build.common.LocalProperties
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.maven.tasks.PublishToMavenLocal
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension
import org.gradle.plugins.signing.SigningPlugin
import java.net.URI

fun Project.applyPlugins() {
	apply<MavenPublishPlugin>()
	apply<SigningPlugin>()
}

fun Project.makePublishDependOnBuild() {
	tasks.withType<PublishToMavenLocal>().configureEach {
		dependsOn("build")
	}
	tasks.withType<PublishToMavenRepository>().configureEach {
		dependsOn("build")
	}
}

fun Project.configureSigning() {
	configure<SigningExtension> {
		val publishing = extensions.getByType<PublishingExtension>()
		sign(publishing.publications.getByName("maven"))
	}
}

fun Project.configurePublish(
	artifactName: String,
	artifactDescription: String,
	artifactId: String,
	version: String,
	localProperties: LocalProperties,
	configureArtifacts: MavenPublication.() -> Unit,
) {

	configure<PublishingExtension> {
		publications {
			create("maven", MavenPublication::class.java) {
				pom {
					name = artifactName
					description = artifactDescription
					url = "https://github.com/OneCodeDevs/compass"
					licenses {
						license {
							name = "The Apache License, Version 2.0"
							url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
						}
					}
					developers {
						developer {
							id = "a-frank"
							name = "Alexander Frank"
							email = "alexander.frank@onecode.de"
						}
						developer {
							id = "pynnie"
							name = "Sebastian Hecken"
							email = "sebastian.hecken@onecode.de"
						}
						developer {
							id = "florian-meyer-onecode "
							name = "Florian Meyer"
							email = "florian.meyer@onecode.de"
						}
					}
					scm {
						connection = "https://github.com/OneCodeDevs/compass.git"
						developerConnection = "https://github.com/OneCodeDevs/compass.git"
						url = "https://github.com/OneCodeDevs/compass"
					}
				}

				groupId = "de.onecode"
				this.artifactId = artifactId
				this.version = version

				configureArtifacts()
			}
		}
		repositories {
			maven {
				url = if (version.uppercase().endsWith("-SNAPSHOT")) {
					URI.create("https://s01.oss.sonatype.org/content/repositories/snapshots/")
				} else {
					URI.create("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
				}
				credentials {
					username = localProperties.username
					password = localProperties.password
				}
			}
		}
	}
}