package io.redandroid.navigator.demo.wizard

import io.redandroid.navigator.api.SubGraph

@SubGraph(
	name = "Wizard",
	WizardScreen1Description::class,
	WizardScreen2Description::class
)
object WizardSubGraph