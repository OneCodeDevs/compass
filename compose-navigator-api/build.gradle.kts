import java.net.URI
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.kotlin.jvm)
	id("maven-publish")
	signing
}

val properties = Properties()
val localProperties = project.rootProject.file("local.properties")
if (localProperties.exists()) {
	properties.load(localProperties.inputStream())
}

java {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
	withJavadocJar()
	withSourcesJar()
}

tasks.withType<PublishToMavenLocal>().configureEach {
	dependsOn("build")
}
tasks.withType<PublishToMavenRepository>().configureEach {
	dependsOn("build")
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			pom {
				name.set("Compose Navigator API")
				description.set("The API part of the compose Navigator")
				url.set("https://github.com/OneCodeDevs/navigator")
				licenses {
					license {
						name.set("The Apache License, Version 2.0")
						url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
					}
				}
				developers {
					developer {
						id.set("a-frank")
						name.set("Alexander Frank")
						email.set("alexander.frank@onecode.de")
					}
					developer {
						id.set("pynnie")
						name.set("Sebastian Hecken")
						email.set("sebastian.hecken@onecode.de")
					}
				}
				scm {
					connection.set("https://github.com/OneCodeDevs/navigator.git")
					developerConnection.set("https://github.com/OneCodeDevs/navigator.git")
					url.set("https://github.com/OneCodeDevs/navigator")
				}
			}

			groupId = "de.onecode"
			artifactId = "compose-navigator-api"
			version = libs.versions.compose.navigator.get()

			from(components["kotlin"])
			artifact(tasks["sourcesJar"])
			artifact(tasks["javadocJar"])
		}
	}
	repositories {
		maven {
			url = if (libs.versions.compose.navigator.get().toUpperCase().endsWith("-SNAPSHOT")) {
				URI.create("https://s01.oss.sonatype.org/content/repositories/snapshots/")
			} else {
				URI.create("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
			}
			credentials {
				username = properties["maven.username"] as? String
				password = properties["maven.password"] as? String
			}
		}
	}
}

signing {
	sign(publishing.publications["maven"])
}