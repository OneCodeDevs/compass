package de.onecode.compass.ksp.generator.navigation

import de.onecode.compass.ksp.assertGeneratedCode
import de.onecode.compass.ksp.buildTestFile
import de.onecode.compass.ksp.descriptions.ParameterDescription
import de.onecode.compass.ksp.generator.common.destinationDescription
import org.junit.jupiter.api.Test

@Suppress("RedundantVisibilityModifier", "TestFunctionName")
class CreateCompassComposableTest {
	@Test
	fun `Navigator with a destination without parameters`() {
		val description = destinationDescription(
			name = "foo",
			isHome = true,
		)

		val code = buildTestFile {
			addFunction(createCompassComposable(listOf(description)))
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.compose.runtime.Composable
				import androidx.compose.ui.Modifier
				import androidx.navigation.NavGraphBuilder
				import de.onecode.compass.CompassController
				import de.onecode.compass.ScreenBuilder
				import javax.`annotation`.processing.Generated
				import kotlin.Unit
				
				@Generated
				@Composable
				public fun Compass(
				  modifier: Modifier = Modifier,
				  compassController: CompassController = rememberCompassController(),
				  builder: ScreenBuilder.(NavGraphBuilder) -> Unit,
				) {
				  compassController.RegisterCurrentDestinationListener()
				  val navController = compassController.navController
				  LocalNavHostController = compositionLocalOf { navController }
				  CompositionLocalProvider(LocalNavHostController provides navController) {
				    NavHost(modifier = modifier, startDestination = "foo", navController = navController) {
				      val screenBuilder = ScreenBuilderImpl()
				      screenBuilder.builder(this)
				      composable(route = "foo", arguments = emptyList() 
						, deepLinks = emptyList()
				      ) {
				        screenBuilder.fooComposable?.invoke(fooContext(LocalNavHostController.current, it))
				      }
				    }
				  }
				}
			"""
		)
	}

	@Test
	fun `Navigator with a destination with a parameter`() {
		val description = destinationDescription(
			name = "foo",
			parameters = listOf(ParameterDescription("param1", "kotlin.Int")),
			isHome = true,
		)

		val code = buildTestFile {
			addFunction(createCompassComposable(listOf(description)))
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.compose.runtime.Composable
				import androidx.compose.ui.Modifier
				import androidx.navigation.NavGraphBuilder
				import de.onecode.compass.CompassController
				import de.onecode.compass.ScreenBuilder
				import javax.`annotation`.processing.Generated
				import kotlin.Unit
				
				@Generated
				@Composable
				public fun Compass(
				  modifier: Modifier = Modifier,
				  compassController: CompassController = rememberCompassController(),
				  builder: ScreenBuilder.(NavGraphBuilder) -> Unit,
				) {
				  compassController.RegisterCurrentDestinationListener()
				  val navController = compassController.navController
				  LocalNavHostController = compositionLocalOf { navController }
				  CompositionLocalProvider(LocalNavHostController provides navController) {
				    NavHost(modifier = modifier, startDestination = "foo", navController = navController) {
				      val screenBuilder = ScreenBuilderImpl()
				      screenBuilder.builder(this)
				      composable(route = "foo/{param1}", arguments = listOf(navArgument(name = "param1") {
				                        type = NavType.IntType
				                      }
				          )
						  , deepLinks = emptyList()
				      ) {
				        screenBuilder.fooComposable?.invoke(fooContext(LocalNavHostController.current, it))
				      }
				    }
				  }
				}
			"""
		)
	}
}
