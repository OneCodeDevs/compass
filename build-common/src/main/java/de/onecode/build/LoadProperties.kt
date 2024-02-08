package de.onecode.build

import org.gradle.api.Project
import java.util.Properties

fun Project.loadProperties(): LocalProperties {
	val properties = Properties()
	val localProperties = rootProject.file("local.properties")
	if (localProperties.exists()) {
		properties.load(localProperties.inputStream())
	}

	val username = properties["maven.username"] as? String
		?: error("No maven.username provided in local.properties")
	val password = properties["maven.password"] as? String
		?: error("No maven.password provided in local.properties")
	return LocalProperties(
		username = username,
		password = password,
	)
}

data class LocalProperties(
	val username: String,
	val password: String,
)