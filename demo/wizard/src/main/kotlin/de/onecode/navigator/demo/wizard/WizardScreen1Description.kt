package de.onecode.navigator.demo.wizard

import de.onecode.navigator.api.Destination
import de.onecode.navigator.api.Home
import de.onecode.navigator.api.Navigation
import de.onecode.navigator.api.Parameter

@Destination(name = "WizardScreen1")
@Home
@Navigation(to = WizardScreen2Description::class)
@Parameter(name = "input", String::class)
object WizardScreen1Description