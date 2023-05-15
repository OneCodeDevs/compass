package de.onecode.navigator.ksp.descriptions

data class GraphDescription(
	val destinations: List<DestinationDescription>,
	val subGraphs: List<SubGraphDescription>
)