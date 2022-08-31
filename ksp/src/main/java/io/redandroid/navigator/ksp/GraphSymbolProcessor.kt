package io.redandroid.navigator.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import io.redandroid.navigator.api.Destination
import io.redandroid.navigator.api.SubGraph

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
		val home = destinations.filter { it.isHome }

		val homeAmount = home.size
		if (homeAmount > 1) {
			val homeNames = home.joinToString { it.name }
			error("Only one ${Destination::class.simpleName} is allowed to be marked as home. Found ${home.size}: $homeNames")
		} else if (homeAmount == 0) {
			error("No ${Destination::class.simpleName} was marked as home")
		}

		val subGraphSymbols = resolver.getSymbolsWithAnnotation(SubGraph::class.java.canonicalName)
		val graphVisitor = GraphVisitor(destinations)
		subGraphSymbols.forEach {
			it.accept(graphVisitor, Unit)
		}

		codeGenerator.generateCode(
			graph = graphVisitor.graph,
			dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray())
		)

		return emptyList()
	}
}