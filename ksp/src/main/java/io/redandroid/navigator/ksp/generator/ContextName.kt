package io.redandroid.navigator.ksp.generator

import io.redandroid.navigator.ksp.descriptions.DestinationDescription

internal val DestinationDescription.contextName: String
	get() = "${name}Context"