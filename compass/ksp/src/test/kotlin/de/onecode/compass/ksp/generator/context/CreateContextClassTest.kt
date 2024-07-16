package de.onecode.compass.ksp.generator.context

import de.onecode.compass.ksp.assertGeneratedCode
import de.onecode.compass.ksp.buildTestFile
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.NavigationTarget
import de.onecode.compass.ksp.descriptions.ParameterDescription
import org.junit.jupiter.api.Test

@Suppress("RedundantVisibilityModifier", "RemoveRedundantQualifierName", "ClassName")
class CreateContextClassTest {
	@Test
	fun `Destination Context with no parameters and no navigation`() {
		val description = DestinationDescription("foo", parameters = emptyList(), navigationTargets = emptyList(), isHome = false, isTop = false)

		val code = buildTestFile {
			addType(createContextClass(description, "CommonContext"))
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
			import androidx.navigation.NavBackStackEntry
			import androidx.navigation.NavHostController
			import de.onecode.compass.CommonContext
			import javax.`annotation`.processing.Generated
			
			@Generated
			public class fooContext(
				navHostController: NavHostController,
			    navBackStackEntry: NavBackStackEntry,
			) : CommonContext(navHostController, navBackStackEntry)
			"""
		)
	}

	@Test
	fun `Destination Context with one parameter and no navigation`() {
		val description = DestinationDescription(
			name = "foo",
			parameters = listOf(ParameterDescription(name = "param1", type = "kotlin.String", required = true)),
			navigationTargets = emptyList(),
			isHome = false,
			isTop = false
		)

		val code = buildTestFile {
			addType(createContextClass(description, "CommonContext"))
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.navigation.NavBackStackEntry
				import androidx.navigation.NavHostController
				import de.onecode.compass.CommonContext
				import javax.`annotation`.processing.Generated
				import kotlin.String
				
				@Generated
				public class fooContext(
					navHostController: NavHostController,
					navBackStackEntry: NavBackStackEntry,
				) : CommonContext(navHostController, navBackStackEntry) { 
					public val param1: String
						get() {
							val arg = navBackStackEntry.arguments?.getString("param1") 
								?: error("Required parameter param1 not provided")
							return arg
						} 
					}
			"""
		)
	}

	@Test
	fun `Destination Context with one parameter and one navigation that also has one parameter`() {
		val description = DestinationDescription(
			"foo",
			parameters = listOf(ParameterDescription(name = "param1", type = "kotlin.String", required = true)),
			navigationTargets = listOf(NavigationTarget("target", listOf(ParameterDescription(name = "targetParam1", type = "kotlin.Int", required = true)))),
			isHome = false,
			isTop = false
		)

		val code = buildTestFile {
			addType(createContextClass(description, "CommonContext"))
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.navigation.NavBackStackEntry
				import androidx.navigation.NavHostController
				import androidx.navigation.NavOptionsBuilder
				import de.onecode.compass.CommonContext
				import javax.`annotation`.processing.Generated
				import kotlin.String
				import kotlin.Unit
				
				@Generated
				public class fooContext(
				  navHostController: NavHostController,
				  navBackStackEntry: NavBackStackEntry,
				) : CommonContext(navHostController, navBackStackEntry) {
				  public val param1: String
				    get() {
				      val arg = navBackStackEntry.arguments?.getString("param1")
				      ?: error("Required parameter param1 not provided")
				      return arg
				    }
				
				  public fun navigateToTarget(targetParam1: kotlin.Int,
				      navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
				      }
				  ) {
				    val optionalQueryStatement = "" 
				    navHostController.navigate(${'"'}target/${'$'}{targetParam1}${'$'}optionalQueryStatement${'"'}) {
				      navOptionsBlock()
				    }
				  }
				}
			"""
		)
	}
}
