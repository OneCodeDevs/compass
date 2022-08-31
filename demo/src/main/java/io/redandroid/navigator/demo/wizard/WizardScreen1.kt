package io.redandroid.navigator.demo.wizard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun WizardScreen1(onToScreen2: () -> Unit) {
	Column(verticalArrangement = Arrangement.SpaceEvenly) {
		Text(text = "Screen 1")
		Button(onClick = { onToScreen2() }) {
			Text(text = "To screen 2")
		}
	}
}