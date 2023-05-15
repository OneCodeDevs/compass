package de.onecode.navigator.demo.home

import de.onecode.navigator.api.Destination
import de.onecode.navigator.api.Home
import de.onecode.navigator.api.Navigation
import de.onecode.navigator.demo.details.DetailsDescription
import de.onecode.navigator.demo.wizard.WizardSubGraph

@Destination(name = "Home")
@Home
@Navigation(to = DetailsDescription::class)
@Navigation(to = WizardSubGraph::class)
object HomeDescription
