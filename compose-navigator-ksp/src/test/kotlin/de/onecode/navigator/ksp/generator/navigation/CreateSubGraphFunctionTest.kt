package de.onecode.navigator.ksp.generator.navigation

import com.google.common.truth.Truth.assertThat
import de.onecode.navigator.ksp.buildTestFile
import de.onecode.navigator.ksp.descriptions.DestinationDescription
import de.onecode.navigator.ksp.descriptions.NavigationTarget
import de.onecode.navigator.ksp.descriptions.ParameterDescription
import de.onecode.navigator.ksp.descriptions.SubGraphDescription
import org.junit.jupiter.api.Test

class CreateSubGraphFunctionTest {
	@Test
	fun `SuGraph function with two destinations`() {
		val param1 = ParameterDescription("param1", "kotlin.Int")
		val description1 = DestinationDescription("foo", parameters = emptyList(), navigationTargets = listOf(NavigationTarget("bar", listOf(param1))), isHome = true, isTop = false)
		val description2 = DestinationDescription("bar", parameters = listOf(param1), navigationTargets = emptyList(), isHome = false, isTop = false)
		val subGraph = SubGraphDescription("sub", listOf(description1, description2))

		val code = buildTestFile {
			addFunction(createSubGraphFunction(subGraph))
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.navigation.NavGraphBuilder
				|import de.onecode.navigator.subScreenBuilder
				|import javax.`annotation`.processing.Generated
				|import kotlin.Unit
				|
				|@Generated
				|public fun NavGraphBuilder.subSubGraph(builder: subScreenBuilder.(NavGraphBuilder) -> Unit) {
				|  navigation(startDestination = "foo", route = "sub") {
				|    val screenBuilder = subScreenBuilderImpl()
				|    screenBuilder.builder(this)
				|    composable(route = "foo", arguments = emptyList()
				|    ) {
				|      screenBuilder.fooComposable?.invoke(fooContext(LocalNavHostController.current, it))
				|    }
				|    composable(route = "bar/{param1}", arguments = listOf(navArgument(name = "param1") {
				|          type = NavType.IntType
				|        }
				|        )
				|    ) {
				|      screenBuilder.barComposable?.invoke(barContext(LocalNavHostController.current, it))
				|    }
				|  }
				|}
				|
				""".trimMargin()
			)
	}

	@Test
	fun `SuGraph function with a home with parameters`() {
		val param1 = ParameterDescription("param1", "kotlin.Int")
		val description = DestinationDescription("foo", parameters = listOf(param1), navigationTargets = emptyList(), isHome = true, isTop = false)
		val subGraph = SubGraphDescription("sub", listOf(description))

		val code = buildTestFile {
			addFunction(createSubGraphFunction(subGraph))
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.navigation.NavGraphBuilder
				|import de.onecode.navigator.subScreenBuilder
				|import javax.`annotation`.processing.Generated
				|import kotlin.Unit
				|
				|@Generated
				|public fun NavGraphBuilder.subSubGraph(builder: subScreenBuilder.(NavGraphBuilder) -> Unit) {
				|  navigation(startDestination = "foo/{param1}", route = "sub/{param1}") {
				|    val screenBuilder = subScreenBuilderImpl()
				|    screenBuilder.builder(this)
				|    composable(route = "foo/{param1}", arguments = listOf(navArgument(name = "param1") {
                |          type = NavType.IntType
                |        }
                |        )
				|    ) {
				|      screenBuilder.fooComposable?.invoke(fooContext(LocalNavHostController.current, it))
				|    }
				|  }
				|}
				|
				""".trimMargin()
			)
	}
}