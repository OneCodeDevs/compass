package de.onecode.navigator.ksp.generator.screenbuilder

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import de.onecode.navigator.ksp.descriptions.DestinationDescription
import de.onecode.navigator.ksp.generator.screenBuilderClass
import de.onecode.navigator.ksp.generator.screenBuilderImplClass

internal fun createScreenBuilderInterface(destinations: List<DestinationDescription>): TypeSpec =
	TypeSpec.interfaceBuilder(screenBuilderClass)
		.addAbstractDestinationFunctions(destinations)
		.build()

internal fun createScreenBuilderImplementation(destinations: List<DestinationDescription>): TypeSpec =
	TypeSpec.classBuilder(screenBuilderImplClass)
		.addModifiers(KModifier.PRIVATE)
		.addSuperinterface(screenBuilderClass)
		.addDestinationPropertiesAndFunctions(destinations)
		.build()