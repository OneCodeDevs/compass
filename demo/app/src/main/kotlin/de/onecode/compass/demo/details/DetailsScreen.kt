package de.onecode.compass.demo.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DetailsScreen(
	number: Int,
	detailsViewModel: DetailsViewModel,
	onOpenWizard: (String) -> Unit,
	onBack: () -> Unit,
) {

	var wizardInputValue by rememberSaveable {
		mutableStateOf("")
	}

	Column {
		Text(text = "We got the random parameter in the composable")
		Text(text = number.toString(), fontWeight = FontWeight.Bold)
		Text(text = "And also from the ViewModel")
		Text(text = detailsViewModel.parameterInViewModel.toString(), fontWeight = FontWeight.Bold)
		Spacer(modifier = Modifier.size(16.dp))
		TextField(
			value = wizardInputValue,
			onValueChange = { wizardInputValue = it },
			label = { Text(text = "Wizard input") },
			placeholder = { Text(text = "Enter a value for the wizard") }
		)
		Button(onClick = { onOpenWizard(wizardInputValue) }) {
			Text(text = "Open wizard")
		}
		Spacer(modifier = Modifier.size(32.dp))
		Button(onClick = { onBack() }) {
			Text(text = "Go back")
		}
	}
}
