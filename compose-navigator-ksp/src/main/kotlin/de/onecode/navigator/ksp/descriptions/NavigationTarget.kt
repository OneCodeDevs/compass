package de.onecode.navigator.ksp.descriptions

data class NavigationTarget(
	override val name: String,
	override val parameters: List<ParameterDescription>
) : NamedWithParameters