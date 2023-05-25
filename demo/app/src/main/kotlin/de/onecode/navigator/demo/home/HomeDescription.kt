package de.onecode.navigator.demo.home

import de.onecode.navigator.api.Destination
import de.onecode.navigator.api.Home
import de.onecode.navigator.api.Navigation

@Destination(name = "Home")
@Home
@Navigation(to = SubHomeDestination::class)
object HomeDescription

@Destination(name = "SubHome")
object SubHomeDestination
