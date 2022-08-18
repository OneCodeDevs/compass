package de.gnarly.navgraph.config

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.gnarly.navgraph.config.details.DetailsScreen
import de.gnarly.navgraph.config.home.HomeScreen
import de.gnarly.navgraph.config.ui.theme.NavGraphConfigComposeTheme
import io.redandroid.navigator.Navigator

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			NavGraphConfigComposeTheme {
				Box(modifier = Modifier.padding(16.dp)) {
					Main()
				}
			}
		}
	}
}

@Composable
fun Main() {
	Navigator {
		homeScreen {
			HomeScreen(
				onRandom = {
					navigateToDetails(it)

				}
			)
		}

		detailsScreen {
			DetailsScreen(number = myParam)
		}
	}
}