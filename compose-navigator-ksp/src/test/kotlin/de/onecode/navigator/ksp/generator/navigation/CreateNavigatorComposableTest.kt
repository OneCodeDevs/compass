package de.onecode.navigator.ksp.generator.navigation

import com.google.common.truth.Truth.assertThat
import de.onecode.navigator.ksp.buildTestFile
import de.onecode.navigator.ksp.descriptions.DestinationDescription
import de.onecode.navigator.ksp.descriptions.ParameterDescription
import org.junit.jupiter.api.Test

class CreateNavigatorComposableTest {
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
			addFunction(createNavigatorComposable(listOf(description)))
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.compose.runtime.Composable
				|import androidx.compose.ui.Modifier
				|import androidx.navigation.NavGraphBuilder
				|import de.onecode.navigator.NavigatorController
				|import de.onecode.navigator.ScreenBuilder
				|import javax.`annotation`.processing.Generated
				|import kotlin.Unit
				|
				|@Generated
				|@Composable
				|public fun Navigator(
				|  modifier: Modifier = Modifier,
				|  navigatorController: NavigatorController = rememberNavigatorController(),
				|  builder: ScreenBuilder.(NavGraphBuilder) -> Unit,
				|) {
				|  navigatorController.RegisterCurrentDestinationListener()
				|  val navController = navigatorController.navController
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
			addFunction(createNavigatorComposable(listOf(description)))
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.compose.runtime.Composable
				|import androidx.compose.ui.Modifier
				|import androidx.navigation.NavGraphBuilder
				|import de.onecode.navigator.NavigatorController
				|import de.onecode.navigator.ScreenBuilder
				|import javax.`annotation`.processing.Generated
				|import kotlin.Unit
				|
				|@Generated
				|@Composable
				|public fun Navigator(
				|  modifier: Modifier = Modifier,
				|  navigatorController: NavigatorController = rememberNavigatorController(),
				|  builder: ScreenBuilder.(NavGraphBuilder) -> Unit,
				|) {
				|  navigatorController.RegisterCurrentDestinationListener()
				|  val navController = navigatorController.navController
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