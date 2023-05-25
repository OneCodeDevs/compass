package de.onecode.navigator.ksp.generator.navigation

import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import de.onecode.navigator.ksp.descriptions.DestinationDescription
import de.onecode.navigator.ksp.generator.composeAnnotation
import de.onecode.navigator.ksp.generator.derivedStateOfName
import de.onecode.navigator.ksp.generator.mutableStateClass
import de.onecode.navigator.ksp.generator.mutableStateOfName
import de.onecode.navigator.ksp.generator.navHostControllerClass
import de.onecode.navigator.ksp.generator.navigatorControllerClass
import de.onecode.navigator.ksp.generator.rememberName
import de.onecode.navigator.ksp.generator.stateClass
import de.onecode.navigator.ksp.getNameOfHome
import java.util.Locale

fun createNavigatorController(destinations: List<DestinationDescription>): TypeSpec {
	val navControllerParameterName = "navController"

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

	return TypeSpec.classBuilder(navigatorControllerClass)
		.primaryConstructor(
			FunSpec.constructorBuilder()
				.addParameter(navControllerParameterName, navHostControllerClass)
				.build()
		)
		.addProperty(PropertySpec.builder(navControllerParameterName, navHostControllerClass, KModifier.INTERNAL).initializer(navControllerParameterName).build())
		.addProperty(mutableCurrentDestinationState)
		.addProperty(currentDestinationState)
		.apply {
			destinations.topDestinations.forEach { topDestination ->
				addFunction(createCurrentDestinationFunction(topDestination, mutableCurrentDestinationState))
			}
		}
		.build()
}

private fun createCurrentDestinationFunction(topDestination: DestinationDescription, mutableCurrentDestinationState: PropertySpec): FunSpec {
	val nameCapitalized = topDestination.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
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

private val List<DestinationDescription>.topDestinations: List<DestinationDescription>
	get() = filter { it.isTop || it.isHome }