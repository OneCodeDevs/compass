package de.onecode.navigator.ksp.generator.context

import com.google.common.truth.Truth.assertThat
import de.onecode.navigator.ksp.buildTestFile
import de.onecode.navigator.ksp.descriptions.DestinationDescription
import de.onecode.navigator.ksp.descriptions.NavigationTarget
import de.onecode.navigator.ksp.descriptions.ParameterDescription
import org.junit.jupiter.api.Test

class CreateContextClassTest {
	@Test
	fun `Destination Context with no parameters and no navigation`() {
		val description = DestinationDescription("foo", parameters = emptyList(), navigationTargets = emptyList(), isHome = false, isTop = false)

		val code = buildTestFile {
			addType(createContextClass(description, "CommonContext"))
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.navigation.NavBackStackEntry
				|import androidx.navigation.NavHostController
				|import de.onecode.navigator.CommonContext
				|import javax.`annotation`.processing.Generated
				|
				|@Generated
				|public class fooContext(
                |  private val navHostController: NavHostController,
                |  private val navBackStackEntry: NavBackStackEntry,
				|) : CommonContext(navHostController)
				|
				""".trimMargin()
			)
	}

	@Test
	fun `Destination Context with one parameter and no navigation`() {
		val description = DestinationDescription("foo", parameters = listOf(ParameterDescription("param1", "kotlin.String")), navigationTargets = emptyList(), isHome = false, isTop = false)

		val code = buildTestFile {
			addType(createContextClass(description, "CommonContext"))
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.navigation.NavBackStackEntry
				|import androidx.navigation.NavHostController
				|import de.onecode.navigator.CommonContext
				|import javax.`annotation`.processing.Generated
				|
				|@Generated
				|public class fooContext(
                |  private val navHostController: NavHostController,
                |  private val navBackStackEntry: NavBackStackEntry,
				|) : CommonContext(navHostController) {
                |  public val param1: kotlin.String
                |    get() = navBackStackEntry.arguments?.getString("param1") ?:
                |        error("Required parameter param1 not provided")
				|}
				|
				""".trimMargin()
			)
	}

	@Test
	fun `Destination Context with one parameter and one navigation that also has one parameter`() {
		val description = DestinationDescription(
			"foo",
			parameters = listOf(ParameterDescription("param1", "kotlin.String")),
			navigationTargets = listOf(NavigationTarget("target", listOf(ParameterDescription("targetParam1", "kotlin.Int")))),
			isHome = false,
			isTop = false
		)

		val code = buildTestFile {
			addType(createContextClass(description, "CommonContext"))
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.navigation.NavBackStackEntry
				|import androidx.navigation.NavHostController
				|import androidx.navigation.NavOptionsBuilder
				|import de.onecode.navigator.CommonContext
				|import javax.`annotation`.processing.Generated
				|import kotlin.Unit
				|
				|@Generated
				|public class fooContext(
                |  private val navHostController: NavHostController,
                |  private val navBackStackEntry: NavBackStackEntry,
				|) : CommonContext(navHostController) {
                |  public val param1: kotlin.String
                |    get() = navBackStackEntry.arguments?.getString("param1") ?:
                |        error("Required parameter param1 not provided")
				|
				|  public fun navigateToTarget(targetParam1: kotlin.Int,
                |      navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
                |      }
                |  ) {
                |    navHostController.navigate(""${'"'}target/${'$'}{targetParam1}""${'"'}) {
                |      navOptionsBlock()
                |    }
                |  }
				|}
				|
				""".trimMargin()
			)
	}
}