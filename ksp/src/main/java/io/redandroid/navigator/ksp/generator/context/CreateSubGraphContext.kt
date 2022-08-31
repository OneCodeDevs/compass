package io.redandroid.navigator.ksp.generator.context

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import io.redandroid.navigator.ksp.descriptions.SubGraphDescription
import io.redandroid.navigator.ksp.generator.COMMON_CONTEXT
import io.redandroid.navigator.ksp.generator.PACKAGE
import io.redandroid.navigator.ksp.generator.navHostControllerClass

fun createSubGraphContext(subGraphDescription: SubGraphDescription): TypeSpec {
	val navControllerParam = "navHostController"
	return TypeSpec.classBuilder("${subGraphDescription.name}$COMMON_CONTEXT")
		.superclass(ClassName(PACKAGE, COMMON_CONTEXT))
		.addSuperclassConstructorParameter(navControllerParam)
		.addModifiers(KModifier.ABSTRACT)
		.primaryConstructor(
			FunSpec.constructorBuilder()
				.addParameter(navControllerParam, navHostControllerClass)
				.build()
		)
		.addProperty(PropertySpec.builder(navControllerParam, navHostControllerClass, KModifier.PRIVATE).initializer(navControllerParam).build())
		.addFunction(
			FunSpec.builder("leaveSubGraph")
				.addStatement("%L.popBackStack(route = %S, inclusive = true)", navControllerParam, subGraphDescription.name)
				.build()
		)
		.build()
}