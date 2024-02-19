package de.onecode.compass.ksp.generator.screenbuilder

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import de.onecode.compass.ksp.decapitalize
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.generator.PACKAGE
import de.onecode.compass.ksp.generator.composeAnnotation
import de.onecode.compass.ksp.generator.contextName
import de.onecode.compass.ksp.generator.navGraphBuilderClass
import de.onecode.compass.ksp.generator.navigation.addComposableBody

internal fun TypeSpec.Builder.addAbstractDestinationFunctions(destinations: List<DestinationDescription>): TypeSpec.Builder {
	destinations.forEach { destination ->
		addFunction(
			createDestinationFunctionBuilder(destination)
				.addModifiers(KModifier.ABSTRACT)
				.build()
		)
	}
	return this
}

internal fun TypeSpec.Builder.addDestinationPropertiesAndFunctions(destinations: List<DestinationDescription>): TypeSpec.Builder {
	destinations.forEach { destination ->
		val destinationScreenName = destination.name.decapitalize()
		val composableProperty = "${destinationScreenName}Composable"
		val destinationContextClass = ClassName(PACKAGE, destination.contextName)
		val destinationContextLambda = LambdaTypeName
			.get(receiver = destinationContextClass, returnType = UNIT)
			.copy(annotations = listOf(composeAnnotation))

		addProperty(
			PropertySpec.builder(composableProperty, destinationContextLambda.copy(nullable = true), KModifier.INTERNAL)
				.initializer("null")
				.mutable(mutable = true)
				.build()
		)
		addFunction(
			createDestinationFunctionBuilder(destination)
				.addModifiers(KModifier.OVERRIDE)
				.addStatement("%L = composable", composableProperty)
				.build()
		)
	}
	return this
}

internal fun createNavHostBuilderComposable(destination: DestinationDescription): FunSpec =
	createDestinationFunctionBuilder(destination)
		.receiver(navGraphBuilderClass)
		.addComposableBody(destination)
		.build()

private fun createDestinationFunctionBuilder(destination: DestinationDescription): FunSpec.Builder {
	val destinationScreenName = destination.name.decapitalize()
	val destinationContextClass = ClassName(PACKAGE, destination.contextName)
	val destinationContextLambda = LambdaTypeName.get(receiver = destinationContextClass, returnType = UNIT).copy(annotations = listOf(composeAnnotation))

	return FunSpec.builder("${destinationScreenName}Screen")
		.addParameter(ParameterSpec.builder("composable", destinationContextLambda).build())
}
