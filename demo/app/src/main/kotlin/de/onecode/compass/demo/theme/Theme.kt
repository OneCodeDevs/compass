package de.onecode.compass.demo.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
	primary = Purple200,
	secondary = Teal200,
	tertiary = Purple700,
)

private val LightColorPalette = lightColorScheme(
	primary = Purple500,
	secondary = Teal200,
	tertiary = Purple700,
)

@Composable
fun NavGraphConfigComposeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
	val colorScheme = if (darkTheme) {
		DarkColorPalette
	} else {
		LightColorPalette
	}

	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		shapes = Shapes,
		content = content
	)
}
