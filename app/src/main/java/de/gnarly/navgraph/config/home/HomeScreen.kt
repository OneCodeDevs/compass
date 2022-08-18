package de.gnarly.navgraph.config.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import kotlin.random.Random

@Composable
fun HomeScreen(
	onRandom: (Int) -> Unit
) {
	Column {
		Button(
			onClick = {
				val randomNumber = Random.nextInt()
				onRandom(randomNumber)
			}
		) {
			Text(text = "Random")
		}
	}
}
