package de.onecode.compass.ksp.generator.context

import de.onecode.compass.ksp.assertGeneratedCode
import de.onecode.compass.ksp.buildTestFile
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.SubGraphDescription
import org.junit.jupiter.api.Test

@Suppress("RedundantVisibilityModifier", "ClassName")
class CreateSubGraphContextTest {
	@Test
	fun `SubGraph Context`() {
		val description1 = DestinationDescription("foo", parameters = emptyList(), navigationTargets = emptyList(), isHome = true, isTop = false)
		val description2 = DestinationDescription("bar", parameters = emptyList(), navigationTargets = emptyList(), isHome = false, isTop = false)
		val subGraph = SubGraphDescription("sub", listOf(description1, description2))

		val code = buildTestFile {
			addType(createSubGraphContext(subGraph))
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
				public abstract class subCommonContext(
					navHostController: NavHostController,
					navBackStackEntry: NavBackStackEntry,
				) : CommonContext(navHostController, navBackStackEntry) { 
					public fun leaveSubGraph() {
						navHostController.popBackStack(route = "sub", inclusive = true)
					} 
				}
			"""
		)
	}
}
