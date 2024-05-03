package de.onecode.compass.ksp.generator.common

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import de.onecode.compass.ksp.assertGeneratedCode
import de.onecode.compass.ksp.buildTestFile
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.NavigationTarget
import de.onecode.compass.ksp.descriptions.ParameterDescription
import de.onecode.compass.ksp.generator.navHostControllerClass
import org.junit.jupiter.api.Test

@Suppress("RedundantVisibilityModifier", "RemoveRedundantQualifierName")
class NavigationFunctionTest {
	@Test
	fun `navigation function for Destination without parameters`() {
		val description = DestinationDescription(name = "foo", parameters = emptyList(), navigationTargets = emptyList(), isHome = false, isTop = false)
		val navControllerParamName = "navHostController"

		val code = buildTestFile {
			addFunction(
				description.toNavigationFunction(
					PropertySpec
						.builder(navControllerParamName, navHostControllerClass, KModifier.PRIVATE)
						.initializer(navControllerParamName)
						.build()
				)
			)
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.navigation.NavOptionsBuilder
				import kotlin.Unit
				
				public fun navigateToFoo(navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
					}
				) {
					navHostController.navigate(""${'"'}foo""${'"'}) {
						navOptionsBlock()
					}
				}
			"""
		)
	}

	@Test
	fun `navigation function for NavigationTarget without parameters`() {
		val navigationTarget = NavigationTarget("foo", parameters = emptyList())
		val navControllerParamName = "navHostController"

		val code = buildTestFile {
			addFunction(navigationTarget.toNavigationFunction(PropertySpec.builder(navControllerParamName, navHostControllerClass, KModifier.PRIVATE).initializer(navControllerParamName).build()))
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.navigation.NavOptionsBuilder
				import kotlin.Unit
				
				public fun navigateToFoo(navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
					}
				) {
					navHostController.navigate(""${'"'}foo""${'"'}) {
						navOptionsBlock()
					}
				}
			"""
		)
	}

	@Test
	fun `navigation function for NavigationTarget with parameters`() {
		val navigationTarget = NavigationTarget("foo", parameters = listOf(ParameterDescription(name = "param1", type = "kotlin.String", required = true)))
		val navControllerParamName = "navHostController"

		val code = buildTestFile {
			addFunction(navigationTarget.toNavigationFunction(PropertySpec.builder(navControllerParamName, navHostControllerClass, KModifier.PRIVATE).initializer(navControllerParamName).build()))
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.navigation.NavOptionsBuilder
				import kotlin.Unit
				
				public fun navigateToFoo(param1: kotlin.String, navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
					}
				) {
					navHostController.navigate(""${'"'}foo/${'$'}{param1}""${'"'}) {
						navOptionsBlock()
					}
				}
			"""
		)
	}
}
