package de.onecode.navigator.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSType
import de.onecode.navigator.api.Destination
import de.onecode.navigator.api.SubGraph
import de.onecode.navigator.ksp.descriptions.DestinationDescription
import de.onecode.navigator.ksp.descriptions.ParameterDescription
import de.onecode.navigator.ksp.descriptions.SubGraphDescription
import de.onecode.navigator.ksp.generator.SCREEN_BUILDER
import java.util.Locale
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

fun String.typeString(): String =
	substring(lastIndexOf(".") + 1)

fun String.type(): KClass<*> =
	when (this.typeString()) {
		"String" -> String::class
		"Byte"   -> Byte::class
		"Int"    -> Int::class
		"Long"   -> Long::class
		"Float"  -> Float::class
		"Double" -> Double::class
		else     -> error("Unknown type $this")
	}

val KSType.isNavigable: Boolean
	get() = declaration.annotations.any {
		val shortName = it.shortName.asString()
		shortName == Destination::class.simpleName ||
			shortName == SubGraph::class.simpleName
	}

fun KSType.asClassDeclaration(): KSClassDeclaration =
	declaration as? KSClassDeclaration
		?: error("${declaration.simpleName.asString()} has to be a class, an interface or an object")

fun KSAnnotation.toParameterDescription(classDeclaration: KSClassDeclaration): ParameterDescription {
	val paramName = getParameterValue<String>(ParameterDescription::name.name, classDeclaration)
	val paramType = getParameterValue<KSType>(ParameterDescription::type.name, classDeclaration)

	return ParameterDescription(
		name = paramName,
		type = paramType.declaration.qualifiedName?.asString()
			?: error("Can't get qualified name of parameter type")
	)
}

fun <T : Annotation> KSDeclaration.filterAnnotations(annotationClass: KClass<T>): Sequence<KSAnnotation> =
	annotations.filter { it.shortName.asString() == annotationClass.simpleName }

fun <T : Annotation> KSType.filterAnnotations(annotationClass: KClass<T>): Sequence<KSAnnotation> =
	declaration.filterAnnotations(annotationClass)

val KSClassDeclaration.className: String
	get() = simpleName.asString()

inline fun <reified T> KSAnnotation.getParameterValue(parameterName: String, classDeclaration: KSClassDeclaration): T {
	return arguments.firstOrNull { it.name?.asString() == parameterName }?.value as? T
		?: error("No parameter $parameterName was provided for ${shortName.asString()} on ${classDeclaration.className}")
}

fun KSAnnotation.getDesiredName(nameProperty: KProperty1<*, String>, classDeclaration: KSClassDeclaration): String {
	val annotatedDestinationName = getParameterValue<String>(nameProperty.name, classDeclaration)

	return annotatedDestinationName.ifEmpty {
		classDeclaration.className
	}
}

fun KSAnnotation.getDestinationName(classDeclaration: KSClassDeclaration): String =
	getDesiredName(Destination::name, classDeclaration)

fun KSAnnotation.getSubGraphName(classDeclaration: KSClassDeclaration): String =
	getDesiredName(SubGraph::name, classDeclaration)

fun String.capitalize(): String = replaceFirstChar { firstChar ->
	if (firstChar.isLowerCase()) {
		firstChar.titlecase(Locale.getDefault())
	} else {
		firstChar.toString()
	}
}

fun String.decapitalize(): String =
	replaceFirstChar { it.lowercase(Locale.getDefault()) }

val SubGraphDescription.screenBuilderInterfaceName: String
	get() = "$name$SCREEN_BUILDER"

val SubGraphDescription.screenBuilderImplementationName: String
	get() = "$name${SCREEN_BUILDER}Impl"

fun List<DestinationDescription>.getHome(): DestinationDescription =
	firstOrNull { it.isHome }
		?: error("Couldn't find a ${Destination::class.simpleName} marked as home")

fun List<DestinationDescription>.getNameOfHome(): String =
	getHome().name

val DestinationDescription.route: String
	get() = name + routeParameterSuffix

val DestinationDescription.routeParameterSuffix: String
	get() {
		val params = parameters.joinToString("/") { "{${it.name}}" }
		return if (params.isNotBlank()) "/$params" else ""
	}