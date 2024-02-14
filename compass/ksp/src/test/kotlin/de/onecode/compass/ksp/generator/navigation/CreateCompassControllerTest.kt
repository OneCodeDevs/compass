package de.onecode.compass.ksp.generator.navigation

import com.google.common.truth.Truth.assertThat
import de.onecode.compass.ksp.buildTestFile
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.NavigationTarget
import de.onecode.compass.ksp.descriptions.ParameterDescription
import org.junit.jupiter.api.Test

class CreateCompassControllerTest {
	@Test
	fun `CompassController with one Destination no top`() {
		val description = DestinationDescription(
			name = "foo",
			parameters = emptyList(),
			navigationTargets = emptyList(),
			isHome = true,
			isTop = false
		)

		val code = buildTestFile {
			addType(createCompassController(listOf(description)))
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.compose.runtime.Composable
				|import androidx.compose.runtime.DisposableEffect
				|import androidx.compose.runtime.MutableState
				|import androidx.compose.runtime.State
				|import androidx.compose.runtime.derivedStateOf
				|import androidx.compose.runtime.mutableStateOf
				|import androidx.compose.runtime.remember
				|import androidx.navigation.NavController.OnDestinationChangedListener
				|import androidx.navigation.NavHostController
				|import androidx.navigation.NavOptionsBuilder
				|import javax.`annotation`.processing.Generated
				|import kotlin.Boolean
				|import kotlin.String
				|import kotlin.Unit
				|
				|@Generated
				|public class CompassController(
				|  internal val navController: NavHostController,
				|) {
				|  private val _currentDestinationName: MutableState<String?> = mutableStateOf<String?>("foo")
				|
				|
				|  public val currentDestinationName: State<String?> = _currentDestinationName
				|
				|  @Composable
				|  public fun RegisterCurrentDestinationListener() {
				|    DisposableEffect(key1 = navController) {
				|      val listener = OnDestinationChangedListener { _, destination, _ ->
				|      	_currentDestinationName.value = destination.route
				|      }
				|      navController.addOnDestinationChangedListener(listener)
				|      onDispose {
				|        navController.removeOnDestinationChangedListener(listener)
				|      }
				|    }
				|  }
				|
				|  @Composable
				|  public fun currentDestinationIsFoo(): State<Boolean> = remember {
				|    derivedStateOf {
				|      _currentDestinationName.value == "foo"
				|    }
				|  }
				|
				|  public fun navigateToFoo(navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				|    popUpTo("foo") {
				|      inclusive = true
				|    }
				|  }
				|  ) {
				|    navController.navigate(""${'"'}foo""${'"'}) {
				|      navOptionsBlock()
				|    }
				|  }
				|}
				|
				""".trimMargin()
			)
	}

	@Test
	fun `CompassController with two Destination no top`() {
		val param1 = ParameterDescription("param1", "kotlin.Int")
		val description1 = DestinationDescription(
			name = "foo",
			parameters = emptyList(),
			navigationTargets = listOf(NavigationTarget("bar", listOf(param1))),
			isHome = true,
			isTop = false
		)
		val description2 = DestinationDescription(
			name = "bar",
			parameters = listOf(param1),
			navigationTargets = emptyList(),
			isHome = false,
			isTop = false
		)

		val code = buildTestFile {
			addType(createCompassController(listOf(description1, description2)))
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.compose.runtime.Composable
				|import androidx.compose.runtime.DisposableEffect
				|import androidx.compose.runtime.MutableState
				|import androidx.compose.runtime.State
				|import androidx.compose.runtime.derivedStateOf
				|import androidx.compose.runtime.mutableStateOf
				|import androidx.compose.runtime.remember
				|import androidx.navigation.NavController.OnDestinationChangedListener
				|import androidx.navigation.NavHostController
				|import androidx.navigation.NavOptionsBuilder
				|import javax.`annotation`.processing.Generated
				|import kotlin.Boolean
				|import kotlin.String
				|import kotlin.Unit
				|
				|@Generated
				|public class CompassController(
				|  internal val navController: NavHostController,
				|) {
				|  private val _currentDestinationName: MutableState<String?> = mutableStateOf<String?>("foo")
				|
				|
				|  public val currentDestinationName: State<String?> = _currentDestinationName
				|
				|  @Composable
				|  public fun RegisterCurrentDestinationListener() {
				|    DisposableEffect(key1 = navController) {
				|      val listener = OnDestinationChangedListener { _, destination, _ ->
				|      	_currentDestinationName.value = destination.route
				|      }
				|      navController.addOnDestinationChangedListener(listener)
				|      onDispose {
				|        navController.removeOnDestinationChangedListener(listener)
				|      }
				|    }
				|  }
				|
				|  @Composable
				|  public fun currentDestinationIsFoo(): State<Boolean> = remember {
				|    derivedStateOf {
				|      _currentDestinationName.value == "foo"
				|    }
				|  }
				|
				|  public fun navigateToFoo(navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				|    popUpTo("foo") {
				|      inclusive = true
				|    }
				|  }
				|  ) {
				|    navController.navigate(""${'"'}foo""${'"'}) {
				|      navOptionsBlock()
				|    }
				|  }
				|}
				|
				""".trimMargin()
			)
	}

	@Test
	fun `CompassController with two Destination and one top`() {
		val param1 = ParameterDescription("param1", "kotlin.Int")
		val description1 = DestinationDescription(
			name = "foo",
			parameters = emptyList(),
			navigationTargets = listOf(NavigationTarget("bar", listOf(param1))),
			isHome = true,
			isTop = false
		)
		val description2 = DestinationDescription(
			name = "bar",
			parameters = listOf(param1),
			navigationTargets = emptyList(),
			isHome = false,
			isTop = true
		)

		val code = buildTestFile {
			addType(createCompassController(listOf(description1, description2)))
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.compose.runtime.Composable
				|import androidx.compose.runtime.DisposableEffect
				|import androidx.compose.runtime.MutableState
				|import androidx.compose.runtime.State
				|import androidx.compose.runtime.derivedStateOf
				|import androidx.compose.runtime.mutableStateOf
				|import androidx.compose.runtime.remember
				|import androidx.navigation.NavController.OnDestinationChangedListener
				|import androidx.navigation.NavHostController
				|import androidx.navigation.NavOptionsBuilder
				|import javax.`annotation`.processing.Generated
				|import kotlin.Boolean
				|import kotlin.String
				|import kotlin.Unit
				|
				|@Generated
				|public class CompassController(
				|  internal val navController: NavHostController,
				|) {
				|  private val _currentDestinationName: MutableState<String?> = mutableStateOf<String?>("foo")
				|
				|
				|  public val currentDestinationName: State<String?> = _currentDestinationName
				|
				|  @Composable
				|  public fun RegisterCurrentDestinationListener() {
				|    DisposableEffect(key1 = navController) {
				|      val listener = OnDestinationChangedListener { _, destination, _ ->
				|      	_currentDestinationName.value = destination.route
				|      }
				|      navController.addOnDestinationChangedListener(listener)
				|      onDispose {
				|        navController.removeOnDestinationChangedListener(listener)
				|      }
				|    }
				|  }
				|
				|  @Composable
				|  public fun currentDestinationIsFoo(): State<Boolean> = remember {
				|    derivedStateOf {
				|      _currentDestinationName.value == "foo"
				|    }
				|  }
				|
				|  public fun navigateToFoo(navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				|    popUpTo("foo") {
				|      inclusive = true
				|    }
				|  }
				|  ) {
				|    navController.navigate(""${'"'}foo""${'"'}) {
				|      navOptionsBlock()
				|    }
				|  }
				|
                |  @Composable
                |  public fun currentDestinationIsBar(): State<Boolean> = remember {
                |    derivedStateOf {
                |      _currentDestinationName.value == "bar"
                |    }
                |  }
				|
                |  public fun navigateToBar(param1: kotlin.Int, navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
                |        popUpTo("foo") {
                |        }
                |      }
                |  ) {
                |    navController.navigate(""${'"'}bar/${'$'}{param1}""${'"'}) {
                |      navOptionsBlock()
                |    }
                |  }
				|}
				|
				""".trimMargin()
			)
	}
}