package de.onecode.compass.demo.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
	onToSub: () -> Unit,
	onToFeature: () -> Unit,
	onToDialog: () -> Unit,
) {
	Column(verticalArrangement = Arrangement.SpaceBetween) {
		Button(
			onClick = onToSub
		) {
			Text(text = "GoTo Sub")
		}

		Spacer(modifier = Modifier.size(16.dp))

		Button(
			onClick = onToFeature
		) {
			Text(text = "GoTo Feature")
		}

		Spacer(modifier = Modifier.size(16.dp))

		Button(onClick = onToDialog) {
			Text(text = "GoTo Dialog")
		}
	}
}
