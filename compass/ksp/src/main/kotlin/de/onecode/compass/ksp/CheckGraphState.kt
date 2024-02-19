package de.onecode.compass.ksp

import de.onecode.compass.api.Destination
import de.onecode.compass.api.Top
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.GraphDescription

internal fun GraphDescription.checkGraphState(): GraphState {
	if (destinations.isEmpty() && subGraphs.isEmpty()) {
		return GraphState.NoDestinations
	}

	subGraphs.forEach { subGraph ->
		subGraph.destinations.apply {
			assertOneHomeInSubGraph()
			assertNoTopDestinationsPresent()
		}
	}

	destinations.assertOneHomeInSubGraph(noHomeAllowed = true)
	val home = destinations.firstOrNull { it.isHome }

	if (destinations.isNotEmpty() && home == null) {
		return GraphState.NoHome
	}

	return GraphState.Standard
}

private fun List<DestinationDescription>.assertOneHomeInSubGraph(noHomeAllowed: Boolean = false) {
	val home = filter { it.isHome }
	val homeAmount = home.size
	if (homeAmount > 1) {
		val homeNames = home.joinToString { it.name }
		error("Only one ${Destination::class.simpleName} is allowed to be marked as home within a graph or sub graph. Found ${home.size}: $homeNames")
	} else if (!noHomeAllowed && homeAmount == 0 && isNotEmpty()) {
		error("No ${Destination::class.simpleName} was marked as home")
	}
}

private fun List<DestinationDescription>.assertNoTopDestinationsPresent() {
	val hasTopDestinations = any { it.isTop }
	if (hasTopDestinations) {
		error("${Top::class.simpleName} destinations are not supported in sub graphs")
	}
}
