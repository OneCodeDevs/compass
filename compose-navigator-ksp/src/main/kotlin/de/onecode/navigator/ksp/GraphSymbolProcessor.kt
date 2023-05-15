package de.onecode.navigator.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import de.onecode.navigator.api.Destination
import de.onecode.navigator.api.SubGraph
import de.onecode.navigator.ksp.descriptions.DestinationDescription
import de.onecode.navigator.ksp.discovery.DestinationVisitor
import de.onecode.navigator.ksp.discovery.GraphVisitor
import de.onecode.navigator.ksp.generator.generateCode

class GraphSymbolProcessor(
	private val codeGenerator: CodeGenerator
) : SymbolProcessor {
	override fun process(resolver: Resolver): List<KSAnnotated> {
		val destinationSymbols = resolver.getSymbolsWithAnnotation(Destination::class.java.canonicalName)

		if (!destinationSymbols.iterator().hasNext()) {
			return emptyList()
		}

		val destinationVisitor = DestinationVisitor()
		destinationSymbols.forEach {
			it.accept(destinationVisitor, Unit)
		}

		val destinations = destinationVisitor.destinations

		val subGraphSymbols = resolver.getSymbolsWithAnnotation(SubGraph::class.java.canonicalName)
		val graphVisitor = GraphVisitor(destinations)
		subGraphSymbols.forEach {
			it.accept(graphVisitor, Unit)
		}

		val graph = graphVisitor.graph
		graph.destinations.assertOneHome()
		graph.subGraphs.forEach {
			it.destinations.assertOneHome()
		}

		codeGenerator.generateCode(
			graph = graph,
			dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray())
		)

		return emptyList()
	}

	private fun List<DestinationDescription>.assertOneHome() {
		val home = filter { it.isHome }
		val homeAmount = home.size
		if (homeAmount > 1) {
			val homeNames = home.joinToString { it.name }
			error("Only one ${Destination::class.simpleName} is allowed to be marked as home within a graph or sub graph. Found ${home.size}: $homeNames")
		} else if (homeAmount == 0 && isNotEmpty()) {
			error("No ${Destination::class.simpleName} was marked as home")
		}
	}
}