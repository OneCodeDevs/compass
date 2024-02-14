package de.onecode.compass.ksp.discovery

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import de.onecode.compass.api.Destination
import de.onecode.compass.api.SubGraph
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.GraphDescription
import de.onecode.compass.ksp.descriptions.SubGraphDescription
import de.onecode.compass.ksp.asClassDeclaration
import de.onecode.compass.ksp.className
import de.onecode.compass.ksp.filterAnnotations
import de.onecode.compass.ksp.getDestinationName
import de.onecode.compass.ksp.getParameterValue
import de.onecode.compass.ksp.getSubGraphName

class GraphVisitor(private val destinationDescriptions: List<DestinationDescription>) : KSVisitorVoid() {
	private val subGraphs = mutableListOf<SubGraphDescription>()
	val graph: GraphDescription
		get() =
			GraphDescription(
				destinations = destinationDescriptions - subGraphs.flatMap { it.destinations }.toSet(),
				subGraphs = subGraphs
			)

	override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
		val subGraphAnnotation = classDeclaration.filterAnnotations(SubGraph::class).firstOrNull() ?: error("Couldn't find ${SubGraph::class.simpleName} on ${classDeclaration.className}")
		val classesInSubGraph = subGraphAnnotation.getParameterValue<List<KSType>>(SubGraph::destinations.name, classDeclaration)
		val subGraphName = subGraphAnnotation.getSubGraphName(classDeclaration)

		val destinationsInSubGraph = classesInSubGraph.map { classInSubGraph ->
			val declaration = classInSubGraph.asClassDeclaration()
			val destination = declaration.filterAnnotations(Destination::class).firstOrNull()
				?: error("${declaration.simpleName.asString()} in a SubGraph has to be annotated with ${Destination::class.simpleName}")

			val destinationName = destination.getDestinationName(declaration)
			destinationDescriptions.firstOrNull { it.name == destinationName }
				?: error("Destination $destinationName not found")
		}

		subGraphs += SubGraphDescription(
			name = subGraphName,
			destinations = destinationsInSubGraph
		)
	}
}