package de.onecode.navigator.ksp.generator.navigation

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import de.onecode.navigator.ksp.generator.REMEMBER_NAVIGATOR_CONTROLLER_NAME
import de.onecode.navigator.ksp.generator.composableClass
import de.onecode.navigator.ksp.generator.navHostControllerClass
import de.onecode.navigator.ksp.generator.navigatorControllerClass
import de.onecode.navigator.ksp.generator.rememberName
import de.onecode.navigator.ksp.generator.rememberNavControllerName
import javax.annotation.processing.Generated

fun createRememberNavigatorController(): FunSpec {
	val navControllerParameter = ParameterSpec.builder("navController", navHostControllerClass).defaultValue("%M()", rememberNavControllerName).build()

	return FunSpec.builder(REMEMBER_NAVIGATOR_CONTROLLER_NAME)
		.addAnnotation(Generated::class)
		.addAnnotation(composableClass)
		.addParameter(navControllerParameter)
		.returns(navigatorControllerClass)
		.beginControlFlow("return %M(key1 = %N)", rememberName, navControllerParameter)
		.addStatement("%T(%N)", navigatorControllerClass, navControllerParameter)
		.endControlFlow()
		.build()
}