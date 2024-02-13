package de.onecode.navigator.demo.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.onecode.compass.api.Destination
import de.onecode.compass.api.Parameter

@Destination("featureComposable")
@Parameter("fromApp", Float::class)
object FeatureComposableDestination

@Composable
fun FeatureComposable(fromApp: Float, modifier: Modifier = Modifier) {
	Column(
		modifier = modifier
			.padding(16.dp)
	) {
		Text(text = "I'm in a feature module")
		Text(text = "From App = $fromApp")
	}
}