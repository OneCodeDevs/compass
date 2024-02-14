package de.onecode.build.common

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

internal fun Project.versionCatalog(): VersionCatalog = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

internal val VersionCatalog.java: String
	get() = findVersion("java").get().toString()

internal val VersionCatalog.androidMinSdk: Int
	get() = findVersion("android-sdk-min").get().toString().toInt()

internal val VersionCatalog.androidTargetSdk: Int
	get() = findVersion("android-sdk-target").get().toString().toInt()

internal val VersionCatalog.composeCompiler: String
	get() = findVersion("compose-compiler").get().toString()