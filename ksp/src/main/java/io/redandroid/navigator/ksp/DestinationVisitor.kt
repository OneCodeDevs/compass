package io.redandroid.navigator.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import io.redandroid.navigator.api.Destination
import io.redandroid.navigator.api.Home
import io.redandroid.navigator.api.Navigation
import io.redandroid.navigator.api.Parameter

class DestinationVisitor : KSVisitorVoid() {

	private val _destinations = mutableListOf<DestinationDescription>()
	val destinations: List<DestinationDescription>
		get() = _destinations.toList()

	override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
		val classAnnotations = classDeclaration.annotations.toList()

		val destination = classAnnotations.filterAnnotation(Destination::class).firstOrNull()
			?: error("Could not find annotation ${Destination::name} on ${classDeclaration.className}")
		val isHome = classAnnotations.filterAnnotation(Home::class).isNotEmpty()
		val destinationName = destination.getDestinationName(classDeclaration)

		val navTargets = classAnnotations.filterAnnotation(Navigation::class)
			.map { navigationAnnotation ->
				val target = navigationAnnotation.getParameterValue<KSType>(Navigation::to.name, classDeclaration)
				if (!target.isDestination) {
					error("Navigation target $target is not annotated with ${Destination::class.qualifiedName}")
				}
				val targetClassDeclaration = target.declaration as? KSClassDeclaration
					?: error("Navigation target has to be a class, an interface or an object")

				val targetParameters = target.declaration.annotations.filterAnnotation(Parameter::class)

				val targetDestination = target.declaration.annotations.filterAnnotation(Destination::class).firstOrNull()
					?: error("Navigation target has to be annotated with ${Destination::name}")

				NavigationTarget(
					name = targetDestination.getDestinationName(targetClassDeclaration),
					parameters = targetParameters.map { it.toParameterDescription(classDeclaration) }.toList()
				)
			}

		val parameters = classAnnotations.filterAnnotation(Parameter::class)
			.map { parameterAnnotation ->
				parameterAnnotation.toParameterDescription(classDeclaration)
			}

		_destinations += DestinationDescription(
			name = destinationName,
			parameters = parameters,
			navigationTargets = navTargets,
			isHome = isHome
		)
	}

	private fun KSAnnotation.getDestinationName(classDeclaration: KSClassDeclaration): String {
		val annotatedDestinationName = getParameterValue<String>(Destination::name.name, classDeclaration)

		return annotatedDestinationName.ifEmpty {
			classDeclaration.className
		}
	}
}