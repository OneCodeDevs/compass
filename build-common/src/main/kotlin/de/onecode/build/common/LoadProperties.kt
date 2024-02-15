package de.onecode.build.common

import org.gradle.api.Project
import java.util.Properties

fun Project.loadProperties(): LocalProperties {
	val properties = Properties()
	val localProperties = rootProject.file("local.properties")
	if (localProperties.exists()) {
		properties.load(localProperties.inputStream())
	}

	val envUsername = System.getenv("MAVEN_USERNAME")
	val envPassword = System.getenv("MAVEN_PASSWORD")

	val propUsername = properties["maven.username"] as? String
	val propPassword = properties["maven.password"] as? String

	return LocalProperties(
		username = envUsername
			?: propUsername,
		password = envPassword
			?: propPassword,
	)
}

data class LocalProperties(
	val username: String?,
	val password: String?,
)