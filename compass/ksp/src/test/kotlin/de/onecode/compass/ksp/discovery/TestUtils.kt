package de.onecode.compass.ksp.discovery

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSName
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueArgument
import de.onecode.compass.api.Destination
import de.onecode.compass.api.Home
import de.onecode.compass.api.Navigation
import de.onecode.compass.api.Parameter
import de.onecode.compass.api.Top
import io.mockk.every
import io.mockk.mockk
import kotlin.reflect.KClass

internal fun declareDestination(
	destinationVisitor: DestinationVisitor,
	name: String = "",
	isHome: Boolean = false,
	isTop: Boolean = false,
	typeName: String = "TestDestination",
	parameters: () -> List<KSAnnotation> = { emptyList() },
	navigationTargets: () -> List<KSAnnotation> = { emptyList() },
): KSClassDeclaration {
	val annotationsOnDestination = createAnnotationsForDestination(name, isHome, isTop, parameters, navigationTargets)

	return mockk<KSClassDeclaration> {
		every { simpleName } returns ksName(typeName)
		every { annotations } returns annotationsOnDestination.asSequence()
		every { accept(any<DestinationVisitor>(), Unit) } answers {
			destinationVisitor.visitClassDeclaration(this@mockk, Unit)
		}
	}
}

internal fun createAnnotationsForDestination(
	name: String = "",
	isHome: Boolean = false,
	isTop: Boolean = false,
	parameters: () -> List<KSAnnotation> = { emptyList() },
	navigationTargets: () -> List<KSAnnotation> = { emptyList() },
): List<KSAnnotation> {
	val destination = createDestination(name)

	val home = isHome
		.ifTrue { createAnnotation(Home::class) }

	val top = isTop
		.ifTrue { createAnnotation(Top::class) }

	return listOfNotNull(destination, home, top, *parameters().toTypedArray(), *navigationTargets().toTypedArray())
}

internal fun createDestination(destinationName: String? = null): KSAnnotation = createAnnotation(Destination::class) {
	destinationName?.let {
		listOf(ksValueArgument("name", it))
	}
		?: emptyList()
}

internal fun Any.createNavigationTarget(annotations: () -> List<KSAnnotation>): KSAnnotation = createAnnotation(Navigation::class) {
	listOf(
		ksValueArgument("to", ksType(this::class, annotations()))
	)
}

internal fun createParameter(parameterName: String, parameterType: KClass<*>): KSAnnotation = createAnnotation(Parameter::class) {
	listOf(
		ksValueArgument("name", parameterName),
		ksValueArgument("type", ksType(parameterType))
	)
}

private fun createAnnotation(kClass: KClass<*>, block: () -> List<KSValueArgument> = { emptyList() }): KSAnnotation =
	mockk<KSAnnotation> {
		every { shortName } returns ksName(kClass.java.simpleName)
		every { arguments } returns block()
	}

private fun ksValueArgument(valueName: String, valueContent: Any): KSValueArgument = mockk<KSValueArgument> {
	every { name } returns ksName(valueName)
	every { value } returns valueContent
}

private fun ksType(kClass: KClass<*>, typeAnnotations: List<KSAnnotation> = emptyList()): KSType = mockk {
	every { declaration } returns mockk<KSClassDeclaration> {
		every { qualifiedName } returns ksName(kClass.java.canonicalName)
		every { simpleName } returns ksName(kClass.java.simpleName)
		every { annotations } returns typeAnnotations.asSequence()
	}
}

private fun ksName(name: String): KSName = mockk {
	every { asString() } returns name
}

private fun <T> Boolean.ifTrue(block: () -> T?) =
	if (this) {
		block()
	} else {
		null
	}