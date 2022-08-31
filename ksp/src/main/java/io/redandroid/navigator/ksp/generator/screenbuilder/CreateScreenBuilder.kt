package io.redandroid.navigator.ksp.generator.screenbuilder

import com.squareup.kotlinpoet.TypeSpec
import io.redandroid.navigator.ksp.descriptions.DestinationDescription
import io.redandroid.navigator.ksp.generator.SCREEN_BUILDER

internal fun createScreenBuilder(destinations: List<DestinationDescription>): TypeSpec =
	TypeSpec.classBuilder(SCREEN_BUILDER)
		.addDestinationsToScreenBuilder(destinations)
		.build()