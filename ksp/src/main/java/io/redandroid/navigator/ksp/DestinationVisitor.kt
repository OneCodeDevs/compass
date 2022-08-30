package io.redandroid.navigator.ksp

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

		val isHome = classAnnotations.filterAnnotation(Home::class).isNotEmpty()

		val navTargets = classAnnotations.filterAnnotation(Navigation::class)
			.map { navigationAnnotation ->
				val target = navigationAnnotation.getParameterValue<KSType>(Navigation::to.name, classDeclaration)
				if (!target.isDestination) {
					error("Navigation target $target is not annotated with ${Destination::class.qualifiedName}")
				}

				val targetParameters = target.declaration.annotations.filterAnnotation(Parameter::class)

				NavigationTarget(
					name = target.declaration.simpleName.asString(),
					parameters = targetParameters.map { it.toParameterDescription(classDeclaration) }.toList()
				)
			}

		val parameters = classAnnotations.filterAnnotation(Parameter::class)
			.map { parameterAnnotation ->
				parameterAnnotation.toParameterDescription(classDeclaration)
			}

		_destinations += DestinationDescription(
			name = classDeclaration.className,
			parameters = parameters,
			navigationTargets = navTargets,
			isHome = isHome
		)
	}
}