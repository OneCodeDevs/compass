package de.onecode.compass.ksp.generator.context

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.ParameterDescription
import de.onecode.compass.ksp.generator.PACKAGE
import de.onecode.compass.ksp.generator.common.toNavigationFunction
import de.onecode.compass.ksp.generator.contextName
import de.onecode.compass.ksp.generator.navBackStackEntryClass
import de.onecode.compass.ksp.generator.navHostControllerClass
import de.onecode.compass.ksp.type
import de.onecode.compass.ksp.typeString
import javax.annotation.processing.Generated

internal fun createContextClass(destination: DestinationDescription, parentName: String): TypeSpec {
	val navControllerParamName = "navHostController"
	val navBackStackEntryParamName = "navBackStackEntry"

	val commonContext = ClassName(PACKAGE, parentName)
	val navController = PropertySpec
		.builder(navControllerParamName, navHostControllerClass, KModifier.PRIVATE)
		.initializer(navControllerParamName)
		.build()

	return TypeSpec.classBuilder(destination.contextName)
		.addAnnotation(Generated::class)
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
	val classType = type.type().copy(nullable = !required)

	return PropertySpec.builder(name, classType)
		.mutable(mutable = false)
		.getter(
			FunSpec.getterBuilder()
				.addStatement("val arg = %L.arguments?.get%L(%S)", navBackStackEntryParam, type.typeString(), name)
				.apply {
					if (required) {
						addStatement("?: error(%S)", "Required parameter $name not provided")
					}
				}
				.addStatement("return arg")
				.build()
		)
		.build()
}
