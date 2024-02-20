package de.onecode.compass.demo.wizard

import de.onecode.compass.api.Destination
import de.onecode.compass.api.Home
import de.onecode.compass.api.Navigation
import de.onecode.compass.api.Parameter

@Destination(name = "WizardScreen1")
@Home
@Navigation(to = WizardScreen2Description::class)
@Parameter(name = "input", String::class)
object WizardScreen1Description
