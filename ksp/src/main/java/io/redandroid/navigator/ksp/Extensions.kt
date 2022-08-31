package io.redandroid.navigator.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSType
import io.redandroid.navigator.api.Destination
import io.redandroid.navigator.api.SubGraph
import java.io.OutputStream
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

operator fun OutputStream.plusAssign(str: String) {
	write("$str\n".toByteArray())
}

fun String.typeString(): String =
	substring(lastIndexOf(".") + 1)

val KSType.isNavigable: Boolean
	get() = declaration.annotations.any {
		val shortName = it.shortName.asString()
		shortName == Destination::class.simpleName ||
			shortName == SubGraph::class.simpleName
	}

fun KSType.asClassDeclaration(): KSClassDeclaration =
	declaration as? KSClassDeclaration ?: error("${declaration.simpleName.asString()} has to be a class, an interface or an object")

fun KSAnnotation.toParameterDescription(classDeclaration: KSClassDeclaration): ParameterDescription {
	val paramName = getParameterValue<String>(ParameterDescription::name.name, classDeclaration)
	val paramType = getParameterValue<KSType>(ParameterDescription::type.name, classDeclaration)

	return ParameterDescription(
		name = paramName,
		type = paramType.declaration.qualifiedName?.asString() ?: error("Can't get qualified name of parameter type")
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