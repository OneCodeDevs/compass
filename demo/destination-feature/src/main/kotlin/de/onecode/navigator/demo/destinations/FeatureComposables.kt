package de.onecode.navigator.demo.destinations

import androidx.navigation.NavGraphBuilder
import de.onecode.navigator.featureComposableScreen

fun NavGraphBuilder.attachFeatureComposable() {
	featureComposableScreen {
		FeatureComposable(fromApp)
	}
}