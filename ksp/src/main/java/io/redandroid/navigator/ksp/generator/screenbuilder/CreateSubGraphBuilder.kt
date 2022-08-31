package io.redandroid.navigator.ksp.generator.screenbuilder

import com.squareup.kotlinpoet.TypeSpec
import io.redandroid.navigator.ksp.descriptions.SubGraphDescription
import io.redandroid.navigator.ksp.screenBuilderName

internal fun createSubGraphBuilder(subGraph: SubGraphDescription): TypeSpec =
	TypeSpec.classBuilder(subGraph.screenBuilderName)
		.addDestinationsToScreenBuilder(subGraph.destinations)
		.build()