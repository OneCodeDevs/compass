package io.redandroid.navigator.ksp.generator.navigation

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.UNIT
import io.redandroid.navigator.ksp.decapitalize
import io.redandroid.navigator.ksp.descriptions.SubGraphDescription
import io.redandroid.navigator.ksp.generator.PACKAGE
import io.redandroid.navigator.ksp.generator.navGraphBuilderClass
import io.redandroid.navigator.ksp.getNameOfHome
import io.redandroid.navigator.ksp.screenBuilderName

internal fun createSubGraphFunction(subGraph: SubGraphDescription): FunSpec {
	val subGraphScreenBuilderClass = ClassName(PACKAGE, subGraph.screenBuilderName)

	val screenBuilderLambda = LambdaTypeName.get(receiver = subGraphScreenBuilderClass, returnType = UNIT, parameters = arrayOf(navGraphBuilderClass))
	val screenBuilderParam = ParameterSpec.builder("builder", screenBuilderLambda).build()

	val destinations = subGraph.destinations
	val subGraphHome = destinations.getNameOfHome()

	return FunSpec.builder("${subGraph.name.decapitalize()}SubGraph")
		.receiver(navGraphBuilderClass)
		.addParameter(screenBuilderParam)
		.beginControlFlow("navigation(startDestination = %S, route = %S)", subGraphHome, subGraph.name)
		.addComposablesBody(destinations, subGraphScreenBuilderClass)
		.endControlFlow()
		.build()
}