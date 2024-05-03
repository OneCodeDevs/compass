package de.onecode.compass.ksp.generator.navigation

import de.onecode.compass.ksp.assertGeneratedCode
import de.onecode.compass.ksp.buildTestFile
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.NavigationTarget
import de.onecode.compass.ksp.descriptions.ParameterDescription
import org.junit.jupiter.api.Test

@Suppress("RedundantVisibilityModifier", "RemoveRedundantQualifierName", "TestFunctionName")
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

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.compose.runtime.Composable
				import androidx.compose.runtime.DisposableEffect
				import androidx.compose.runtime.MutableState
				import androidx.compose.runtime.State
				import androidx.compose.runtime.derivedStateOf
				import androidx.compose.runtime.mutableStateOf
				import androidx.compose.runtime.remember
				import androidx.navigation.NavController.OnDestinationChangedListener
				import androidx.navigation.NavHostController
				import androidx.navigation.NavOptionsBuilder
				import javax.`annotation`.processing.Generated
				import kotlin.Boolean
				import kotlin.String
				import kotlin.Unit
				
				@Generated
				public class CompassController(
				  internal val navController: NavHostController,
				) {
				  private val _currentDestinationName: MutableState<String?> = mutableStateOf<String?>("foo")
				
				
				  public val currentDestinationName: State<String?> = _currentDestinationName
				
				  @Composable
				  public fun RegisterCurrentDestinationListener() {
				    DisposableEffect(key1 = navController) {
				      val listener = OnDestinationChangedListener { _, destination, _ ->
				        _currentDestinationName.value = destination.route
				      }
				      navController.addOnDestinationChangedListener(listener)
				      onDispose {
				        navController.removeOnDestinationChangedListener(listener)
				      }
				    }
				  }
				
				  @Composable
				  public fun currentDestinationIsFoo(): State<Boolean> = remember {
				    derivedStateOf {
				      _currentDestinationName.value == "foo" || 
				      _currentDestinationName.value?.startsWith("foo/") == true ||
				      _currentDestinationName.value?.startsWith("foo?") == true
				    }
				  }
				
				  public fun navigateToFoo(navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				    popUpTo("foo") {
				      inclusive = true
				    }
				  }
				  ) {
				    val optionalQueryStatement = "" 
				    navController.navigate(${'"'}foo${'$'}optionalQueryStatement${'"'}) {
				      navOptionsBlock()
				    }
				  }
				}
			"""
		)
	}

	@Test
	fun `CompassController with two Destination no top`() {
		val param1 = ParameterDescription(name = "param1", type = "kotlin.Int", required = true)
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

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.compose.runtime.Composable
				import androidx.compose.runtime.DisposableEffect
				import androidx.compose.runtime.MutableState
				import androidx.compose.runtime.State
				import androidx.compose.runtime.derivedStateOf
				import androidx.compose.runtime.mutableStateOf
				import androidx.compose.runtime.remember
				import androidx.navigation.NavController.OnDestinationChangedListener
				import androidx.navigation.NavHostController
				import androidx.navigation.NavOptionsBuilder
				import javax.`annotation`.processing.Generated
				import kotlin.Boolean
				import kotlin.String
				import kotlin.Unit
				
				@Generated
				public class CompassController(
				  internal val navController: NavHostController,
				) {
				  private val _currentDestinationName: MutableState<String?> = mutableStateOf<String?>("foo")
				
				
				  public val currentDestinationName: State<String?> = _currentDestinationName
				
				  @Composable
				  public fun RegisterCurrentDestinationListener() {
				    DisposableEffect(key1 = navController) {
				      val listener = OnDestinationChangedListener { _, destination, _ ->
				        _currentDestinationName.value = destination.route
				      }
				      navController.addOnDestinationChangedListener(listener)
				      onDispose {
				        navController.removeOnDestinationChangedListener(listener)
				      }
				    }
				  }
				
				  @Composable
				  public fun currentDestinationIsFoo(): State<Boolean> = remember {
				    derivedStateOf {
				      _currentDestinationName.value == "foo" || 
				      _currentDestinationName.value?.startsWith("foo/") == true ||
				      _currentDestinationName.value?.startsWith("foo?") == true
				    }
				  }
				
				  public fun navigateToFoo(navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				    popUpTo("foo") {
				      inclusive = true
				    }
				  }
				  ) {
				    val optionalQueryStatement = "" 
				    navController.navigate(${'"'}foo${'$'}optionalQueryStatement${'"'}) {
				      navOptionsBlock()
				    }
				  }
				}
			"""
		)
	}

	@Test
	fun `CompassController with two Destination and one top`() {
		val param1 = ParameterDescription(name = "param1", type = "kotlin.Int", required = true)
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

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.compose.runtime.Composable
				import androidx.compose.runtime.DisposableEffect
				import androidx.compose.runtime.MutableState
				import androidx.compose.runtime.State
				import androidx.compose.runtime.derivedStateOf
				import androidx.compose.runtime.mutableStateOf
				import androidx.compose.runtime.remember
				import androidx.navigation.NavController.OnDestinationChangedListener
				import androidx.navigation.NavHostController
				import androidx.navigation.NavOptionsBuilder
				import javax.`annotation`.processing.Generated
				import kotlin.Boolean
				import kotlin.String
				import kotlin.Unit
				
				@Generated
				public class CompassController(
				  internal val navController: NavHostController,
				) {
				  private val _currentDestinationName: MutableState<String?> = mutableStateOf<String?>("foo")
				
				
				  public val currentDestinationName: State<String?> = _currentDestinationName
				
				  @Composable
				  public fun RegisterCurrentDestinationListener() {
				    DisposableEffect(key1 = navController) {
				      val listener = OnDestinationChangedListener { _, destination, _ ->
				      	_currentDestinationName.value = destination.route
				      }
				      navController.addOnDestinationChangedListener(listener)
				      onDispose {
				        navController.removeOnDestinationChangedListener(listener)
				      }
				    }
				  }
				
				  @Composable
				  public fun currentDestinationIsFoo(): State<Boolean> = remember {
				    derivedStateOf { 
						_currentDestinationName.value == "foo" ||
						_currentDestinationName.value?.startsWith("foo/") == true ||
						_currentDestinationName.value?.startsWith("foo?") == true
				    }
				  }
				
				  public fun navigateToFoo(navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				    popUpTo("foo") {
				      inclusive = true
				    }
				  }
				  ) {
				    val optionalQueryStatement = "" 
				    navController.navigate(${'"'}foo${'$'}optionalQueryStatement${'"'}) {
				      navOptionsBlock()
				    }
				  }
				
				  @Composable
				  public fun currentDestinationIsBar(): State<Boolean> = remember {
				    derivedStateOf { 
						_currentDestinationName.value == "bar" ||
						_currentDestinationName.value?.startsWith("bar/") == true ||
						_currentDestinationName.value?.startsWith("bar?") == true
				    }
				  }
				
				  public fun navigateToBar(param1: kotlin.Int, navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				        popUpTo("foo") {
				        }
				      }
				  ) {
				    val optionalQueryStatement = "" 
				    navController.navigate(${'"'}bar/${'$'}{param1}${'$'}optionalQueryStatement${'"'}) {
				      navOptionsBlock()
				    }
				  }
				}
			"""
		)
	}
}
