package de.onecode.navigator.ksp.generator.navigation

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.UNIT
import de.onecode.navigator.ksp.decapitalize
import de.onecode.navigator.ksp.descriptions.SubGraphDescription
import de.onecode.navigator.ksp.generator.PACKAGE
import de.onecode.navigator.ksp.generator.navGraphBuilderClass
import de.onecode.navigator.ksp.getHome
import de.onecode.navigator.ksp.route
import de.onecode.navigator.ksp.routeParameterSuffix
import de.onecode.navigator.ksp.screenBuilderImplementationName
import de.onecode.navigator.ksp.screenBuilderInterfaceName

internal fun createSubGraphFunction(subGraph: SubGraphDescription): FunSpec {
	val subGraphScreenBuilderClass = ClassName(PACKAGE, subGraph.screenBuilderInterfaceName)
	val subGraphScreenBuilderImplementationClass = ClassName(PACKAGE, subGraph.screenBuilderImplementationName)

	val screenBuilderLambda = LambdaTypeName.get(receiver = subGraphScreenBuilderClass, returnType = UNIT, parameters = arrayOf(navGraphBuilderClass))
	val screenBuilderParam = ParameterSpec.builder("builder", screenBuilderLambda).build()

	val destinations = subGraph.destinations
	val subGraphHome = destinations.getHome()

	return FunSpec.builder("${subGraph.name.decapitalize()}SubGraph")
		.receiver(navGraphBuilderClass)
		.addParameter(screenBuilderParam)
		.beginControlFlow("navigation(startDestination = %S, route = %S)", subGraphHome.route, subGraph.name + subGraphHome.routeParameterSuffix)
		.addComposablesBody(destinations, subGraphScreenBuilderImplementationClass)
		.endControlFlow()
		.build()
}