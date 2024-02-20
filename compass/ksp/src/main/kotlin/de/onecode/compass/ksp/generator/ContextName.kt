package de.onecode.compass.ksp.generator

import de.onecode.compass.ksp.descriptions.DestinationDescription

internal val DestinationDescription.contextName: String
	get() = "${name}Context"
