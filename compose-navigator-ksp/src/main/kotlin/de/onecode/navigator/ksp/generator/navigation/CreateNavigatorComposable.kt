package de.onecode.navigator.ksp.generator.navigation

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.UNIT
import de.onecode.navigator.ksp.descriptions.DestinationDescription
import de.onecode.navigator.ksp.generator.LOCAL_NAV_HOST_CONTROLLER
import de.onecode.navigator.ksp.generator.NAVIGATOR_COMPOSABLE_NAME
import de.onecode.navigator.ksp.generator.REGISTER_CURRENT_DESTINATION_LISTENER
import de.onecode.navigator.ksp.generator.REMEMBER_NAVIGATOR_CONTROLLER_NAME
import de.onecode.navigator.ksp.generator.composeAnnotation
import de.onecode.navigator.ksp.generator.composeModifierClass
import de.onecode.navigator.ksp.generator.navGraphBuilderClass
import de.onecode.navigator.ksp.generator.navigatorControllerClass
import de.onecode.navigator.ksp.generator.screenBuilderClass
import de.onecode.navigator.ksp.generator.screenBuilderImplClass
import de.onecode.navigator.ksp.getNameOfHome
import javax.annotation.processing.Generated

internal fun createNavigatorComposable(destinations: List<DestinationDescription>): FunSpec {
	val modifier = ParameterSpec.builder("modifier", composeModifierClass)
		.defaultValue("%T", composeModifierClass)
		.build()

	val navigatorController = ParameterSpec.builder("navigatorController", navigatorControllerClass)
		.defaultValue("%L()", REMEMBER_NAVIGATOR_CONTROLLER_NAME)
		.build()

	val screenBuilderLambda = LambdaTypeName.get(receiver = screenBuilderClass, returnType = UNIT, parameters = arrayOf(navGraphBuilderClass))
	val screenBuilderParam = ParameterSpec.builder("builder", screenBuilderLambda).build()

	val home = destinations.getNameOfHome()

	val navControllerVariable = "navController"

	return FunSpec.builder(NAVIGATOR_COMPOSABLE_NAME)
		.addAnnotation(Generated::class)
		.addAnnotation(composeAnnotation)
		.addParameter(modifier)
		.addParameter(navigatorController)
		.addParameter(screenBuilderParam)
		.addStatement("%N.%L()", navigatorController, REGISTER_CURRENT_DESTINATION_LISTENER)
		.addStatement("val %L = %N.%L", navControllerVariable, navigatorController, navControllerVariable)
		.addStatement("%L = compositionLocalOf { %L }", LOCAL_NAV_HOST_CONTROLLER, navControllerVariable)
		.beginControlFlow("CompositionLocalProvider(%L provides %L)", LOCAL_NAV_HOST_CONTROLLER, navControllerVariable)
		.beginControlFlow("NavHost(modifier = %N, startDestination = %S, navController = %L)", modifier, home, navControllerVariable)
		.addComposablesBody(destinations, screenBuilderImplClass)
		.endControlFlow()
		.endControlFlow()
		.build()
}
