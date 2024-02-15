package de.onecode.compass.ksp.generator

import de.onecode.compass.ksp.assertGeneratedCode
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.GraphDescription
import de.onecode.compass.ksp.descriptions.NavigationTarget
import de.onecode.compass.ksp.descriptions.ParameterDescription
import de.onecode.compass.ksp.descriptions.SubGraphDescription
import de.onecode.compass.ksp.writeToString
import org.junit.jupiter.api.Test

@Suppress("RedundantVisibilityModifier", "RemoveRedundantQualifierName", "CanBeParameter", "UnusedReceiverParameter", "TestFunctionName", "ClassName")
class CodeGeneratorTest {
	@Test
	fun `Two destinations not top and no subgraph`() {
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
		val graph = GraphDescription(
			destinations = listOf(description1, description2),
			subGraphs = emptyList()
		)

		val code = writeToString {
			generateNavigatorCode(graph)
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				package de.onecode.compass
				
				import androidx.compose.runtime.Composable
				import androidx.compose.runtime.CompositionLocalProvider
				import androidx.compose.runtime.DisposableEffect
				import androidx.compose.runtime.MutableState
				import androidx.compose.runtime.State
				import androidx.compose.runtime.compositionLocalOf
				import androidx.compose.runtime.derivedStateOf
				import androidx.compose.runtime.mutableStateOf
				import androidx.compose.runtime.remember
				import androidx.compose.ui.Modifier
				import androidx.lifecycle.SavedStateHandle
				import androidx.navigation.NavBackStackEntry
				import androidx.navigation.NavController.OnDestinationChangedListener
				import androidx.navigation.NavGraphBuilder
				import androidx.navigation.NavHostController
				import androidx.navigation.NavOptionsBuilder
				import androidx.navigation.NavType
				import androidx.navigation.compose.NavHost
				import androidx.navigation.compose.composable
				import androidx.navigation.compose.rememberNavController
				import androidx.navigation.navArgument
				import de.onecode.compass.runtime.CommonContext
				import de.onecode.compass.runtime.LocalNavHostController
				import javax.`annotation`.processing.Generated
				import kotlin.Boolean
				import kotlin.Int
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
				      _currentDestinationName.value == "foo"
				    }
				  }
				
