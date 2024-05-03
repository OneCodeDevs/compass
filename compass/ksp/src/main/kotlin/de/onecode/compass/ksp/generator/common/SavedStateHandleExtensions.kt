package de.onecode.compass.ksp.generator.common

import com.squareup.kotlinpoet.FunSpec
import de.onecode.compass.ksp.capitalize
import de.onecode.compass.ksp.descriptions.ParameterDescription
import de.onecode.compass.ksp.generator.savedStateHandleClass
import de.onecode.compass.ksp.type
import de.onecode.compass.ksp.typeString
import javax.annotation.processing.Generated

fun createParameterExtensionOnSavedStateHandle(parameter: ParameterDescription): FunSpec {
	val parameterName = parameter.name
	return FunSpec.builder("get${parameterName.capitalize()}")
		.addAnnotation(Generated::class)
		.receiver(savedStateHandleClass)
		.returns(parameter.type.type().copy(nullable = !parameter.required))
		.addStatement("val arg = get<%L>(%S)", parameter.type.typeString(), parameterName)
		.apply {
			if (parameter.required) {
				addStatement("?: error(%S)", "Required parameter $parameterName not provided")
			}
		}
		.addStatement("return arg")
		.build()
}
