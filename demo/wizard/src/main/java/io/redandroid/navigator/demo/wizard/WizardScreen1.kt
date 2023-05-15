package io.redandroid.navigator.demo.wizard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun WizardScreen1(wizardInput: String, onToScreen2: () -> Unit) {
	Column(verticalArrangement = Arrangement.SpaceEvenly) {
		Text(text = "Screen 1")
		Text(text = wizardInput)
		Button(onClick = { onToScreen2() }) {
			Text(text = "To screen 2")
		}
	}
}