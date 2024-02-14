package de.onecode.compass.ksp.generator.common

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import de.onecode.compass.ksp.capitalize
import de.onecode.compass.ksp.descriptions.NamedWithParameters

fun NamedWithParameters.toNavigationFunction(
	navController: PropertySpec,
	defaultNavOptionsCodeBlock: () -> CodeBlock = {
		CodeBlock.builder()
			.beginControlFlow("")
			.endControlFlow()
			.build()
	}
): FunSpec {
	val paramsRoute = parameters.joinToString(separator = "/") { "\${${it.name}}" }
	val paramsRouteWithSlash = if (paramsRoute.isNotBlank()) "/$paramsRoute" else ""

	val navOptionsBlock = ParameterSpec.builder("navOptionsBlock", de.onecode.compass.ksp.generator.navOptionsBuilderLambdaName)
		.defaultValue(defaultNavOptionsCodeBlock())
		.build()

	val nameCapitalized = name.capitalize()
	return FunSpec.builder("navigateTo$nameCapitalized")
		.apply {
			this@toNavigationFunction.parameters.forEach { navigationParameter ->
				val parameterType = ClassName("", navigationParameter.type)
				addParameter(
					ParameterSpec.builder(navigationParameter.name, parameterType).build()
				)
			}
		}
		.addParameter(navOptionsBlock)
		.beginControlFlow("%N.navigate(%P)", navController, "${name}$paramsRouteWithSlash")
		.addStatement("%N()", navOptionsBlock)
		.endControlFlow()
		.build()
}