package io.redandroid.navigator.ksp.generator.navigation

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.UNIT
import io.redandroid.navigator.ksp.descriptions.DestinationDescription
import io.redandroid.navigator.ksp.generator.NAVIGATOR_COMPOSABLE_NAME
import io.redandroid.navigator.ksp.generator.NAV_HOST_CONTROLLER_LOCAL
import io.redandroid.navigator.ksp.generator.composeAnnotation
import io.redandroid.navigator.ksp.generator.navGraphBuilderClass
import io.redandroid.navigator.ksp.generator.navHostControllerClass
import io.redandroid.navigator.ksp.generator.rememberNavControllerName
import io.redandroid.navigator.ksp.generator.screenBuilderClass
import io.redandroid.navigator.ksp.getNameOfHome

internal fun createNavigatorComposable(destinations: List<DestinationDescription>): FunSpec {
	val navHostControllerParam = ParameterSpec.builder("navHostController", navHostControllerClass)
		.defaultValue("%M()", rememberNavControllerName)
		.build()

	val screenBuilderLambda = LambdaTypeName.get(receiver = screenBuilderClass, returnType = UNIT, parameters = arrayOf(navGraphBuilderClass))
	val screenBuilderParam = ParameterSpec.builder("builder", screenBuilderLambda).build()

	val home = destinations.getNameOfHome()

	return FunSpec.builder(NAVIGATOR_COMPOSABLE_NAME)
		.addAnnotation(composeAnnotation)
		.addParameter(navHostControllerParam)
		.addParameter(screenBuilderParam)
		.addStatement("%L = compositionLocalOf { navHostController }", NAV_HOST_CONTROLLER_LOCAL)
		.beginControlFlow("CompositionLocalProvider(%L provides navHostController)", NAV_HOST_CONTROLLER_LOCAL)
		.beginControlFlow("NavHost(startDestination = %S, navController = navHostController)", home)
		.addComposablesBody(destinations, screenBuilderClass)
		.endControlFlow()
		.endControlFlow()
		.build()
}
