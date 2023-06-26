package de.onecode.navigator.demo.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(
	onToSub: () -> Unit
) {
	Column(verticalArrangement = Arrangement.SpaceBetween) {
		Button(
			onClick = { onToSub() }
		) {
			Text(text = "GoTo Sub")
		}
	}
}
