package de.onecode.navigator.ksp.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import de.onecode.navigator.ksp.descriptions.GraphDescription
import de.onecode.navigator.ksp.generator.context.createContextClass
import de.onecode.navigator.ksp.generator.context.createSubGraphContext
import de.onecode.navigator.ksp.generator.navigation.createNavigatorComposable
import de.onecode.navigator.ksp.generator.navigation.createNavigatorController
import de.onecode.navigator.ksp.generator.navigation.createRememberNavigatorController
import de.onecode.navigator.ksp.generator.navigation.createSubGraphFunction
import de.onecode.navigator.ksp.generator.screenbuilder.createScreenBuilderImplementation
import de.onecode.navigator.ksp.generator.screenbuilder.createScreenBuilderInterface
import de.onecode.navigator.ksp.generator.screenbuilder.createSubGraphBuilderImplementation
import de.onecode.navigator.ksp.generator.screenbuilder.createSubGraphBuilderInterface

fun CodeGenerator.generateCode(graph: GraphDescription, dependencies: Dependencies) {
	val destinations = graph.destinations
	val subGraphs = graph.subGraphs

	val composeNavigationImports = listOfNotNull(
		"NavHost",
		"composable",
		if (subGraphs.isNotEmpty()) "navigation" else null
	).toTypedArray()

	val fileSuffix = (destinations + subGraphs.map { it.destinations }).hashCode().toString()
	val fileSpec = FileSpec.builder(PACKAGE, NAVIGATOR_COMPOSABLE_NAME + fileSuffix)
		.addImport("androidx.navigation.compose", *composeNavigationImports)
		.addImport("androidx.compose.runtime", "CompositionLocalProvider", "compositionLocalOf")
		.addImport("de.onecode.navigator.runtime", LOCAL_NAV_HOST_CONTROLLER, COMMON_CONTEXT)
		.apply {
			if (destinations.isNotEmpty()) {
				addType(createNavigatorController(destinations))
				addFunction(createRememberNavigatorController())
				addFunction(createNavigatorComposable(destinations))
				addType(createScreenBuilderInterface(destinations))
				addType(createScreenBuilderImplementation(destinations))
			}

			destinations.forEach { destination ->
				addType(createContextClass(destination, COMMON_CONTEXT))
			}
			subGraphs.forEach { subGraph ->
				addFunction(createSubGraphFunction(subGraph))
				addType(createSubGraphBuilderInterface(subGraph))
				addType(createSubGraphBuilderImplementation(subGraph))
				addType(createSubGraphContext(subGraph))
				subGraph.destinations.forEach { subGraphDestination ->
					addType(createContextClass(subGraphDestination, "${subGraph.name}$COMMON_CONTEXT"))
				}
			}
		}
		.build()

	fileSpec.writeTo(codeGenerator = this, dependencies)
}