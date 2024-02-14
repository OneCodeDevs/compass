package de.onecode.compass.ksp.generator.navigation

import com.google.common.truth.Truth.assertThat
import de.onecode.compass.ksp.buildTestFile
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.ParameterDescription
import org.junit.jupiter.api.Test

class CreateCompassComposableTest {
	@Test
	fun `Navigator with a destination without parameters`() {
		val description = DestinationDescription(
			name = "foo",
			parameters = emptyList(),
			navigationTargets = emptyList(),
			isHome = true,
			isTop = false
		)

		val code = buildTestFile {
			addFunction(createCompassComposable(listOf(description)))
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.compose.runtime.Composable
				|import androidx.compose.ui.Modifier
				|import androidx.navigation.NavGraphBuilder
				|import de.onecode.compass.CompassController
				|import de.onecode.compass.ScreenBuilder
				|import javax.`annotation`.processing.Generated
				|import kotlin.Unit
				|
				|@Generated
				|@Composable
				|public fun Compass(
				|  modifier: Modifier = Modifier,
				|  compassController: CompassController = rememberCompassController(),
				|  builder: ScreenBuilder.(NavGraphBuilder) -> Unit,
				|) {
				|  compassController.RegisterCurrentDestinationListener()
				|  val navController = compassController.navController
				|  LocalNavHostController = compositionLocalOf { navController }
				|  CompositionLocalProvider(LocalNavHostController provides navController) {
				|    NavHost(modifier = modifier, startDestination = "foo", navController = navController) {
				|      val screenBuilder = ScreenBuilderImpl()
				|      screenBuilder.builder(this)
				|      composable(route = "foo", arguments = emptyList()
				|      ) {
				|        screenBuilder.fooComposable?.invoke(fooContext(LocalNavHostController.current, it))
				|      }
				|    }
				|  }
				|}
				|
			""".trimMargin()
			)
	}

	@Test
	fun `Navigator with a destination with a parameter`() {
		val description = DestinationDescription(
			name = "foo",
			parameters = listOf(ParameterDescription("param1", "kotlin.Int")),
			navigationTargets = emptyList(),
			isHome = true,
			isTop = false
		)

		val code = buildTestFile {
			addFunction(createCompassComposable(listOf(description)))
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.compose.runtime.Composable
				|import androidx.compose.ui.Modifier
				|import androidx.navigation.NavGraphBuilder
				|import de.onecode.compass.CompassController
				|import de.onecode.compass.ScreenBuilder
				|import javax.`annotation`.processing.Generated
				|import kotlin.Unit
				|
				|@Generated
				|@Composable
				|public fun Compass(
				|  modifier: Modifier = Modifier,
				|  compassController: CompassController = rememberCompassController(),
				|  builder: ScreenBuilder.(NavGraphBuilder) -> Unit,
				|) {
				|  compassController.RegisterCurrentDestinationListener()
				|  val navController = compassController.navController
				|  LocalNavHostController = compositionLocalOf { navController }
				|  CompositionLocalProvider(LocalNavHostController provides navController) {
				|    NavHost(modifier = modifier, startDestination = "foo", navController = navController) {
				|      val screenBuilder = ScreenBuilderImpl()
				|      screenBuilder.builder(this)
				|      composable(route = "foo/{param1}", arguments = listOf(navArgument(name = "param1") {
                |            type = NavType.IntType
                |          }
				|          )
				|      ) {
				|        screenBuilder.fooComposable?.invoke(fooContext(LocalNavHostController.current, it))
				|      }
				|    }
				|  }
				|}
				|
			""".trimMargin()
			)
	}
}