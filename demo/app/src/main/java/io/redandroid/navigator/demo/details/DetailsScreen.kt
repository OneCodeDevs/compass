package io.redandroid.navigator.demo.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DetailsScreen(number: Int, onBack: () -> Unit) {
	Column {
		Text(text = "The result is")
		Text(text = number.toString(), fontWeight = FontWeight.Bold)
		Spacer(modifier = Modifier.size(16.dp))
		Button(onClick = { onBack() }) {
			Text(text = "Go back")
		}
	}
}