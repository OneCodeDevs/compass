package de.onecode.compass.ksp.generator.screenbuilder

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import de.onecode.compass.ksp.descriptions.SubGraphDescription
import de.onecode.compass.ksp.generator.PACKAGE
import de.onecode.compass.ksp.screenBuilderImplementationName
import de.onecode.compass.ksp.screenBuilderInterfaceName
import javax.annotation.processing.Generated

internal fun createSubGraphBuilderInterface(subGraph: SubGraphDescription): TypeSpec =
	TypeSpec.interfaceBuilder(subGraph.screenBuilderInterfaceName)
		.addAnnotation(Generated::class)
		.addAbstractDestinationFunctions(subGraph.destinations)
		.build()

internal fun createSubGraphBuilderImplementation(subGraph: SubGraphDescription): TypeSpec =
	TypeSpec.classBuilder(subGraph.screenBuilderImplementationName)
		.addAnnotation(Generated::class)
		.addModifiers(KModifier.PRIVATE)
		.addSuperinterface(ClassName(PACKAGE, subGraph.screenBuilderInterfaceName))
		.addDestinationPropertiesAndFunctions(subGraph.destinations)
		.build()