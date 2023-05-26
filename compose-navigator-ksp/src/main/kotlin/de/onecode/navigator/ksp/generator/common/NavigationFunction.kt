package de.onecode.navigator.ksp.generator.common

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import de.onecode.navigator.ksp.descriptions.NamedWithParameters
import de.onecode.navigator.ksp.generator.navOptionsBuilderLambdaName
import java.util.Locale

fun NamedWithParameters.toNavigationFunction(
	navController: PropertySpec,
	defaultNavOptionsCodeBlock: () -> CodeBlock = {
		CodeBlock.builder()
			.beginControlFlow("")
			.addStatement("this")
			.endControlFlow()
			.build()
	}
): FunSpec {
	val paramsRoute = parameters.joinToString(separator = "/") { "\${${it.name}}" }
	val paramsRouteWithSlash = if (paramsRoute.isNotBlank()) "/$paramsRoute" else ""

	val navOptionsBlock = ParameterSpec.builder("navOptionsBlock", navOptionsBuilderLambdaName)
		.defaultValue(defaultNavOptionsCodeBlock())
		.build()

	val nameCapitalized = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
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