package de.onecode.compass.ksp.generator.context

import de.onecode.compass.ksp.assertGeneratedCode
import de.onecode.compass.ksp.buildTestFile
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.NavigationTarget
import de.onecode.compass.ksp.descriptions.ParameterDescription
import org.junit.jupiter.api.Test

@Suppress("RedundantVisibilityModifier", "RemoveRedundantQualifierName", "CanBeParameter", "ClassName")
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
				private val navHostController: NavHostController,
			    private val navBackStackEntry: NavBackStackEntry,
			) : CommonContext(navHostController)
			"""
		)
	}

	@Test
	fun `Destination Context with one parameter and no navigation`() {
		val description = DestinationDescription("foo", parameters = listOf(ParameterDescription("param1", "kotlin.String")), navigationTargets = emptyList(), isHome = false, isTop = false)

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
				private val navHostController: NavHostController,
				private val navBackStackEntry: NavBackStackEntry,
					) : CommonContext(navHostController) { 
					public val param1: kotlin.String
						get() = navBackStackEntry.arguments?.getString("param1") ?:
							error("Required parameter param1 not provided")
					}
			"""
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

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.navigation.NavBackStackEntry
				import androidx.navigation.NavHostController
				import androidx.navigation.NavOptionsBuilder
				import de.onecode.compass.CommonContext
				import javax.`annotation`.processing.Generated
				import kotlin.Unit
				
				@Generated
				public class fooContext(
				private val navHostController: NavHostController,
				private val navBackStackEntry: NavBackStackEntry,
					) : CommonContext(navHostController) { 
					public val param1: kotlin.String
						get() = navBackStackEntry.arguments?.getString("param1") ?:
							error("Required parameter param1 not provided")
				
					public fun navigateToTarget(targetParam1: kotlin.Int,
						navOptionsBlock: NavOptionsBuilder.() -> Unit =  {
						}
					) {
						navHostController.navigate(""${'"'}target/${'$'}{targetParam1}""${'"'}) {
							navOptionsBlock()
						}
					} 
				}
			"""
			)
	}
}