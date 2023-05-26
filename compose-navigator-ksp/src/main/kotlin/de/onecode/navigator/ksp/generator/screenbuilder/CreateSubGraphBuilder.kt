package de.onecode.navigator.ksp.generator.screenbuilder

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec
import de.onecode.navigator.ksp.descriptions.SubGraphDescription
import de.onecode.navigator.ksp.generator.PACKAGE
import de.onecode.navigator.ksp.screenBuilderImplementationName
import de.onecode.navigator.ksp.screenBuilderInterfaceName

internal fun createSubGraphBuilderInterface(subGraph: SubGraphDescription): TypeSpec =
	TypeSpec.interfaceBuilder(subGraph.screenBuilderInterfaceName)
		.addAbstractDestinationFunctions(subGraph.destinations)
		.build()

internal fun createSubGraphBuilderImplementation(subGraph: SubGraphDescription): TypeSpec =
	TypeSpec.classBuilder(subGraph.screenBuilderImplementationName)
		.addSuperinterface(ClassName(PACKAGE, subGraph.screenBuilderInterfaceName))
		.addDestinationPropertiesAndFunctions(subGraph.destinations)
		.build()