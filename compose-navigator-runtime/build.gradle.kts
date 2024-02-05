import java.net.URI
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	`maven-publish`
	signing
}

val properties = Properties()
val localProperties = project.rootProject.file("local.properties")
if (localProperties.exists()) {
	properties.load(localProperties.inputStream())
}

android {
	namespace = "de.oencode.navigator.runtime"
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

}

dependencies {
	implementation(libs.compose.navigation)
}

val sourcesJar = tasks.register("sourcesJar", Jar::class) {
	from(android.sourceSets["main"].java.getSourceFiles())
	archiveClassifier.set("sources")
}

val javadoc = tasks.register("javadoc", Javadoc::class) {
	source = android.sourceSets["main"].java.getSourceFiles()
	classpath += project.files(android.bootClasspath)
}

val javadocJar = tasks.register("javadocJar", Jar::class) {
	dependsOn(javadoc)
	from(javadoc)
	archiveClassifier.set("javadoc")
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
				name.set("Compose Navigator Runtime")
				description.set("The Runtime part of the compose Navigator")
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
			artifactId = "compose-navigator-runtime"
			version = libs.versions.compose.navigator.get()

			artifact("${layout.buildDirectory.asFile.get().absolutePath}/outputs/aar/${artifactId}-release.aar")
			artifact(sourcesJar)
			artifact(javadocJar)
		}
	}
	repositories {
		maven {
			url = if (libs.versions.compose.navigator.get().uppercase().endsWith("-SNAPSHOT")) {
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

tasks.named("signMavenPublication") {
	dependsOn("bundleReleaseAar")
}