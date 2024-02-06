package de.onecode.navigator.ksp.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import de.onecode.navigator.ksp.descriptions.DestinationDescription
import de.onecode.navigator.ksp.descriptions.GraphDescription
import de.onecode.navigator.ksp.descriptions.SubGraphDescription
import de.onecode.navigator.ksp.generator.common.createParameterExtensionOnSavedStateHandle
import de.onecode.navigator.ksp.generator.context.createContextClass
import de.onecode.navigator.ksp.generator.context.createSubGraphContext
import de.onecode.navigator.ksp.generator.navigation.createNavigatorComposable
import de.onecode.navigator.ksp.generator.navigation.createNavigatorController
import de.onecode.navigator.ksp.generator.navigation.createRememberNavigatorController
import de.onecode.navigator.ksp.generator.navigation.createSubGraphFunction
import de.onecode.navigator.ksp.generator.screenbuilder.createNavHostBuilderComposable
import de.onecode.navigator.ksp.generator.screenbuilder.createScreenBuilderImplementation
import de.onecode.navigator.ksp.generator.screenbuilder.createScreenBuilderInterface
import de.onecode.navigator.ksp.generator.screenbuilder.createSubGraphBuilderImplementation
import de.onecode.navigator.ksp.generator.screenbuilder.createSubGraphBuilderInterface

fun CodeGenerator.generateNavigatorCode(graph: GraphDescription, dependencies: Dependencies) {
	val destinations = graph.destinations
	val subGraphs = graph.subGraphs

	val fileSpec = createFileSpec(graph)
		.addImport("androidx.compose.runtime", "CompositionLocalProvider", "compositionLocalOf")
		.apply {
			if (destinations.isNotEmpty()) {
				addType(createNavigatorController(destinations))
				addFunction(createRememberNavigatorController())
				addFunction(createNavigatorComposable(destinations))
				addType(createScreenBuilderInterface(destinations))
				addType(createScreenBuilderImplementation(destinations))

				destinations.forEach { destination ->
					addType(createContextClass(destination, COMMON_CONTEXT))
					addParameterExtensionsOnSavedStateHandle(destination)
				}
			}
			createSubGraphs(subGraphs)

		}
		.build()

	fileSpec.writeTo(codeGenerator = this, dependencies)
}

fun CodeGenerator.generateAddDestinationCode(graph: GraphDescription, dependencies: Dependencies) {
	val destinations = graph.destinations
	val fileSpec = createFileSpec(graph)
		.apply {
			if (destinations.isNotEmpty()) {
				destinations.forEach { destination ->
					addFunction(createNavHostBuilderComposable(destination))
					addType(createContextClass(destination, COMMON_CONTEXT))
					addParameterExtensionsOnSavedStateHandle(destination)
				}
			}

			createSubGraphs(graph.subGraphs)
		}
		.build()
	fileSpec.writeTo(codeGenerator = this, dependencies)
}

private fun createFileSpec(graph: GraphDescription): FileSpec.Builder {
	val destinations = graph.destinations
	val subGraphs = graph.subGraphs

	val composeNavigationImports = listOfNotNull(
		if (destinations.any { it.isHome }) "NavHost" else null,
		"composable",
		if (subGraphs.isNotEmpty()) "navigation" else null
	).toTypedArray()

	val navigationImports = arrayOf("NavType", "navArgument")

	val fileSuffix = (destinations + subGraphs.map { it.destinations }).hashCode().toString()
	val fileSpec = FileSpec.builder(PACKAGE, NAVIGATOR_COMPOSABLE_NAME + fileSuffix)
		.addImport("androidx.navigation.compose", *composeNavigationImports)
		.addImport("$PACKAGE.runtime", LOCAL_NAV_HOST_CONTROLLER, COMMON_CONTEXT)
		.apply {
			if (destinations.containsParameters() || subGraphs.hasParametrizedDestinations()) {
				addImport("androidx.navigation", *navigationImports)
			}
		}

	return fileSpec
}

private fun FileSpec.Builder.createSubGraphs(subGraphs: List<SubGraphDescription>) {
	subGraphs.forEach { subGraph ->
		addFunction(createSubGraphFunction(subGraph))
		addType(createSubGraphBuilderInterface(subGraph))
		addType(createSubGraphBuilderImplementation(subGraph))
		addType(createSubGraphContext(subGraph))
		subGraph.destinations.forEach { subGraphDestination ->
			addType(createContextClass(subGraphDestination, "${subGraph.name}$COMMON_CONTEXT"))
			addParameterExtensionsOnSavedStateHandle(subGraphDestination)
		}
	}
}

private fun FileSpec.Builder.addParameterExtensionsOnSavedStateHandle(destination: DestinationDescription) {
	if (destination.parameters.isNotEmpty()) {
		destination.parameters.forEach { parameter ->
			addFunction(createParameterExtensionOnSavedStateHandle(parameter))
		}
	}
}

private fun List<SubGraphDescription>.hasParametrizedDestinations(): Boolean =
	any { subGraph -> subGraph.destinations.any { it.parameters.isNotEmpty() } }

private fun List<DestinationDescription>.containsParameters(): Boolean =
	any { it.parameters.isNotEmpty() }
