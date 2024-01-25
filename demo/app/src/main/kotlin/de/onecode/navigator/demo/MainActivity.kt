package de.onecode.navigator.demo

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
import de.onecode.navigator.Navigator
import de.onecode.navigator.demo.details.DetailsScreen
import de.onecode.navigator.demo.home.HomeScreen
import de.onecode.navigator.demo.home.SubHomeScreen
import de.onecode.navigator.demo.ui.theme.NavGraphConfigComposeTheme
import de.onecode.navigator.demo.wizard.attachWizardSubGraph
import de.onecode.navigator.rememberNavigatorController
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
	val navigatorController = rememberNavigatorController()

	val isHome by navigatorController.currentDestinationIsHome()
	val isDetails by navigatorController.currentDestinationIsDetails()

	Scaffold(
		bottomBar = {
			NavigationBar {
				NavigationBarItem(
					selected = isHome,
					label = { Text(text = "Home") },
					onClick = { navigatorController.navigateToHome() },
					icon = {
						Icon(imageVector = Icons.Default.Home, contentDescription = "")
					}
				)
				NavigationBarItem(
					selected = isDetails,
					label = { Text(text = "Wizard") },
					onClick = { navigatorController.navigateToDetails(Random.nextInt()) },
					icon = {
						Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = "")
					}
				)
			}
		}
	) { scaffoldPadding ->
		Navigator(
			modifier = Modifier
				.padding(scaffoldPadding)
				.padding(16.dp),
			navigatorController = navigatorController
		) { navGraphBuilder ->
			homeScreen {
				HomeScreen(
					onToSub = ::navigateToSubHome
				)
			}

			subHomeScreen {
				SubHomeScreen()
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
		}
	}
}