@file:Suppress("TooManyFunctions")

package de.onecode.compass.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName
import de.onecode.compass.api.Destination
import de.onecode.compass.api.SubGraph
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.ParameterDescription
import de.onecode.compass.ksp.descriptions.SubGraphDescription
import de.onecode.compass.ksp.generator.SCREEN_BUILDER
import java.util.Locale
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

fun String.typeString(): String =
	substring(lastIndexOf(".") + 1)

fun String.getNavTypeName(): String =
	if (this == "Boolean") {
		"Bool"
	} else {
		this
	}

fun String.type(): ClassName =
	when (this.typeString()) {
		"String"  -> String::class
		"Byte"    -> Byte::class
		"Int"     -> Int::class
		"Long"    -> Long::class
		"Float"   -> Float::class
		"Double"  -> Double::class
		"Boolean" -> Boolean::class
		else      -> error("Type $this is currently not supported")
	}.asTypeName()

fun ClassName.optionalAllowed(): Boolean =
	when (this) {
		String::class.asTypeName() -> true
		else                       -> false
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
	val paramRequired = getParameterValue<Boolean>(ParameterDescription::required.name, classDeclaration)

	return ParameterDescription(
		name = paramName,
		type = paramType.declaration.qualifiedName?.asString()
			?: error("Can't get qualified name of parameter type"),
		required = paramRequired
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
		val (required, optional) = parameters.partition { it.required }

		val requiredParamPath = if (required.isNotEmpty()) {
			required.joinToString(prefix = "/", separator = "/") { "{${it.name}}" }
		} else {
			""
		}
		val optionalParamQuery = if (optional.isNotEmpty()) {
			optional.joinToString(prefix = "?", separator = "&") { "${it.name}={${it.name}}" }
		} else {
			""
		}


		return requiredParamPath + optionalParamQuery
	}
