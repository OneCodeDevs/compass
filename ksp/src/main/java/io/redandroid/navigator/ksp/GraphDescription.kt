package io.redandroid.navigator.ksp

data class GraphDescription(
	val destinations: List<DestinationDescription>,
	val subGraphs: List<SubGraphDescription>
)