package de.onecode.navigator.ksp.generator.navigation

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.buildCodeBlock
import de.onecode.navigator.ksp.decapitalize
import de.onecode.navigator.ksp.descriptions.DestinationDescription
import de.onecode.navigator.ksp.generator.LOCAL_NAV_HOST_CONTROLLER
import de.onecode.navigator.ksp.generator.contextName
import de.onecode.navigator.ksp.route
import de.onecode.navigator.ksp.typeString

internal fun FunSpec.Builder.addComposablesBody(destinations: List<DestinationDescription>, screenBuilderClass: ClassName): FunSpec.Builder {
	addStatement("val screenBuilder = %L()", screenBuilderClass.simpleName)
	addStatement("screenBuilder.builder(this)")
	destinations.forEach { destination ->
		addCode(destination.toNavigationComposableCodeBlock())
	}
	return this
}

private fun DestinationDescription.toNavigationComposableCodeBlock(): CodeBlock {
	val destinationScreenName = name.decapitalize()

	val navigationArgumentsBlock = buildCodeBlock {
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

	return CodeBlock.builder()
		.beginControlFlow("composable(route = %S, arguments = %L)", route, navigationArgumentsBlock)
		.addStatement("screenBuilder.%LComposable?.invoke(%L(%L.current, it))", destinationScreenName, contextName, LOCAL_NAV_HOST_CONTROLLER)
		.endControlFlow()
		.build()
}