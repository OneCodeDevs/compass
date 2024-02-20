package de.onecode.compass.ksp.generator.common

import de.onecode.compass.ksp.descriptions.DeepLinkDescription
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.NavigationTarget
import de.onecode.compass.ksp.descriptions.ParameterDescription
import de.onecode.compass.ksp.descriptions.SubGraphDescription

fun destinationDescription(
	name: String,
	parameters: List<ParameterDescription> = emptyList(),
	navigationTargets: List<NavigationTarget> = emptyList(),
	deepLinks: List<DeepLinkDescription> = emptyList(),
	isHome: Boolean = false,
	isTop: Boolean = false,
): DestinationDescription =
	DestinationDescription(
		name = name,
		parameters = parameters,
		navigationTargets = navigationTargets,
		deepLinks = deepLinks,
		isHome = isHome,
		isTop = isTop
	)

fun subGraphDescription(name: String, vararg descriptions: DestinationDescription): SubGraphDescription =
	SubGraphDescription(
		name = name,
		destinations = descriptions.toList()
	)