package de.onecode.navigator.demo.details

import de.onecode.navigator.api.Destination
import de.onecode.navigator.api.Navigation
import de.onecode.navigator.api.Parameter
import de.onecode.navigator.api.Top
import de.onecode.navigator.demo.wizard.WizardSubGraph

@Top
@Destination(name = "Details")
@Parameter(name = "myParam", type = Int::class)
@Navigation(to = WizardSubGraph::class)
object DetailsDescription