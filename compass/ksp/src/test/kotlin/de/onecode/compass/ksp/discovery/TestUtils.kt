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
import de.onecode.compass.api.SubGraph
import de.onecode.compass.api.Top
import de.onecode.compass.ksp.descriptions.DestinationDescription
import io.mockk.every
import io.mockk.mockk
import kotlin.reflect.KClass

@Suppress("LongParameterList")
internal fun declareDestination(
	destinationVisitor: DestinationVisitor? = null,
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
			destinationVisitor?.visitClassDeclaration(this@mockk, Unit)
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

internal fun createParameter(parameterName: String, parameterType: KClass<*>, required: Boolean): KSAnnotation = createAnnotation(Parameter::class) {
	listOf(
		ksValueArgument("name", parameterName),
		ksValueArgument("type", ksType(parameterType)),
		ksValueArgument("required", required)
	)
}

internal fun Any.createSubGraphType(graphVisitor: GraphVisitor, name: String, destinations: List<DestinationDescription> = emptyList()): KSClassDeclaration {
	val subGraphAnnotation = listOf(
		createAnnotation(SubGraph::class) {
			listOf(
				ksValueArgument("name", name),
				ksValueArgument("destinations", destinations.toType()),
			)
		}
	)

	val clazz = this::class.java

	return mockk<KSClassDeclaration> {
		every { qualifiedName } returns ksName(clazz.canonicalName)
		every { simpleName } returns ksName(clazz.simpleName)
		every { annotations } returns subGraphAnnotation.asSequence()
		every { accept(any<DestinationVisitor>(), Unit) } answers {
			graphVisitor.visitClassDeclaration(this@mockk, Unit)
		}
	}
}

private fun List<DestinationDescription>.toType(): List<KSType> =
	map { destination ->
		mockk<KSType> {
			val annotationsForDestination = createAnnotationsForDestination(
				name = destination.name,
				isHome = destination.isHome,
				isTop = destination.isHome,
				parameters = {
					destination.parameters.map { parameter ->
						createParameter(parameter.name, javaClass.classLoader.loadClass(parameter.type).kotlin, parameter.required)
					}
				}
			).asSequence()
			every { annotations } returns annotationsForDestination
			every { declaration } returns mockk<KSClassDeclaration> {
				every { qualifiedName } returns ksName(destination.name)
				every { simpleName } returns ksName(destination.name)
				every { annotations } returns annotationsForDestination
			}
		}
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
