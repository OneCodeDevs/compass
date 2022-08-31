package io.redandroid.navigator.demo.wizard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun WizardScreen2(onLeave: () -> Unit) {
	Column(verticalArrangement = Arrangement.SpaceEvenly) {
		Text(text = "Screen 2")
		Button(onClick = { onLeave() }) {
			Text(text = "Leave")
		}
	}
}