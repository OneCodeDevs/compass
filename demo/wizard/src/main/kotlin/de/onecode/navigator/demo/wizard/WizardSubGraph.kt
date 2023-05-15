package de.onecode.navigator.demo.wizard

import de.onecode.navigator.api.SubGraph

@SubGraph(
	name = "Wizard",
	WizardScreen1Description::class,
	WizardScreen2Description::class
)
object WizardSubGraph