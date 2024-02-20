package de.onecode.compass.demo.wizard

import androidx.navigation.NavGraphBuilder
import de.onecode.compass.wizardSubGraph

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
