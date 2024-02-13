package de.onecode.navigator.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.ksp.writeTo
import de.onecode.navigator.api.Destination
import de.onecode.navigator.api.SubGraph
import de.onecode.navigator.ksp.discovery.DestinationVisitor
import de.onecode.navigator.ksp.discovery.GraphVisitor
import de.onecode.navigator.ksp.generator.generateAddDestinationCode
import de.onecode.navigator.ksp.generator.generateNavigatorCode

class GraphSymbolProcessor(
	private val codeGenerator: CodeGenerator,
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
		val state = graph.checkGraphState()
		val fileSpec = when (state) {
			GraphState.Standard       -> generateNavigatorCode(graph)
			GraphState.NoHome         -> generateAddDestinationCode(graph)
			GraphState.NoDestinations -> null
		}

		fileSpec?.writeTo(codeGenerator = codeGenerator, Dependencies(false, *resolver.getAllFiles().toList().toTypedArray()))
		return emptyList()
	}
}
