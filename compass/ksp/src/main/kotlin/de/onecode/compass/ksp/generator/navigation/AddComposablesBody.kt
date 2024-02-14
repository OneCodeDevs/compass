package de.onecode.compass.ksp.generator.navigation

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.buildCodeBlock
import de.onecode.compass.ksp.decapitalize
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.generator.LOCAL_NAV_HOST_CONTROLLER
import de.onecode.compass.ksp.generator.contextName
import de.onecode.compass.ksp.route
import de.onecode.compass.ksp.typeString

internal fun FunSpec.Builder.addComposablesBody(destinations: List<DestinationDescription>, screenBuilderClass: ClassName): FunSpec.Builder {
	addStatement("val screenBuilder = %L()", screenBuilderClass.simpleName)
	addStatement("screenBuilder.builder(this)")
	destinations.forEach { destination ->
		addCode(destination.toNavigationComposableCodeBlock())
	}
	return this
}

internal fun FunSpec.Builder.addComposableBody(destination: DestinationDescription): FunSpec.Builder {
	addCode(
		buildComposableCodeBlock(destination) {
			addStatement("val current = LocalNavHostController.current")
			addStatement("val context = %L(current, it)", destination.contextName)
			addStatement("composable(context)")
		}
	)
	return this
}

private fun DestinationDescription.toNavigationComposableCodeBlock(): CodeBlock {
	val destinationScreenName = name.decapitalize()

	return buildComposableCodeBlock(this) {
		addStatement("screenBuilder.%LComposable?.invoke(%L(%L.current, it))", destinationScreenName, contextName, LOCAL_NAV_HOST_CONTROLLER)
	}
}

private fun buildComposableCodeBlock(destination: DestinationDescription, statements: CodeBlock.Builder.() -> Unit): CodeBlock =
	buildCodeBlock {
		beginControlFlow("composable(route = %S, arguments = %L)", destination.route, navigationArgumentsCodeBlock(destination))
		statements()
		endControlFlow()
	}

private fun navigationArgumentsCodeBlock(description: DestinationDescription): CodeBlock {
	val parameters = description.parameters
	return buildCodeBlock {
		if (parameters.isEmpty()) {
			addStatement("emptyList()")
		} else {
			val blocks = parameters.map {
				val navType = "${it.type.typeString()}Type"
				buildCodeBlock {
					beginControlFlow("navArgument(name = %S)", it.name)
					addStatement("type = NavType.%L", navType)
					endControlFlow()
				}
			}

			addStatement("listOf(%L)", blocks.joinToString())
		}
	}
}