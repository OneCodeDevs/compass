package io.redandroid.navigator.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import io.redandroid.navigator.api.Destination
import java.io.OutputStream
import kotlin.reflect.KClass

operator fun OutputStream.plusAssign(str: String) {
	write("$str\n".toByteArray())
}

fun String.typeString(): String =
	substring(lastIndexOf(".") + 1)

val KSType.isDestination: Boolean
	get() = declaration.annotations.any { it.shortName.asString() == Destination::class.simpleName }

fun KSAnnotation.toParameterDescription(classDeclaration: KSClassDeclaration): ParameterDescription {
	val paramName = getParameterValue<String>(ParameterDescription::name.name, classDeclaration)
	val paramType = getParameterValue<KSType>(ParameterDescription::type.name, classDeclaration)

	return ParameterDescription(
		name = paramName,
		type = paramType.declaration.qualifiedName?.asString() ?: error("Can't get qualified name of parameter type")
	)
}

fun <T : Annotation> List<KSAnnotation>.filterAnnotation(annotationClass: KClass<T>): List<KSAnnotation> =
	filter { it.shortName.asString() == annotationClass.simpleName }

fun <T : Annotation> Sequence<KSAnnotation>.filterAnnotation(annotationClass: KClass<T>): Sequence<KSAnnotation> =
	filter { it.shortName.asString() == annotationClass.simpleName }

val KSClassDeclaration.className: String
	get() = simpleName.asString()

inline fun <reified T> KSAnnotation.getParameterValue(parameterName: String, classDeclaration: KSClassDeclaration): T {
	return arguments.firstOrNull { it.name?.asString() == parameterName }?.value as? T
		?: error("No parameter $parameterName was provided for ${shortName.asString()} on ${classDeclaration.className}")
}