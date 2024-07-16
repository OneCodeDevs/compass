package de.onecode.compass.ksp.discovery

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ksp.toClassName
import de.onecode.compass.api.Destination
import de.onecode.compass.api.Dialog
import de.onecode.compass.api.Home
import de.onecode.compass.api.Navigation
import de.onecode.compass.api.Parameter
import de.onecode.compass.api.SubGraph
import de.onecode.compass.api.Top
import de.onecode.compass.ksp.asClassDeclaration
import de.onecode.compass.ksp.className
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.NavigationTarget
import de.onecode.compass.ksp.filterAnnotations
import de.onecode.compass.ksp.getDestinationName
import de.onecode.compass.ksp.getParameterValue
import de.onecode.compass.ksp.getSubGraphName
import de.onecode.compass.ksp.isNavigable
import de.onecode.compass.ksp.toParameterDescription

class DestinationVisitor : KSVisitorVoid() {

	private val _destinations = mutableListOf<DestinationDescription>()
	val destinations: List<DestinationDescription>
		get() = _destinations.toList()

	override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
		val destination = classDeclaration.filterAnnotations(Destination::class).firstOrNull()
			?: error("Could not find annotation ${Destination::name} on ${classDeclaration.className}")
		val isHome = classDeclaration.filterAnnotations(Home::class).iterator().hasNext()
		val isTop = classDeclaration.filterAnnotations(Top::class).iterator().hasNext()
		val isDialog = classDeclaration.filterAnnotations(Dialog::class).iterator().hasNext()
		val destinationName = destination.getDestinationName(classDeclaration)

		if (isDialog && isTop) {
			error("Destination $destinationName is marked as ${Dialog::class.qualifiedName} and ${Top::class.qualifiedName}. A Dialog can't also be Top")
		}

		if (isDialog && isHome) {
			error("Destination $destinationName is marked as ${Dialog::class.qualifiedName} and ${Home::class.qualifiedName}. A Dialog can't also be Home")
		}

		val navTargets = classDeclaration.filterAnnotations(Navigation::class)
			.map { navigationAnnotation ->
				val target = navigationAnnotation.getParameterValue<KSType>(Navigation::to.name, classDeclaration)
				if (!target.isNavigable) {
					error("Navigation target $target is not navigable. It has to be annotated with ${Destination::class.qualifiedName} or ${SubGraph::class.qualifiedName}")
				}

				val targetClassDeclaration = target.asClassDeclaration()
				if (target.isDestination()) {
					val targetName = target.getDestinationName()
					val targetParameters = target.filterAnnotations(Parameter::class)
					NavigationTarget(
						name = targetName,
						parameters = targetParameters.map { it.toParameterDescription(classDeclaration) }.toList()
					)
				} else if (target.isSubGraph()) {
					val targetName = target.getSubGraphName()
					val subGraphDescriptionTypes =
						targetClassDeclaration.filterAnnotations(SubGraph::class).firstOrNull()?.getParameterValue<List<KSType>>(SubGraph::destinations.name, targetClassDeclaration)
					val subGraphHome = subGraphDescriptionTypes?.firstOrNull { it.asClassDeclaration().filterAnnotations(Home::class).iterator().hasNext() }
					val targetParameters = subGraphHome?.asClassDeclaration()?.filterAnnotations(Parameter::class)

					NavigationTarget(
						name = targetName,
						parameters = targetParameters?.map { it.toParameterDescription(classDeclaration) }?.toList()
							?: emptyList()
					)
				} else {
					error("Navigation target is neither a ${Destination::class.qualifiedName} nor a ${SubGraph::class.qualifiedName}")
				}
			}

		val parameters = classDeclaration.filterAnnotations(Parameter::class)
			.map { parameterAnnotation ->
				parameterAnnotation.toParameterDescription(classDeclaration)
			}

		_destinations += DestinationDescription(
			name = destinationName,
			parameters = parameters.toList(),
			navigationTargets = navTargets.toList(),
			isHome = isHome,
			isTop = isTop,
			isDialog = isDialog,
		)
	}

	private fun KSType.isDestination(): Boolean =
		asClassDeclaration().filterAnnotations(Destination::class).iterator().hasNext()

	private fun KSType.isSubGraph(): Boolean =
		asClassDeclaration().filterAnnotations(SubGraph::class).iterator().hasNext()

	private fun KSType.getDestinationName(): String =
		filterAnnotations(Destination::class).firstOrNull()?.getDestinationName(asClassDeclaration())
			?: error("Can't get name for destination of type ${toClassName().simpleName}")

	private fun KSType.getSubGraphName(): String =
		filterAnnotations(SubGraph::class).firstOrNull()?.getSubGraphName(asClassDeclaration())
			?: error("Can't get name for sub graph of type ${toClassName().simpleName}")
}
