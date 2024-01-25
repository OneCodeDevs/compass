package de.onecode.navigator.ksp.generator.context

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import de.onecode.navigator.ksp.descriptions.SubGraphDescription
import de.onecode.navigator.ksp.generator.COMMON_CONTEXT
import de.onecode.navigator.ksp.generator.PACKAGE
import de.onecode.navigator.ksp.generator.navHostControllerClass
import de.onecode.navigator.ksp.getHome
import de.onecode.navigator.ksp.routeParameterSuffix
import javax.annotation.processing.Generated

fun createSubGraphContext(subGraph: SubGraphDescription): TypeSpec {
	val navControllerParam = "navHostController"

	val destinations = subGraph.destinations
	val subGraphHome = destinations.getHome()

	return TypeSpec.classBuilder("${subGraph.name}$COMMON_CONTEXT")
		.addAnnotation(Generated::class)
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
				.addStatement("%L.popBackStack(route = %S, inclusive = true)", navControllerParam, subGraph.name + subGraphHome.routeParameterSuffix)
				.build()
		)
		.build()
}