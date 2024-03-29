package de.onecode.compass.ksp.generator.screenbuilder

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.generator.screenBuilderClass
import de.onecode.compass.ksp.generator.screenBuilderImplClass
import javax.annotation.processing.Generated

internal fun createScreenBuilderInterface(destinations: List<DestinationDescription>): TypeSpec =
	TypeSpec.interfaceBuilder(screenBuilderClass)
		.addAnnotation(Generated::class)
		.addAbstractDestinationFunctions(destinations)
		.build()

internal fun createScreenBuilderImplementation(destinations: List<DestinationDescription>): TypeSpec =
	TypeSpec.classBuilder(screenBuilderImplClass)
		.addAnnotation(Generated::class)
		.addModifiers(KModifier.PRIVATE)
		.addSuperinterface(screenBuilderClass)
		.addDestinationPropertiesAndFunctions(destinations)
		.build()
