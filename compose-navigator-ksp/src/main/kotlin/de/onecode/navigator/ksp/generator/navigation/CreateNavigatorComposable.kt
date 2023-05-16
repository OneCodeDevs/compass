package de.onecode.navigator.ksp.generator.navigation

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.UNIT
import de.onecode.navigator.ksp.descriptions.DestinationDescription
import de.onecode.navigator.ksp.generator.LOCAL_NAV_HOST_CONTROLLER
import de.onecode.navigator.ksp.generator.NAVIGATOR_COMPOSABLE_NAME
import de.onecode.navigator.ksp.generator.composeAnnotation
import de.onecode.navigator.ksp.generator.composeModifier
import de.onecode.navigator.ksp.generator.navGraphBuilderClass
import de.onecode.navigator.ksp.generator.navHostControllerClass
import de.onecode.navigator.ksp.generator.rememberNavControllerName
import de.onecode.navigator.ksp.generator.screenBuilderClass
import de.onecode.navigator.ksp.getNameOfHome

internal fun createNavigatorComposable(destinations: List<DestinationDescription>): FunSpec {
	val modifier = ParameterSpec.builder("modifier", composeModifier)
		.defaultValue("%T", composeModifier)
		.build()

	val navHostControllerParam = ParameterSpec.builder("navHostController", navHostControllerClass)
		.defaultValue("%M()", rememberNavControllerName)
		.build()

	val screenBuilderLambda = LambdaTypeName.get(receiver = screenBuilderClass, returnType = UNIT, parameters = arrayOf(navGraphBuilderClass))
	val screenBuilderParam = ParameterSpec.builder("builder", screenBuilderLambda).build()

	val home = destinations.getNameOfHome()

	return FunSpec.builder(NAVIGATOR_COMPOSABLE_NAME)
		.addAnnotation(composeAnnotation)
		.addParameter(modifier)
		.addParameter(navHostControllerParam)
		.addParameter(screenBuilderParam)
		.addStatement("%L = compositionLocalOf { navHostController }", LOCAL_NAV_HOST_CONTROLLER)
		.beginControlFlow("CompositionLocalProvider(%L provides navHostController)", LOCAL_NAV_HOST_CONTROLLER)
		.beginControlFlow("NavHost(modifier = %N, startDestination = %S, navController = navHostController)", modifier, home)
		.addComposablesBody(destinations, screenBuilderClass)
		.endControlFlow()
		.endControlFlow()
		.build()
}
