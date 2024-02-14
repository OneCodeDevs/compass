package de.onecode.build.publish

import de.onecode.build.common.LocalProperties
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.PublishToMavenLocal
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.plugins.signing.SigningExtension
import java.net.URI

fun Project.applyPlugins() {
	plugins.apply("maven-publish")
	plugins.apply("signing")
}

fun Project.makePublishDependOnBuild() {
	tasks.withType(PublishToMavenLocal::class.java).configureEach {
		it.dependsOn("build")
	}
	tasks.withType(PublishToMavenRepository::class.java).configureEach {
		it.dependsOn("build")
	}
}

fun Project.configureSigning() {
	extensions.configure(SigningExtension::class.java) {
		val publishing = extensions.getByName("publishing") as PublishingExtension
		it.sign(publishing.publications.getByName("maven"))
	}
}

fun Project.configurePublish(
	name: String,
	description: String,
	artifactId: String,
	version: String,
	localProperties: LocalProperties,
	configureArtifacts: MavenPublication.() -> Unit,
) {
	extensions.configure(PublishingExtension::class.java) {
		it.publications { container ->
			container.create("maven", MavenPublication::class.java) { maven ->
				maven.pom { pom ->
					pom.name.set(name)
					pom.description.set(description)
					pom.url.set("https://github.com/OneCodeDevs/compass")
					pom.licenses { licenseSpec ->
						licenseSpec.license { license ->
							license.name.set("The Apache License, Version 2.0")
							license.url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
						}
					}
					pom.developers { developerSpec ->
						developerSpec.developer { developer ->
							developer.id.set("a-frank")
							developer.name.set("Alexander Frank")
							developer.email.set("alexander.frank@onecode.de")
						}
						developerSpec.developer { developer ->
							developer.id.set("pynnie")
							developer.name.set("Sebastian Hecken")
							developer.email.set("sebastian.hecken@onecode.de")
						}
						developerSpec.developer { developer ->
							developer.id.set("florian-meyer-onecode ")
							developer.name.set("Florian Meyer")
							developer.email.set("florian.meyer@onecode.de")
						}
					}
					pom.scm { scm ->
						scm.connection.set("https://github.com/OneCodeDevs/compass.git")
						scm.developerConnection.set("https://github.com/OneCodeDevs/compass.git")
						scm.url.set("https://github.com/OneCodeDevs/compass")
					}
				}

				maven.groupId = "de.onecode"
				maven.artifactId = artifactId
				maven.version = version

				maven.configureArtifacts()
			}
		}
		it.repositories { handler ->
			handler.maven { repository ->
				repository.url = if (version.uppercase().endsWith("-SNAPSHOT")) {
					URI.create("https://s01.oss.sonatype.org/content/repositories/snapshots/")
				} else {
					URI.create("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
				}
				repository.credentials { credentials ->
					credentials.username = localProperties.username
					credentials.password = localProperties.password
				}
			}
		}
	}
}