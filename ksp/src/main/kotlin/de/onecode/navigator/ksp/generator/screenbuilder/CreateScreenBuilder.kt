package de.onecode.navigator.ksp.generator.screenbuilder

import com.squareup.kotlinpoet.TypeSpec
import de.onecode.navigator.ksp.descriptions.DestinationDescription
import de.onecode.navigator.ksp.generator.SCREEN_BUILDER

internal fun createScreenBuilder(destinations: List<DestinationDescription>): TypeSpec =
	TypeSpec.classBuilder(SCREEN_BUILDER)
		.addDestinationsToScreenBuilder(destinations)
		.build()