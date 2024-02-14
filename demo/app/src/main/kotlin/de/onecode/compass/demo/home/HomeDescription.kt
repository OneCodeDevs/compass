package de.onecode.compass.demo.home

import de.onecode.compass.api.Destination
import de.onecode.compass.api.Home
import de.onecode.compass.api.Navigation
import de.onecode.compass.demo.destinations.FeatureComposableDestination

@Destination(name = "Home")
@Home
@Navigation(to = SubHomeDestination::class)
@Navigation(to = FeatureComposableDestination::class)
object HomeDescription

@Destination(name = "SubHome")
object SubHomeDestination
