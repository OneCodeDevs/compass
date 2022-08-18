package de.gnarly.navgraph.config.details

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun DetailsScreen(number: Int) {
	Column {
		Text(text = "The result is")
		Text(text = number.toString(), fontWeight = FontWeight.Bold)
	}
}