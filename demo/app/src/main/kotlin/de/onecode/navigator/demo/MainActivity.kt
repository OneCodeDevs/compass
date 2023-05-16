package de.onecode.navigator.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.onecode.navigator.demo.details.DetailsScreen
import de.onecode.navigator.demo.home.HomeScreen
import de.onecode.navigator.demo.ui.theme.NavGraphConfigComposeTheme
import de.onecode.navigator.demo.wizard.attachWizardSubGraph
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
	Scaffold { scaffoldPadding ->
		Navigator(
			modifier = Modifier.padding(scaffoldPadding)
		) { navGraphBuilder ->
			homeScreen {
				HomeScreen(
					onRandom = ::navigateToDetails,
					onWizard = ::navigateToWizard
				)
			}

			detailsScreen {
				DetailsScreen(
					number = myParam,
					onBack = ::popBackStack
				)
			}

			navGraphBuilder.attachWizardSubGraph()
		}
	}
}