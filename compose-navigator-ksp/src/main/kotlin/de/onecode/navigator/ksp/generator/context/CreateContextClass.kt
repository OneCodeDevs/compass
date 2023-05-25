package de.onecode.navigator.ksp.generator.context

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import de.onecode.navigator.ksp.descriptions.DestinationDescription
import de.onecode.navigator.ksp.descriptions.ParameterDescription
import de.onecode.navigator.ksp.generator.PACKAGE
import de.onecode.navigator.ksp.generator.common.toNavigationFunction
import de.onecode.navigator.ksp.generator.contextName
import de.onecode.navigator.ksp.generator.navBackStackEntryClass
import de.onecode.navigator.ksp.generator.navHostControllerClass
import de.onecode.navigator.ksp.typeString

internal fun createContextClass(destination: DestinationDescription, parentName: String): TypeSpec {
	val navControllerParamName = "navHostController"
	val navBackStackEntryParamName = "navBackStackEntry"

	val commonContext = ClassName(PACKAGE, parentName)
	val navController = PropertySpec.builder(navControllerParamName, navHostControllerClass, KModifier.PRIVATE).initializer(navControllerParamName).build()

	return TypeSpec.classBuilder(destination.contextName)
		.superclass(commonContext)
		.addSuperclassConstructorParameter(navControllerParamName)
		.primaryConstructor(
			FunSpec.constructorBuilder()
				.addParameter(navControllerParamName, navHostControllerClass)
				.addParameter(navBackStackEntryParamName, navBackStackEntryClass)
				.build()
		)
		.addProperty(navController)
		.addProperty(PropertySpec.builder(navBackStackEntryParamName, navBackStackEntryClass, KModifier.PRIVATE).initializer(navBackStackEntryParamName).build())
		.apply {
			destination.parameters.forEach { parameter ->
				addProperty(parameter.toParameterProperty(navBackStackEntryParamName))
			}

			destination.navigationTargets.forEach { navigationTarget ->
				addFunction(navigationTarget.toNavigationFunction(navController))
			}
		}
		.build()
}

private fun ParameterDescription.toParameterProperty(navBackStackEntryParam: String): PropertySpec {
	val typeString = type.typeString()
	val typeConverter = if (typeString == String::class.simpleName) {
		""
	} else {
		"?.to${typeString}OrNull()"
	}

	return PropertySpec.builder(name, ClassName("", type))
		.mutable(mutable = false)
		.getter(
			FunSpec.getterBuilder()
				.addStatement(
					"return %L.arguments?.getString(%S)$typeConverter ?: error(%S)",
					navBackStackEntryParam,
					name,
					"Required parameter $name not provided"
				)
				.build()
		)
		.build()
}