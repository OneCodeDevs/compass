package de.onecode.navigator.demo.details

import de.onecode.compass.api.Destination
import de.onecode.compass.api.Navigation
import de.onecode.compass.api.Parameter
import de.onecode.compass.api.Top
import de.onecode.navigator.demo.wizard.WizardSubGraph

@Top
@Destination(name = "Details")
@Parameter(name = "myParam", type = Int::class)
@Navigation(to = WizardSubGraph::class)
object DetailsDescription