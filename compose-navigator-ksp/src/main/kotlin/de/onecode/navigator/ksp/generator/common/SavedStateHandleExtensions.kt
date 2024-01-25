package de.onecode.navigator.ksp.generator.common

import com.squareup.kotlinpoet.FunSpec
import de.onecode.navigator.ksp.capitalize
import de.onecode.navigator.ksp.descriptions.ParameterDescription
import de.onecode.navigator.ksp.generator.savedStateHandleClass
import de.onecode.navigator.ksp.type
import de.onecode.navigator.ksp.typeString
import javax.annotation.processing.Generated

fun createParameterExtensionOnSavedStateHandle(parameter: ParameterDescription): FunSpec {
	val parameterName = parameter.name
	return FunSpec.builder("get${parameterName.capitalize()}")
		.addAnnotation(Generated::class)
		.receiver(savedStateHandleClass)
		.returns(parameter.type.type())
		.addStatement(
			"return get<%L>(%S) ?: error(%S)",
			parameter.type.typeString(),
			parameterName,
			"Required parameter $parameterName not provided"
		)
		.build()
}