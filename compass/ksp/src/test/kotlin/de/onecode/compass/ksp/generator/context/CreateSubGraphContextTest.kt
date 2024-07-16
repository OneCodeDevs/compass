package de.onecode.compass.ksp.generator.context

import de.onecode.compass.ksp.assertGeneratedCode
import de.onecode.compass.ksp.buildTestFile
import de.onecode.compass.ksp.descriptions.SubGraphDescription
import de.onecode.compass.ksp.util.destination
import org.junit.jupiter.api.Test

@Suppress("RedundantVisibilityModifier", "ClassName")
class CreateSubGraphContextTest {
	@Test
	fun `SubGraph Context`() {
		val description1 = destination("foo", isHome = true)
		val description2 = destination("bar")
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
