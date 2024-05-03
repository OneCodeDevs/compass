package de.onecode.compass.ksp.generator.navigation

import de.onecode.compass.ksp.assertGeneratedCode
import de.onecode.compass.ksp.buildTestFile
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.NavigationTarget
import de.onecode.compass.ksp.descriptions.ParameterDescription
import de.onecode.compass.ksp.descriptions.SubGraphDescription
import org.junit.jupiter.api.Test

@Suppress("RedundantVisibilityModifier")
class CreateSubGraphFunctionTest {
	@Test
	fun `SuGraph function with two destinations`() {
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
		val subGraph = SubGraphDescription(
			name = "sub",
			destinations = listOf(description1, description2)
		)

		val code = buildTestFile {
			addFunction(createSubGraphFunction(subGraph))
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.navigation.NavGraphBuilder
				import de.onecode.compass.subScreenBuilder
				import javax.`annotation`.processing.Generated
				import kotlin.Unit
				
				@Generated
				public fun NavGraphBuilder.subSubGraph(builder: subScreenBuilder.(NavGraphBuilder) -> Unit) {
				  navigation(startDestination = "foo", route = "sub") {
				    val screenBuilder = subScreenBuilderImpl()
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
			"""
		)
	}

	@Test
	fun `SuGraph function with a home with parameters`() {
		val param1 = ParameterDescription(name = "param1", type = "kotlin.Int", required = true)
		val description = DestinationDescription("foo", parameters = listOf(param1), navigationTargets = emptyList(), isHome = true, isTop = false)
		val subGraph = SubGraphDescription("sub", listOf(description))

		val code = buildTestFile {
			addFunction(createSubGraphFunction(subGraph))
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.navigation.NavGraphBuilder
				import de.onecode.compass.subScreenBuilder
				import javax.`annotation`.processing.Generated
				import kotlin.Unit
				
				@Generated
				public fun NavGraphBuilder.subSubGraph(builder: subScreenBuilder.(NavGraphBuilder) -> Unit) {
				  navigation(startDestination = "foo/{param1}", route = "sub/{param1}") {
				    val screenBuilder = subScreenBuilderImpl()
				    screenBuilder.builder(this)
				    composable(route = "foo/{param1}", arguments = listOf(navArgument(name = "param1") {
								type = NavType.IntType
							}
						)
				    ) {
				      screenBuilder.fooComposable?.invoke(fooContext(LocalNavHostController.current, it))
				    }
				  }
				}
			"""
		)
	}
}
