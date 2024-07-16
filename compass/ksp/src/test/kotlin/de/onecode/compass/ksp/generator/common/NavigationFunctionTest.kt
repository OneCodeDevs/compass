package de.onecode.compass.ksp.generator.common

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import de.onecode.compass.ksp.assertGeneratedCode
import de.onecode.compass.ksp.buildTestFile
import de.onecode.compass.ksp.descriptions.NavigationTarget
import de.onecode.compass.ksp.descriptions.ParameterDescription
import de.onecode.compass.ksp.generator.navHostControllerClass
import de.onecode.compass.ksp.util.destination
import org.junit.jupiter.api.Test

@Suppress("RedundantVisibilityModifier", "RemoveRedundantQualifierName")
class NavigationFunctionTest {
	@Test
	fun `navigation function for Destination without parameters`() {
		val description = destination(name = "foo")
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
					val optionalQueryStatement = ""
					navHostController.navigate(${'"'}foo${'$'}optionalQueryStatement${'"'}) {
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
				    val optionalQueryStatement = ""
					navHostController.navigate(${'"'}foo${'$'}optionalQueryStatement${'"'}) {
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
					val optionalQueryStatement = ""
					navHostController.navigate(${'"'}foo/${'$'}{param1}${'$'}optionalQueryStatement${'"'}) {
						navOptionsBlock()
					}
				}
			"""
		)
	}

	@Test
	fun `navigation function for NavigationTarget with optional parameter`() {
		val navigationTarget = NavigationTarget("foo", parameters = listOf(ParameterDescription(name = "optional1", type = "kotlin.String", required = false)))
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

				public fun navigateToFoo(optional1: kotlin.String? = null,
				    navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				    }
				) {
				  val optionalParams = mutableListOf<String>()
				  if(optional1 != null) {
				    optionalParams.add(${'"'}optional1=${'$'}optional1${'"'})
				  }
				  val prefix = if(optionalParams.isEmpty()) "" else "?"
				  val optionalQueryStatement = optionalParams.joinToString (prefix = prefix, separator = "&"){ it }
				  navHostController.navigate(${'"'}foo${'$'}optionalQueryStatement${'"'}) {
				    navOptionsBlock()
				  }
				}
			"""
		)
	}

	@Test
	fun `navigation function for NavigationTarget with required and optional parameter`() {
		val navigationTarget = NavigationTarget(
			name = "foo",
			parameters = listOf(
				ParameterDescription(name = "param1", type = "kotlin.String", required = true),
				ParameterDescription(name = "optional1", type = "kotlin.String", required = false)
			)
		)
		val navControllerParamName = "navHostController"

		val code = buildTestFile {
			addFunction(navigationTarget.toNavigationFunction(PropertySpec.builder(navControllerParamName, navHostControllerClass, KModifier.PRIVATE).initializer(navControllerParamName).build()))
		}

		println(code)

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.navigation.NavOptionsBuilder
				import kotlin.Unit
				
				public fun navigateToFoo(
				  param1: kotlin.String,
				  optional1: kotlin.String? = null,
				  navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				      }
				  ,
				) {
				  val optionalParams = mutableListOf<String>()
				  if(optional1 != null) {
				    optionalParams.add(${'"'}optional1=${'$'}optional1${'"'})
				  }
				  val prefix = if(optionalParams.isEmpty()) "" else "?"
				  val optionalQueryStatement = optionalParams.joinToString (prefix = prefix, separator = "&"){ it }
				  navHostController.navigate(${'"'}foo/${'$'}{param1}${'$'}optionalQueryStatement${'"'}) {
				    navOptionsBlock()
				  }
				}
			"""
		)
	}
}