				  public fun navigateToFoo(navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				    popUpTo("foo") {
				      inclusive = true
				    }
				  }
				  ) {
				    navController.navigate(""${'"'}foo""${'"'}) {
				      navOptionsBlock()
				    }
				  }
				}
				
				@Generated
				@Composable
				public fun rememberCompassController(navController: NavHostController = rememberNavController()):
				    CompassController = remember(key1 = navController) {
				  CompassController(navController)
				}
				
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
				      ) {
				        screenBuilder.fooComposable?.invoke(fooContext(LocalNavHostController.current, it))
				      }
				      composable(route = "bar/{param1}", arguments = listOf(navArgument(name = "param1") {
				            type = NavType.IntType
				          }
				          )
				      ) {
				        screenBuilder.barComposable?.invoke(barContext(LocalNavHostController.current, it))
				      }
				    }
				  }
				}
				
				@Generated
				public interface ScreenBuilder {
				  public fun fooScreen(composable: @Composable fooContext.() -> Unit)
				
				  public fun barScreen(composable: @Composable barContext.() -> Unit)
				}
				
				@Generated
				private class ScreenBuilderImpl : ScreenBuilder {
				  internal var fooComposable: @Composable (fooContext.() -> Unit)? = null
				
				  internal var barComposable: @Composable (barContext.() -> Unit)? = null
				
				  override fun fooScreen(composable: @Composable fooContext.() -> Unit) {
				    fooComposable = composable
				  }
				
				  override fun barScreen(composable: @Composable barContext.() -> Unit) {
				    barComposable = composable
				  }
				}
				
				@Generated
				public class fooContext(
				  private val navHostController: NavHostController,
				  private val navBackStackEntry: NavBackStackEntry,
				) : CommonContext(navHostController) {
				  public fun navigateToBar(param1: kotlin.Int, navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				      }
				  ) {
				    navHostController.navigate(""${'"'}bar/${'$'}{param1}""${'"'}) {
				      navOptionsBlock()
				    }
				  }
				}
				
				@Generated
				public class barContext(
				  private val navHostController: NavHostController,
				  private val navBackStackEntry: NavBackStackEntry,
				) : CommonContext(navHostController) {
				  public val param1: kotlin.Int
				    get() = navBackStackEntry.arguments?.getInt("param1") ?:
				        error("Required parameter param1 not provided")
				}
				
				@Generated
				public fun SavedStateHandle.getParam1(): Int = get<Int>("param1") ?:
				    error("Required parameter param1 not provided")
			"""
			)
	}

	@Test
	fun `Two destinations one top and no subgraph`() {
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
		val graph = GraphDescription(
			destinations = listOf(description1, description2),
			subGraphs = emptyList()
		)

		val code = writeToString {
			generateNavigatorCode(graph)
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				package de.onecode.compass
				
				import androidx.compose.runtime.Composable
				import androidx.compose.runtime.CompositionLocalProvider
				import androidx.compose.runtime.DisposableEffect
				import androidx.compose.runtime.MutableState
				import androidx.compose.runtime.State
				import androidx.compose.runtime.compositionLocalOf
				import androidx.compose.runtime.derivedStateOf
				import androidx.compose.runtime.mutableStateOf
				import androidx.compose.runtime.remember
				import androidx.compose.ui.Modifier
				import androidx.lifecycle.SavedStateHandle
				import androidx.navigation.NavBackStackEntry
				import androidx.navigation.NavController.OnDestinationChangedListener
				import androidx.navigation.NavGraphBuilder
				import androidx.navigation.NavHostController
				import androidx.navigation.NavOptionsBuilder
				import androidx.navigation.NavType
				import androidx.navigation.compose.NavHost
				import androidx.navigation.compose.composable
				import androidx.navigation.compose.rememberNavController
				import androidx.navigation.navArgument
				import de.onecode.compass.runtime.CommonContext
				import de.onecode.compass.runtime.LocalNavHostController
				import javax.`annotation`.processing.Generated
				import kotlin.Boolean
				import kotlin.Int
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
					        _currentDestinationName.value == "foo"
						}
					}
					
					public fun navigateToFoo(navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
							popUpTo("foo") {
					            inclusive = true
					        }
						}
					) {
					    navController.navigate(""${'"'}foo""${'"'}) { 
							navOptionsBlock()
					    } 
					}
					
					@Composable
					public fun currentDestinationIsBar(): State<Boolean> = remember {
						derivedStateOf {
							_currentDestinationName.value == "bar"
						}
					}
					
					public fun navigateToBar(param1: kotlin.Int, navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
							popUpTo("foo") {
							}
						}
					) {
						navController.navigate(""${'"'}bar/${'$'}{param1}""${'"'}) {
							navOptionsBlock()
						}
					}
				}
				
				@Generated
				@Composable
				public fun rememberCompassController(navController: NavHostController = rememberNavController()):
				    CompassController = remember(key1 = navController) {
				  CompassController(navController)
				}
				
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
				      ) {
				        screenBuilder.fooComposable?.invoke(fooContext(LocalNavHostController.current, it))
				      }
				      composable(route = "bar/{param1}", arguments = listOf(navArgument(name = "param1") {
				            type = NavType.IntType
				          }
				          )
				      ) {
				        screenBuilder.barComposable?.invoke(barContext(LocalNavHostController.current, it))
				      }
				    }
				  }
				}
				
				@Generated
				public interface ScreenBuilder {
				  public fun fooScreen(composable: @Composable fooContext.() -> Unit)
				
				  public fun barScreen(composable: @Composable barContext.() -> Unit)
				}
				
				@Generated
				private class ScreenBuilderImpl : ScreenBuilder {
				  internal var fooComposable: @Composable (fooContext.() -> Unit)? = null
				
				  internal var barComposable: @Composable (barContext.() -> Unit)? = null
				
				  override fun fooScreen(composable: @Composable fooContext.() -> Unit) {
				    fooComposable = composable
				  }
				
				  override fun barScreen(composable: @Composable barContext.() -> Unit) {
				    barComposable = composable
				  }
				}
				
				@Generated
				public class fooContext(
				  private val navHostController: NavHostController,
				  private val navBackStackEntry: NavBackStackEntry,
				) : CommonContext(navHostController) {
				  public fun navigateToBar(param1: kotlin.Int, navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				      }
				  ) {
				    navHostController.navigate(""${'"'}bar/${'$'}{param1}""${'"'}) {
				      navOptionsBlock()
				    }
				  }
				}
				
				@Generated
				public class barContext(
				  private val navHostController: NavHostController,
				  private val navBackStackEntry: NavBackStackEntry,
				) : CommonContext(navHostController) {
				  public val param1: kotlin.Int
				    get() = navBackStackEntry.arguments?.getInt("param1") ?:
				        error("Required parameter param1 not provided")
				}
				
				@Generated
				public fun SavedStateHandle.getParam1(): Int = get<Int>("param1") ?:
				    error("Required parameter param1 not provided")
			"""
			)
	}

	@Test
	fun `Two destinations one top and subgraph`() {
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

		val sub1 = DestinationDescription(
			name = "sub1",
			parameters = emptyList(),
			navigationTargets = emptyList(),
			isHome = true,
			isTop = false
		)
		val sub2 = DestinationDescription(
			name = "sub1",
			parameters = emptyList(),
			navigationTargets = emptyList(),
			isHome = false,
			isTop = false
		)
		val sub = SubGraphDescription("sub", listOf(sub1, sub2))
		val graph = GraphDescription(
			destinations = listOf(description1, description2),
			subGraphs = listOf(sub)
		)

		val code = writeToString {
			generateNavigatorCode(graph)
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				package de.onecode.compass
				
				import androidx.compose.runtime.Composable
				import androidx.compose.runtime.CompositionLocalProvider
				import androidx.compose.runtime.DisposableEffect
				import androidx.compose.runtime.MutableState
				import androidx.compose.runtime.State
				import androidx.compose.runtime.compositionLocalOf
				import androidx.compose.runtime.derivedStateOf
				import androidx.compose.runtime.mutableStateOf
				import androidx.compose.runtime.remember
				import androidx.compose.ui.Modifier
				import androidx.lifecycle.SavedStateHandle
				import androidx.navigation.NavBackStackEntry
				import androidx.navigation.NavController.OnDestinationChangedListener
				import androidx.navigation.NavGraphBuilder
				import androidx.navigation.NavHostController
				import androidx.navigation.NavOptionsBuilder
				import androidx.navigation.NavType
				import androidx.navigation.compose.NavHost
				import androidx.navigation.compose.composable
				import androidx.navigation.compose.navigation
				import androidx.navigation.compose.rememberNavController
				import androidx.navigation.navArgument
				import de.onecode.compass.runtime.CommonContext
				import de.onecode.compass.runtime.LocalNavHostController
				import javax.`annotation`.processing.Generated
				import kotlin.Boolean
				import kotlin.Int
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
				            _currentDestinationName.value == "foo"
				        }
					}
				
					public fun navigateToFoo(navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				            popUpTo("foo") {
				                inclusive = true
				            }
				        }
				    ) {
				        navController.navigate(""${'"'}foo""${'"'}) {
				            navOptionsBlock()
				        }
					}
				
				    @Composable
					public fun currentDestinationIsBar(): State<Boolean> = remember {
				         derivedStateOf {
				            _currentDestinationName.value == "bar"
				         }
					}
				     
					public fun navigateToBar(param1: kotlin.Int, navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				             popUpTo("foo") {
				             }
				           }
					) {
						navController.navigate(""${'"'}bar/${'$'}{param1}""${'"'}) { 
							navOptionsBlock()
						}
					}
				}
				
				@Generated
				@Composable
				public fun rememberCompassController(navController: NavHostController = rememberNavController()):
				    CompassController = remember(key1 = navController) {
				  CompassController(navController)
				}
				
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
				      ) {
				        screenBuilder.fooComposable?.invoke(fooContext(LocalNavHostController.current, it))
				      }
				      composable(route = "bar/{param1}", arguments = listOf(navArgument(name = "param1") {
				            type = NavType.IntType
				          }
				          )
				      ) {
				        screenBuilder.barComposable?.invoke(barContext(LocalNavHostController.current, it))
				      }
				    }
				  }
				}
				
				@Generated
				public interface ScreenBuilder {
				  public fun fooScreen(composable: @Composable fooContext.() -> Unit)
				
				  public fun barScreen(composable: @Composable barContext.() -> Unit)
				}
				
				@Generated
				private class ScreenBuilderImpl : ScreenBuilder {
				  internal var fooComposable: @Composable (fooContext.() -> Unit)? = null
				
				  internal var barComposable: @Composable (barContext.() -> Unit)? = null
				
				  override fun fooScreen(composable: @Composable fooContext.() -> Unit) {
				    fooComposable = composable
				  }
				
				  override fun barScreen(composable: @Composable barContext.() -> Unit) {
				    barComposable = composable
				  }
				}
				
				@Generated
				public class fooContext(
				  private val navHostController: NavHostController,
				  private val navBackStackEntry: NavBackStackEntry,
				) : CommonContext(navHostController) {
				  public fun navigateToBar(param1: kotlin.Int, navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				      }
				  ) {
				    navHostController.navigate(""${'"'}bar/${'$'}{param1}""${'"'}) {
				      navOptionsBlock()
				    }
				  }
				}
				
				@Generated
				public class barContext(
				  private val navHostController: NavHostController,
				  private val navBackStackEntry: NavBackStackEntry,
				) : CommonContext(navHostController) {
				  public val param1: kotlin.Int
				    get() = navBackStackEntry.arguments?.getInt("param1") ?:
				        error("Required parameter param1 not provided")
				}
				
				@Generated
				public fun SavedStateHandle.getParam1(): Int = get<Int>("param1") ?:
				    error("Required parameter param1 not provided")
				
				@Generated
				public fun NavGraphBuilder.subSubGraph(builder: subScreenBuilder.(NavGraphBuilder) -> Unit) {
				  navigation(startDestination = "sub1", route = "sub") {
				    val screenBuilder = subScreenBuilderImpl()
				    screenBuilder.builder(this)
				    composable(route = "sub1", arguments = emptyList()
				    ) {
				      screenBuilder.sub1Composable?.invoke(sub1Context(LocalNavHostController.current, it))
				    }
				    composable(route = "sub1", arguments = emptyList()
				    ) {
				      screenBuilder.sub1Composable?.invoke(sub1Context(LocalNavHostController.current, it))
				    }
				  }
				}
				
				@Generated
				public interface subScreenBuilder {
				  public fun sub1Screen(composable: @Composable sub1Context.() -> Unit)
				
				  public fun sub1Screen(composable: @Composable sub1Context.() -> Unit)
				}
				
				@Generated
				private class subScreenBuilderImpl : subScreenBuilder {
				  internal var sub1Composable: @Composable (sub1Context.() -> Unit)? = null
				
				  internal var sub1Composable: @Composable (sub1Context.() -> Unit)? = null
				
				  override fun sub1Screen(composable: @Composable sub1Context.() -> Unit) {
				    sub1Composable = composable
				  }
				
				  override fun sub1Screen(composable: @Composable sub1Context.() -> Unit) {
				    sub1Composable = composable
				  }
				}
				
				@Generated
				public abstract class subCommonContext(
				  private val navHostController: NavHostController,
				) : CommonContext(navHostController) {
				  public fun leaveSubGraph() {
				    navHostController.popBackStack(route = "sub", inclusive = true)
				  }
				}
				
				@Generated
				public class sub1Context(
				  private val navHostController: NavHostController,
				  private val navBackStackEntry: NavBackStackEntry,
				) : subCommonContext(navHostController)
				
				@Generated
				public class sub1Context(
				  private val navHostController: NavHostController,
				  private val navBackStackEntry: NavBackStackEntry,
				) : subCommonContext(navHostController)
			"""
			)
	}

	@Test
	fun `Two destinations no home`() {
		val param1 = ParameterDescription("param1", "kotlin.Int")
		val description1 = DestinationDescription(
			name = "foo",
			parameters = emptyList(),
			navigationTargets = listOf(NavigationTarget("bar", listOf(param1))),
			isHome = false,
			isTop = false
		)
		val description2 = DestinationDescription(
			name = "bar",
			parameters = listOf(param1),
			navigationTargets = emptyList(),
			isHome = false,
			isTop = false
		)

		val graph = GraphDescription(listOf(description1, description2), emptyList())

		val code = writeToString {
			generateAddDestinationCode(graph)
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				package de.onecode.compass
				
				import androidx.compose.runtime.Composable
				import androidx.lifecycle.SavedStateHandle
				import androidx.navigation.NavBackStackEntry
				import androidx.navigation.NavGraphBuilder
				import androidx.navigation.NavHostController
				import androidx.navigation.NavOptionsBuilder
				import androidx.navigation.NavType
				import androidx.navigation.compose.composable
				import androidx.navigation.navArgument
				import de.onecode.compass.runtime.CommonContext
				import de.onecode.compass.runtime.LocalNavHostController
				import javax.`annotation`.processing.Generated
				import kotlin.Int
				import kotlin.Unit
				
				public fun NavGraphBuilder.fooScreen(composable: @Composable fooContext.() -> Unit) {
				  composable(route = "foo", arguments = emptyList()
				  ) {
				    val current = LocalNavHostController.current
				    val context = fooContext(current, it)
				    composable(context)
				  }
				}
				
				@Generated
				public class fooContext(
				  private val navHostController: NavHostController,
				  private val navBackStackEntry: NavBackStackEntry,
				) : CommonContext(navHostController) {
				  public fun navigateToBar(param1: kotlin.Int, navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				      }
				  ) {
				    navHostController.navigate(""${'"'}bar/${'$'}{param1}""${'"'}) {
				      navOptionsBlock()
				    }
				  }
				}
				
				public fun NavGraphBuilder.barScreen(composable: @Composable barContext.() -> Unit) {
				  composable(route = "bar/{param1}", arguments = listOf(navArgument(name = "param1") {
				        type = NavType.IntType
				      }
				      )
				  ) {
				    val current = LocalNavHostController.current
				    val context = barContext(current, it)
				    composable(context)
				  }
				}
				
				@Generated
				public class barContext(
				  private val navHostController: NavHostController,
				  private val navBackStackEntry: NavBackStackEntry,
				) : CommonContext(navHostController) {
				  public val param1: kotlin.Int
				    get() = navBackStackEntry.arguments?.getInt("param1") ?:
				        error("Required parameter param1 not provided")
				}
				
				@Generated
				public fun SavedStateHandle.getParam1(): Int = get<Int>("param1") ?:
				    error("Required parameter param1 not provided")
			"""
			)
	}
}