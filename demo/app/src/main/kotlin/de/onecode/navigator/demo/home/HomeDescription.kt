package de.onecode.navigator.demo.home

import de.onecode.navigator.api.Destination
import de.onecode.navigator.api.Home
import de.onecode.navigator.api.Navigation
import de.onecode.navigator.demo.destinations.FeatureComposableDestination

@Destination(name = "Home")
@Home
@Navigation(to = SubHomeDestination::class)
@Navigation(to = FeatureComposableDestination::class)
object HomeDescription

@Destination(name = "SubHome")
object SubHomeDestination
