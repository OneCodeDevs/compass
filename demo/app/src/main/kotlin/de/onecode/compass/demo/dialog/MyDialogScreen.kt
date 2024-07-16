package de.onecode.compass.demo.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun MyDialogScreen(
	onClose: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.clip(RoundedCornerShape(4.dp))
			.background(MaterialTheme.colorScheme.background)
			.padding(16.dp),
	) {
		Text(text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.")
		Spacer(modifier = Modifier.size(8.dp))
		TextButton(
			onClick = onClose,
			modifier = Modifier.align(Alignment.End)
		) {
			Text(text = "Close")
		}
	}
}