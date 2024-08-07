package de.onecode.compass.ksp.generator.context

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import de.onecode.compass.ksp.descriptions.SubGraphDescription
import de.onecode.compass.ksp.generator.COMMON_CONTEXT
import de.onecode.compass.ksp.generator.PACKAGE
import de.onecode.compass.ksp.generator.navBackStackEntryClass
import de.onecode.compass.ksp.generator.navHostControllerClass
import de.onecode.compass.ksp.getHome
import de.onecode.compass.ksp.routeParameterSuffix
import javax.annotation.processing.Generated

fun createSubGraphContext(subGraph: SubGraphDescription): TypeSpec {
	val navControllerParam = "navHostController"
	val navBackStackEntryParamName = "navBackStackEntry"

	val destinations = subGraph.destinations
	val subGraphHome = destinations.getHome()

	return TypeSpec.classBuilder("${subGraph.name}$COMMON_CONTEXT")
		.addAnnotation(Generated::class)
		.superclass(ClassName(PACKAGE, COMMON_CONTEXT))
		.addSuperclassConstructorParameter(navControllerParam)
		.addSuperclassConstructorParameter(navBackStackEntryParamName)
		.addModifiers(KModifier.ABSTRACT)
		.primaryConstructor(
			FunSpec.constructorBuilder()
				.addParameter(navControllerParam, navHostControllerClass)
				.addParameter(navBackStackEntryParamName, navBackStackEntryClass)
				.build()
		)
		.addFunction(
			FunSpec.builder("leaveSubGraph")
				.addStatement(
					"%L.popBackStack(route = %S, inclusive = true)",
					navControllerParam,
					subGraph.name + subGraphHome.routeParameterSuffix
				)
				.build()
		)
		.build()
}
