package de.onecode.navigator.ksp.generator.screenbuilder

import com.squareup.kotlinpoet.TypeSpec
import de.onecode.navigator.ksp.descriptions.SubGraphDescription
import de.onecode.navigator.ksp.screenBuilderName

internal fun createSubGraphBuilder(subGraph: SubGraphDescription): TypeSpec =
	TypeSpec.classBuilder(subGraph.screenBuilderName)
		.addDestinationsToScreenBuilder(subGraph.destinations)
		.build()