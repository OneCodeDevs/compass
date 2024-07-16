package de.onecode.compass.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.onecode.compass.Compass
import de.onecode.compass.demo.destinations.attachFeatureComposable
import de.onecode.compass.demo.details.DetailsScreen
import de.onecode.compass.demo.dialog.MyDialogScreen
import de.onecode.compass.demo.home.HomeScreen
import de.onecode.compass.demo.home.SubHomeScreen
import de.onecode.compass.demo.theme.NavGraphConfigComposeTheme
import de.onecode.compass.demo.wizard.attachWizardSubGraph
import de.onecode.compass.rememberCompassController
import kotlin.random.Random

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			NavGraphConfigComposeTheme {
				Main()
			}
		}
	}
}

@Composable
fun Main() {
	val compassController = rememberCompassController()

	val isHome by compassController.currentDestinationIsHome()
	val isDetails by compassController.currentDestinationIsDetails()

	Scaffold(
		bottomBar = {
			NavigationBar {
				NavigationBarItem(
					selected = isHome,
					label = { Text(text = "Home") },
					onClick = { compassController.navigateToHome() },
					icon = {
						Icon(imageVector = Icons.Default.Home, contentDescription = "")
					}
				)
				NavigationBarItem(
					selected = isDetails,
					label = { Text(text = "Wizard") },
					onClick = { compassController.navigateToDetails(Random.nextInt()) },
					icon = {
						Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = "")
					}
				)
			}
		}
	) { scaffoldPadding ->
		Compass(
			modifier = Modifier
				.padding(scaffoldPadding)
				.padding(16.dp),
			compassController = compassController
		) { navGraphBuilder ->
			homeScreen {
				HomeScreen(
					onToSub = { navigateToSubHome(optionalParam = getOptionalParam()) },
					onToFeature = { navigateToFeatureComposable(Random.nextFloat()) },
					onToDialog = ::navigateToMyDialog
				)
			}

			myDialogScreen {
				MyDialogScreen(onClose = ::popBackStack)
			}

			subHomeScreen {
				SubHomeScreen(
					optionalValue = optionalParam
				)
			}

			detailsScreen {
				DetailsScreen(
					number = myParam,
					detailsViewModel = viewModel(),
					onOpenWizard = ::navigateToWizard,
					onBack = ::popBackStack
				)
			}

			navGraphBuilder.attachWizardSubGraph()

			navGraphBuilder.attachFeatureComposable()
		}
	}
}

private fun getOptionalParam(): String? =
	if (Random.nextBoolean()) {
		"I'm optional"
	} else {
		null
	}
