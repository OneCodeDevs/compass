package de.onecode.build.common

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

fun Project.configureDetekt() {
	configure<DetektExtension> {
		config.setFrom("${rootDir.absolutePath}/build-common/src/main/resources/detekt.yml")
		allRules = true
	}
}