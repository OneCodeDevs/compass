package de.onecode.compass.demo.details

import de.onecode.compass.api.Destination
import de.onecode.compass.api.Navigation
import de.onecode.compass.api.Parameter
import de.onecode.compass.api.Top
import de.onecode.compass.demo.wizard.WizardSubGraph

@Top
@Destination(name = "Details")
@Parameter(name = "myParam", type = Int::class)
@Navigation(to = WizardSubGraph::class)
object DetailsDescription