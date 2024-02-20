package de.onecode.compass.ksp.generator.navigation

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.UNIT
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.generator.LOCAL_NAV_HOST_CONTROLLER
import de.onecode.compass.ksp.generator.COMPASS_COMPOSABLE_NAME
import de.onecode.compass.ksp.generator.REGISTER_CURRENT_DESTINATION_LISTENER
import de.onecode.compass.ksp.generator.REMEMBER_NAVIGATOR_CONTROLLER_NAME
import de.onecode.compass.ksp.generator.composeAnnotation
import de.onecode.compass.ksp.generator.composeModifierClass
import de.onecode.compass.ksp.generator.navGraphBuilderClass
import de.onecode.compass.ksp.generator.compassControllerClass
import de.onecode.compass.ksp.generator.screenBuilderClass
import de.onecode.compass.ksp.generator.screenBuilderImplClass
import de.onecode.compass.ksp.getNameOfHome
import javax.annotation.processing.Generated

internal fun createCompassComposable(destinations: List<DestinationDescription>): FunSpec {
	val modifier = ParameterSpec.builder("modifier", composeModifierClass)
		.defaultValue("%T", composeModifierClass)
		.build()

	val compassController = ParameterSpec.builder("compassController", compassControllerClass)
		.defaultValue("%L()", REMEMBER_NAVIGATOR_CONTROLLER_NAME)
		.build()

	val screenBuilderLambda = LambdaTypeName.get(receiver = screenBuilderClass, returnType = UNIT, parameters = arrayOf(navGraphBuilderClass))
	val screenBuilderParam = ParameterSpec.builder("builder", screenBuilderLambda).build()

	val home = destinations.getNameOfHome()

	val navControllerVariable = "navController"

	return FunSpec.builder(COMPASS_COMPOSABLE_NAME)
		.addAnnotation(Generated::class)
		.addAnnotation(composeAnnotation)
		.addParameter(modifier)
		.addParameter(compassController)
		.addParameter(screenBuilderParam)
		.addStatement("%N.%L()", compassController, REGISTER_CURRENT_DESTINATION_LISTENER)
		.addStatement("val %L = %N.%L", navControllerVariable, compassController, navControllerVariable)
		.addStatement("%L = compositionLocalOf { %L }", LOCAL_NAV_HOST_CONTROLLER, navControllerVariable)
		.beginControlFlow("CompositionLocalProvider(%L provides %L)", LOCAL_NAV_HOST_CONTROLLER, navControllerVariable)
		.beginControlFlow("NavHost(modifier = %N, startDestination = %S, navController = %L)", modifier, home, navControllerVariable)
		.addComposablesBody(destinations, screenBuilderImplClass)
		.endControlFlow()
		.endControlFlow()
		.build()
}
