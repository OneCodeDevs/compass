package de.onecode.compass.ksp.generator.navigation

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.buildCodeBlock
import de.onecode.compass.ksp.decapitalize
import de.onecode.compass.ksp.descriptions.DeepLinkDescription
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.ParameterDescription
import de.onecode.compass.ksp.generator.LOCAL_NAV_HOST_CONTROLLER
import de.onecode.compass.ksp.generator.contextName
import de.onecode.compass.ksp.route
import de.onecode.compass.ksp.typeString

internal fun FunSpec.Builder.addComposablesBody(
	destinations: List<DestinationDescription>,
	screenBuilderClass: ClassName,
): FunSpec.Builder {
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
		val arguments = navigationArgumentsCodeBlock(destination.parameters)
		val deepLinks = deepLinksCodeBlock(destination.deepLinks, destination.parameters)
		beginControlFlow(
			"composable(route = %S, arguments = %L, deepLinks = %L)",
			destination.route,
			arguments,
			deepLinks
		)
		statements()
		endControlFlow()
	}

private fun navigationArgumentsCodeBlock(parameters: List<ParameterDescription>): CodeBlock =
	buildCodeBlock {
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

private fun deepLinksCodeBlock(deepLinks: List<DeepLinkDescription>, parameters: List<ParameterDescription>): CodeBlock =
	buildCodeBlock {
		if (deepLinks.isEmpty()) {
			addStatement("emptyList()")
		} else {
			val parametersSuffix = parameters.joinToString(separator = "/") { "{${it.name}}" }
			val navDeepLink = deepLinks.map { description ->
				val deepLink = description.toUri(parametersSuffix)
				buildCodeBlock {
					addStatement("NavDeepLink(%S)", deepLink)
				}
			}

			addStatement("listOf(%L)", navDeepLink.joinToString())
		}
	}

private fun DeepLinkDescription.toUri(parametersSuffix: String): String {
	val uri = "${schema}://${host}"
	return if (path.isBlank()) {
		uri
	} else {
		"$uri/$path"
	}.let {
		if (parametersSuffix.isBlank()) {
			it
		} else {
			"$it/$parametersSuffix"
		}
	}
}

