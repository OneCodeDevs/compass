package de.onecode.compass.demo.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SubHomeScreen() {
	Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
		Text(text = "Navigation target reached")
	}
}
