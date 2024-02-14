package de.onecode.compass.ksp.generator.navigation

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import de.onecode.compass.ksp.generator.REMEMBER_NAVIGATOR_CONTROLLER_NAME
import de.onecode.compass.ksp.generator.composableClass
import de.onecode.compass.ksp.generator.navHostControllerClass
import de.onecode.compass.ksp.generator.compassControllerClass
import de.onecode.compass.ksp.generator.rememberName
import de.onecode.compass.ksp.generator.rememberNavControllerName
import javax.annotation.processing.Generated

fun createRememberCompassController(): FunSpec {
	val navControllerParameter = ParameterSpec.builder("navController", navHostControllerClass).defaultValue("%M()", rememberNavControllerName).build()

	return FunSpec.builder(REMEMBER_NAVIGATOR_CONTROLLER_NAME)
		.addAnnotation(Generated::class)
		.addAnnotation(composableClass)
		.addParameter(navControllerParameter)
		.returns(compassControllerClass)
		.beginControlFlow("return %M(key1 = %N)", rememberName, navControllerParameter)
		.addStatement("%T(%N)", compassControllerClass, navControllerParameter)
		.endControlFlow()
		.build()
}