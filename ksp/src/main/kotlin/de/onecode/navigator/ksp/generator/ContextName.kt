package de.onecode.navigator.ksp.generator

import de.onecode.navigator.ksp.descriptions.DestinationDescription

internal val DestinationDescription.contextName: String
	get() = "${name}Context"