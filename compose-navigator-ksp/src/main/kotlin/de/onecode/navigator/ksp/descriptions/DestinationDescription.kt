package de.onecode.navigator.ksp.descriptions

data class DestinationDescription(
	override val name: String,
	override val parameters: List<ParameterDescription>,
	val navigationTargets: List<NavigationTarget>,
	val isHome: Boolean,
	val isTop: Boolean
): NamedWithParameters