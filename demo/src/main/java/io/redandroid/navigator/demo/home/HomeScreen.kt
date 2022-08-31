package io.redandroid.navigator.demo.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import kotlin.random.Random

@Composable
fun HomeScreen(
	onRandom: (Int) -> Unit,
	onWizard: () -> Unit
) {
	Column(verticalArrangement = Arrangement.SpaceBetween) {
		Button(
			onClick = {
				val randomNumber = Random.nextInt()
				onRandom(randomNumber)
			}
		) {
			Text(text = "Random")
		}
		Button(onClick = { onWizard() }) {
			Text(text = "Wizard")
		}
	}
}
