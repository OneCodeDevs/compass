package de.onecode.compass.demo.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SubHomeScreen(
	optionalValue: String?,
) {
	Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
		Text(text = "Navigation target reached")
		if (optionalValue != null) {
			Text(text = "Optional value provided = $optionalValue")
		}
	}
}
