package de.onecode.navigator.demo.wizard

import androidx.navigation.NavGraphBuilder
import io.redandroid.navigator.wizardSubGraph

fun NavGraphBuilder.attachWizardSubGraph() {
	wizardSubGraph {
		wizardScreen1Screen {
			WizardScreen1(
				wizardInput = input,
				onToScreen2 = ::navigateToWizardScreen2
			)
		}
		wizardScreen2Screen {
			WizardScreen2(
				onLeave = ::leaveSubGraph
			)
		}
	}
}