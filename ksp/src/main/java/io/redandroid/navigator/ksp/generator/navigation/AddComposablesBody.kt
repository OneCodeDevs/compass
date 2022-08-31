package io.redandroid.navigator.ksp.generator.navigation

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import io.redandroid.navigator.ksp.decapitalize
import io.redandroid.navigator.ksp.descriptions.DestinationDescription
import io.redandroid.navigator.ksp.generator.NAV_HOST_CONTROLLER_LOCAL
import io.redandroid.navigator.ksp.generator.contextName
import io.redandroid.navigator.ksp.route

internal fun FunSpec.Builder.addComposablesBody(destinations: List<DestinationDescription>, screenBuilderClass: ClassName): FunSpec.Builder {
	addStatement("val screenBuilder = %L()", screenBuilderClass.simpleName)
	addStatement("screenBuilder.builder(this)")
	destinations.forEach { destination ->
		addCode(destination.toNavigationComposableCodeBlock())
	}
	return this
}

private fun DestinationDescription.toNavigationComposableCodeBlock(): CodeBlock {
	val destinationScreenName = name.decapitalize()

	return CodeBlock.builder()
		.beginControlFlow("composable(route = %S)", route)
		.addStatement("screenBuilder.%LComposable?.invoke(%L(%L.current, it))", destinationScreenName, contextName, NAV_HOST_CONTROLLER_LOCAL)
		.endControlFlow()
		.build()
}