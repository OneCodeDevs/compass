package de.onecode.compass.ksp.generator.navigation

import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import de.onecode.compass.ksp.capitalize
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.generator.REGISTER_CURRENT_DESTINATION_LISTENER
import de.onecode.compass.ksp.generator.common.toNavigationFunction
import de.onecode.compass.ksp.generator.compassControllerClass
import de.onecode.compass.ksp.generator.composeAnnotation
import de.onecode.compass.ksp.generator.derivedStateOfName
import de.onecode.compass.ksp.generator.disposableEffectName
import de.onecode.compass.ksp.generator.mutableStateClass
import de.onecode.compass.ksp.generator.mutableStateOfName
import de.onecode.compass.ksp.generator.navHostControllerClass
import de.onecode.compass.ksp.generator.onDestinationChangedListenerClass
import de.onecode.compass.ksp.generator.rememberName
import de.onecode.compass.ksp.generator.stateClass
import de.onecode.compass.ksp.getNameOfHome
import javax.annotation.processing.Generated

fun createCompassController(destinations: List<DestinationDescription>): TypeSpec {
	val navControllerParameterName = "navController"
	val navController = PropertySpec.builder(navControllerParameterName, navHostControllerClass, KModifier.INTERNAL).initializer(navControllerParameterName).build()

	val nullableString = STRING.copy(nullable = true)
	val mutableCurrentDestinationState = PropertySpec.builder("_currentDestinationName", mutableStateClass.parameterizedBy(nullableString), KModifier.PRIVATE)
		.initializer(
			CodeBlock.builder()
				.addStatement("%M<%T>(%S)", mutableStateOfName, nullableString, destinations.getNameOfHome())
				.build()
		)
		.build()

	val currentDestinationState = PropertySpec.builder("currentDestinationName", stateClass.parameterizedBy(nullableString), KModifier.PUBLIC)
		.initializer("%N", mutableCurrentDestinationState)
		.build()


	return TypeSpec.classBuilder(compassControllerClass)
		.addAnnotation(Generated::class)
		.primaryConstructor(
			FunSpec.constructorBuilder()
				.addParameter(navControllerParameterName, navHostControllerClass)
				.build()
		)
		.addProperty(navController)
		.addProperty(mutableCurrentDestinationState)
		.addProperty(currentDestinationState)
		.addFunction(createRegisterCurrentDestinationListenerFunction(navController, mutableCurrentDestinationState))
		.addTopDestinationFunctions(destinations, mutableCurrentDestinationState, navController)
		.build()
}

private fun createRegisterCurrentDestinationListenerFunction(navController: PropertySpec, mutableCurrentDestinationState: PropertySpec): FunSpec {
	val listenerVariableName = "listener"
	// TODO create interface and make RegisterCurrentDestinationListener only available in the impl
	return FunSpec.builder(REGISTER_CURRENT_DESTINATION_LISTENER)
		.addAnnotation(composeAnnotation)
		.beginControlFlow("%M(key1 = %N)", disposableEffectName, navController)
		.addCode(
			CodeBlock.builder()
				.addStatement("val %L = %T { _, destination, _ ->", listenerVariableName, onDestinationChangedListenerClass)
				.addStatement("	%N.value = destination.route", mutableCurrentDestinationState)
				.addStatement("}")
				.build()
		)
		.addStatement("%N.addOnDestinationChangedListener(%N)", navController, listenerVariableName)
		.beginControlFlow("onDispose")
		.addStatement("%N.removeOnDestinationChangedListener(%N)", navController, listenerVariableName)
		.endControlFlow()
		.endControlFlow()
		.build()
}

private fun createCurrentDestinationFunction(topDestination: DestinationDescription, mutableCurrentDestinationState: PropertySpec): FunSpec {
	val nameCapitalized = topDestination.name.capitalize()
	return FunSpec.builder("currentDestinationIs$nameCapitalized")
		.addAnnotation(composeAnnotation)
		.returns(stateClass.parameterizedBy(BOOLEAN))
		.beginControlFlow("return %M", rememberName)
		.beginControlFlow("%M", derivedStateOfName)
		.addStatement("%N.value == %S", mutableCurrentDestinationState, topDestination.name)
		.endControlFlow()
		.endControlFlow()
		.build()
}

private fun TypeSpec.Builder.addTopDestinationFunctions(
	destinations: List<DestinationDescription>,
	mutableCurrentDestinationState: PropertySpec,
	navController: PropertySpec,
): TypeSpec.Builder = apply {
	destinations.topDestinations.forEach { topDestination ->
		addFunction(createCurrentDestinationFunction(topDestination, mutableCurrentDestinationState))
		addFunction(topDestination.toNavigationFunction(navController) {
			CodeBlock.builder()
				.beginControlFlow("")
				.beginControlFlow("popUpTo(%S)", destinations.getNameOfHome())
				.apply {
					if (topDestination.isHome) {
						addStatement("inclusive = true")
					}
				}
				.endControlFlow()
				.endControlFlow()
				.build()
		})
	}
}

private val List<DestinationDescription>.topDestinations: List<DestinationDescription>
	get() = filter { it.isTop || it.isHome }
