package de.onecode.compass.ksp.generator.navigation

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.UNIT
import de.onecode.compass.ksp.decapitalize
import de.onecode.compass.ksp.descriptions.SubGraphDescription
import de.onecode.compass.ksp.generator.PACKAGE
import de.onecode.compass.ksp.generator.navGraphBuilderClass
import de.onecode.compass.ksp.getHome
import de.onecode.compass.ksp.route
import de.onecode.compass.ksp.routeParameterSuffix
import de.onecode.compass.ksp.screenBuilderImplementationName
import de.onecode.compass.ksp.screenBuilderInterfaceName
import javax.annotation.processing.Generated

internal fun createSubGraphFunction(subGraph: SubGraphDescription): FunSpec {
	val subGraphScreenBuilderClass = ClassName(PACKAGE, subGraph.screenBuilderInterfaceName)
	val subGraphScreenBuilderImplementationClass = ClassName(PACKAGE, subGraph.screenBuilderImplementationName)

	val screenBuilderLambda = LambdaTypeName.get(receiver = subGraphScreenBuilderClass, returnType = UNIT, parameters = arrayOf(navGraphBuilderClass))
	val screenBuilderParam = ParameterSpec.builder("builder", screenBuilderLambda).build()

	val destinations = subGraph.destinations
	val subGraphHome = destinations.getHome()

	return FunSpec.builder("${subGraph.name.decapitalize()}SubGraph")
		.addAnnotation(Generated::class)
		.receiver(navGraphBuilderClass)
		.addParameter(screenBuilderParam)
		.beginControlFlow("navigation(startDestination = %S, route = %S)", subGraphHome.route, subGraph.name + subGraphHome.routeParameterSuffix)
		.addComposablesBody(destinations, subGraphScreenBuilderImplementationClass)
		.endControlFlow()
		.build()
}