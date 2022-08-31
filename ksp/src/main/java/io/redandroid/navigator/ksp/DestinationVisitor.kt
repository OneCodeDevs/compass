package io.redandroid.navigator.ksp

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import io.redandroid.navigator.api.Destination
import io.redandroid.navigator.api.Home
import io.redandroid.navigator.api.Navigation
import io.redandroid.navigator.api.Parameter
import io.redandroid.navigator.api.SubGraph

class DestinationVisitor : KSVisitorVoid() {

	private val _destinations = mutableListOf<DestinationDescription>()
	val destinations: List<DestinationDescription>
		get() = _destinations.toList()

	override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
		val destination = classDeclaration.filterAnnotations(Destination::class).firstOrNull()
			?: error("Could not find annotation ${Destination::name} on ${classDeclaration.className}")
		val isHome = classDeclaration.filterAnnotations(Home::class).iterator().hasNext()
		val destinationName = destination.getDestinationName(classDeclaration)

		val navTargets = classDeclaration.filterAnnotations(Navigation::class)
			.map { navigationAnnotation ->
				val target = navigationAnnotation.getParameterValue<KSType>(Navigation::to.name, classDeclaration)
				if (!target.isNavigable) {
					error("Navigation target $target is not navigable. It has to be annotated with ${Destination::class.qualifiedName} or ${SubGraph::class.qualifiedName}")
				}
				val targetClassDeclaration = target.asClassDeclaration()
				val targetParameters = target.filterAnnotations(Parameter::class)
				val targetName = target.filterAnnotations(Destination::class).firstOrNull()?.getDestinationName(targetClassDeclaration)
					?: target.filterAnnotations(SubGraph::class).firstOrNull()?.getSubGraphName(targetClassDeclaration)
					?: error("Navigation target has to be annotated with ${Destination::name}")

				NavigationTarget(
					name = targetName,
					parameters = targetParameters.map { it.toParameterDescription(classDeclaration) }.toList()
				)
			}

		val parameters = classDeclaration.filterAnnotations(Parameter::class)
			.map { parameterAnnotation ->
				parameterAnnotation.toParameterDescription(classDeclaration)
			}

		_destinations += DestinationDescription(
			name = destinationName,
			parameters = parameters.toList(),
			navigationTargets = navTargets.toList(),
			isHome = isHome
		)
	}
}