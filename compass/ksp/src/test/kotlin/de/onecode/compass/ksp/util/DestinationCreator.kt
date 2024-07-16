package de.onecode.compass.ksp.util

import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.NavigationTarget
import de.onecode.compass.ksp.descriptions.ParameterDescription

internal fun destination(
	name: String,
	parameters: List<ParameterDescription> = emptyList(),
	navigationTargets: List<NavigationTarget> = emptyList(),
	isHome: Boolean = false,
	isTop: Boolean = false,
	isDialog: Boolean = false
): DestinationDescription =
	DestinationDescription(
		name = name,
		parameters = parameters,
		navigationTargets = navigationTargets,
		isHome = isHome,
		isTop = isTop,
		isDialog = isDialog,
	)