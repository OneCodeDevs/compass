package de.onecode.compass.ksp.generator.common

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.buildCodeBlock
import de.onecode.compass.ksp.capitalize
import de.onecode.compass.ksp.descriptions.NamedWithParameters
import de.onecode.compass.ksp.descriptions.ParameterDescription

fun NamedWithParameters.toNavigationFunction(
	navController: PropertySpec,
	defaultNavOptionsCodeBlock: () -> CodeBlock = {
		CodeBlock.builder()
			.beginControlFlow("")
			.endControlFlow()
			.build()
	},
): FunSpec {
	val (requiredParameters, optionalParameters) = parameters.partition { it.required }

	val paramsRoute = requiredParameters.joinToString(separator = "/") { "\${${it.name}}" }
	val paramsRouteWithSlash = if (paramsRoute.isNotBlank()) "/$paramsRoute" else ""
	val optionalQueryStatementName = "optionalQueryStatement"

	val navOptionsBlock = ParameterSpec
		.builder("navOptionsBlock", de.onecode.compass.ksp.generator.navOptionsBuilderLambdaName)
		.defaultValue(defaultNavOptionsCodeBlock())
		.build()

	val nameCapitalized = name.capitalize()
	return FunSpec.builder("navigateTo$nameCapitalized")
		.apply {
			requiredParameters.forEach { navigationParameter ->
				val parameterType = ClassName("", navigationParameter.type)
				addParameter(
					ParameterSpec.builder(navigationParameter.name, parameterType).build()
				)
			}
		}
		.apply {
			optionalParameters.forEach { navigationParameter ->
				val parameterType = ClassName("", navigationParameter.type).copy(nullable = true)
				addParameter(
					ParameterSpec.builder(navigationParameter.name, parameterType)
						.defaultValue("null")
						.build()
				)
			}
		}
		.addParameter(navOptionsBlock)
		.addOptionalParameterCode(optionalParameters, optionalQueryStatementName)
		.beginControlFlow("""%N.navigate("%L$%L")""", navController, "${name}$paramsRouteWithSlash", optionalQueryStatementName)
		.addStatement("%N()", navOptionsBlock)
		.endControlFlow()
		.build()
}

private fun FunSpec.Builder.addOptionalParameterCode(optionalParameters: List<ParameterDescription>, optionalQueryStatementName: String): FunSpec.Builder {
	return if (optionalParameters.isNotEmpty()) {
		addCode(
			buildCodeBlock {
				addStatement("val optionalParams = mutableListOf<String>()")
				optionalParameters.forEach { optional ->
					beginControlFlow("if(%L != null)", optional.name)
					addStatement("""optionalParams.add("%L=$%L")""", optional.name, optional.name)
					endControlFlow()
				}
				addStatement("""val prefix = if(optionalParams.isEmpty()) "" else "?"""")
				addStatement("""val %L = optionalParams.joinToString (prefix = prefix, separator = "&"){ it }""", optionalQueryStatementName)
			}
		)
	} else {
		addStatement("""val %L = "" """, optionalQueryStatementName)
	}
}