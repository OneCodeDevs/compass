package de.onecode.compass.ksp.descriptions

data class DestinationDescription(
	override val name: String,
	override val parameters: List<ParameterDescription>,
	val navigationTargets: List<NavigationTarget>,
	val isHome: Boolean,
	val isTop: Boolean,
	val isDialog: Boolean,
) : NamedWithParameters
