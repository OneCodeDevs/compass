package io.redandroid.navigator.ksp.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.ksp.writeTo
import io.redandroid.navigator.ksp.descriptions.GraphDescription
import io.redandroid.navigator.ksp.generator.context.createCommonContext
import io.redandroid.navigator.ksp.generator.context.createContextClass
import io.redandroid.navigator.ksp.generator.navigation.createNavigatorComposable
import io.redandroid.navigator.ksp.generator.navigation.createSubGraphFunction
import io.redandroid.navigator.ksp.generator.screenbuilder.createScreenBuilder
import io.redandroid.navigator.ksp.generator.screenbuilder.createSubGraphBuilder
import io.redandroid.navigator.ksp.getNameOfHome

fun CodeGenerator.generateCode(graph: GraphDescription, dependencies: Dependencies) {
	val destinations = graph.destinations
	val subGraphs = graph.subGraphs

	val home = destinations.getNameOfHome()

	val startDestinationProperty = PropertySpec.builder(START_DESTINATION, String::class).addModifiers(KModifier.PRIVATE).initializer("%S", home).mutable(mutable = false).build()
	val navHostControllerLocalProperty = PropertySpec.builder(
		NAV_HOST_CONTROLLER_LOCAL,
		ClassName("androidx.compose.runtime", "ProvidableCompositionLocal").parameterizedBy(navHostControllerClass),
		KModifier.LATEINIT, KModifier.PRIVATE
	)
		.mutable(mutable = true)
		.build()

	val composeNavigationImports = listOfNotNull(
		"NavHost",
		"composable",
		if (subGraphs.isNotEmpty()) "navigation" else null
	).toTypedArray()

	val fileSpec = FileSpec.builder(PACKAGE, NAVIGATOR_COMPOSABLE_NAME)
		.addImport("androidx.navigation.compose", *composeNavigationImports)
		.addImport("androidx.compose.runtime", "CompositionLocalProvider", "compositionLocalOf")
		.addProperty(startDestinationProperty)
		.addProperty(navHostControllerLocalProperty)
		.addFunction(createNavigatorComposable(destinations))
		.apply {
			subGraphs.forEach { subGraph ->
				addFunction(createSubGraphFunction(subGraph))
			}
		}
		.addType(createScreenBuilder(destinations))
		.apply {
			subGraphs.forEach { subGraph ->
				addType(createSubGraphBuilder(subGraph))
			}
		}
		.addType(createCommonContext())
		.apply {
			(destinations + subGraphs.flatMap { it.destinations }).forEach { destination ->
				addType(createContextClass(destination))
			}
		}.build()

	fileSpec.writeTo(codeGenerator = this, dependencies)
}