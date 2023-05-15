package de.onecode.navigator.ksp.generator.context

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import de.onecode.navigator.ksp.generator.COMMON_CONTEXT
import de.onecode.navigator.ksp.generator.navHostControllerClass

internal fun createCommonContext(): TypeSpec {
	val navControllerParam = "navHostController"

	return TypeSpec.classBuilder(COMMON_CONTEXT)
		.addModifiers(KModifier.ABSTRACT)
		.primaryConstructor(
			FunSpec.constructorBuilder()
				.addParameter(navControllerParam, navHostControllerClass)
				.build()
		)
		.addProperty(PropertySpec.builder(navControllerParam, navHostControllerClass, KModifier.PRIVATE).initializer(navControllerParam).build())
		.addFunction(
			FunSpec.builder("popBackStack")
				.addStatement("%L.popBackStack()", navControllerParam)
				.build()
		)
		.build()
}