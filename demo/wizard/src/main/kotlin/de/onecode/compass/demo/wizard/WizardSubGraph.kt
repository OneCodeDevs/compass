package de.onecode.compass.demo.wizard

import de.onecode.compass.api.SubGraph

@SubGraph(
	name = "Wizard",
	WizardScreen1Description::class,
	WizardScreen2Description::class
)
object WizardSubGraph