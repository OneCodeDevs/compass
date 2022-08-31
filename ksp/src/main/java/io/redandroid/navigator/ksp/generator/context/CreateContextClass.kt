package io.redandroid.navigator.ksp.generator.context

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import io.redandroid.navigator.ksp.descriptions.DestinationDescription
import io.redandroid.navigator.ksp.descriptions.NavigationTarget
import io.redandroid.navigator.ksp.descriptions.ParameterDescription
import io.redandroid.navigator.ksp.generator.PACKAGE
import io.redandroid.navigator.ksp.generator.contextName
import io.redandroid.navigator.ksp.generator.navBackStackEntryClass
import io.redandroid.navigator.ksp.generator.navHostControllerClass
import io.redandroid.navigator.ksp.typeString

internal fun createContextClass(destination: DestinationDescription, parentName: String): TypeSpec {
	val navControllerParam = "navHostController"
	val navBackStackEntryParam = "navBackStackEntry"

	val commonContext = ClassName(PACKAGE, parentName)


	return TypeSpec.classBuilder(destination.contextName)
		.superclass(commonContext)
		.addSuperclassConstructorParameter(navControllerParam)
		.primaryConstructor(
			FunSpec.constructorBuilder()
				.addParameter(navControllerParam, navHostControllerClass)
				.addParameter(navBackStackEntryParam, navBackStackEntryClass)
				.build()
		)
		.addProperty(PropertySpec.builder(navControllerParam, navHostControllerClass, KModifier.PRIVATE).initializer(navControllerParam).build())
		.addProperty(PropertySpec.builder(navBackStackEntryParam, navBackStackEntryClass, KModifier.PRIVATE).initializer(navBackStackEntryParam).build())
		.apply {
			destination.parameters.forEach { parameter ->
				addProperty(parameter.toParameterProperty(navBackStackEntryParam))
			}

			destination.navigationTargets.forEach { navigationTarget ->
				addFunction(navigationTarget.toNavigationFunction(navControllerParam))
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

private fun NavigationTarget.toNavigationFunction(navControllerParam: String): FunSpec {
	val paramsRoute = parameters.joinToString(separator = "/") { "\${${it.name}}" }
	val paramsRouteWithSlash = if (paramsRoute.isNotBlank()) "/$paramsRoute" else ""
	return FunSpec.builder("navigateTo${name}")
		.apply {
			this@toNavigationFunction.parameters.forEach { navigationParameter ->
				val parameterType = ClassName("", navigationParameter.type)
				addParameter(
					ParameterSpec.builder(navigationParameter.name, parameterType).build()
				)
			}
		}
		.addStatement("%L.navigate(%P)", navControllerParam, "${name}$paramsRouteWithSlash")
		.build()
}