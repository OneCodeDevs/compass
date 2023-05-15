package io.redandroid.navigator.demo.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlin.random.Random

@Composable
fun HomeScreen(
	onRandom: (Int) -> Unit,
	onWizard: (String) -> Unit
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
		Button(onClick = { onWizard("Call the Wizard") }) {
			Text(text = "Wizard")
		}
	}
}
