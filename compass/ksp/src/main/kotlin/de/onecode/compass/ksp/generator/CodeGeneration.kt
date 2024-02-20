package de.onecode.compass.ksp.generator

import com.squareup.kotlinpoet.FileSpec
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.GraphDescription
import de.onecode.compass.ksp.descriptions.SubGraphDescription
import de.onecode.compass.ksp.generator.common.createParameterExtensionOnSavedStateHandle
import de.onecode.compass.ksp.generator.context.createContextClass
import de.onecode.compass.ksp.generator.context.createSubGraphContext
import de.onecode.compass.ksp.generator.navigation.createCompassComposable
import de.onecode.compass.ksp.generator.navigation.createCompassController
import de.onecode.compass.ksp.generator.navigation.createRememberCompassController
import de.onecode.compass.ksp.generator.navigation.createSubGraphFunction
import de.onecode.compass.ksp.generator.screenbuilder.createNavHostBuilderComposable
import de.onecode.compass.ksp.generator.screenbuilder.createScreenBuilderImplementation
import de.onecode.compass.ksp.generator.screenbuilder.createScreenBuilderInterface
import de.onecode.compass.ksp.generator.screenbuilder.createSubGraphBuilderImplementation
import de.onecode.compass.ksp.generator.screenbuilder.createSubGraphBuilderInterface

fun generateNavigatorCode(graph: GraphDescription): FileSpec {
	val destinations = graph.destinations
	val subGraphs = graph.subGraphs

	return createFileSpec(graph)
		.addImport("androidx.compose.runtime", "CompositionLocalProvider", "compositionLocalOf")
		.apply {
			if (destinations.isNotEmpty()) {
				addType(createCompassController(destinations))
				addFunction(createRememberCompassController())
				addFunction(createCompassComposable(destinations))
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
}

fun generateAddDestinationCode(graph: GraphDescription): FileSpec {
	val destinations = graph.destinations
	return createFileSpec(graph)
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
	val fileSpec = FileSpec.builder(PACKAGE, COMPASS_COMPOSABLE_NAME + fileSuffix)
		.addImport("androidx.navigation.compose", *composeNavigationImports)
		.addImport("$PACKAGE.runtime", LOCAL_NAV_HOST_CONTROLLER, COMMON_CONTEXT)
		.apply {
			if (destinations.containsParameters() || subGraphs.hasParametrizedDestinations()) {
				addImport("androidx.navigation", *navigationImports)
			}
			if (destinations.containsDeepLinks() || subGraphs.hasDestinationsWithDeepLinks()) {
				addImport("androidx.navigation", "NavDeepLink")
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
	any { it.destinations.containsParameters() }

private fun List<DestinationDescription>.containsParameters(): Boolean =
	any { it.parameters.isNotEmpty() }

private fun List<SubGraphDescription>.hasDestinationsWithDeepLinks(): Boolean =
	any { it.destinations.containsDeepLinks() }

private fun List<DestinationDescription>.containsDeepLinks(): Boolean =
	any { it.deepLinks.isNotEmpty() }
