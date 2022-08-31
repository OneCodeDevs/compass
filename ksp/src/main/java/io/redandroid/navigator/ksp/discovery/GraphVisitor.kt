package io.redandroid.navigator.ksp.discovery

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import io.redandroid.navigator.api.Destination
import io.redandroid.navigator.api.SubGraph
import io.redandroid.navigator.ksp.descriptions.DestinationDescription
import io.redandroid.navigator.ksp.descriptions.GraphDescription
import io.redandroid.navigator.ksp.descriptions.SubGraphDescription
import io.redandroid.navigator.ksp.asClassDeclaration
import io.redandroid.navigator.ksp.className
import io.redandroid.navigator.ksp.filterAnnotations
import io.redandroid.navigator.ksp.getDestinationName
import io.redandroid.navigator.ksp.getParameterValue
import io.redandroid.navigator.ksp.getSubGraphName

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
			parameters = emptyList(), // TODO support parameters,
			destinations = destinationsInSubGraph
		)
	}
}