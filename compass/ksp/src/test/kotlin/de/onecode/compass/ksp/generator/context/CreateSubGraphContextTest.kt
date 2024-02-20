package de.onecode.compass.ksp.generator.context

import de.onecode.compass.ksp.assertGeneratedCode
import de.onecode.compass.ksp.buildTestFile
import de.onecode.compass.ksp.generator.common.destinationDescription
import de.onecode.compass.ksp.generator.common.subGraphDescription
import org.junit.jupiter.api.Test

@Suppress("RedundantVisibilityModifier", "ClassName")
class CreateSubGraphContextTest {
	@Test
	fun `SubGraph Context`() {
		val description1 = destinationDescription(
			name = "foo",
			isHome = true,
		)
		val description2 = destinationDescription(
			name = "bar",
		)
		val subGraph = subGraphDescription("sub", description1, description2)

		val code = buildTestFile {
			addType(createSubGraphContext(subGraph))
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.navigation.NavHostController
				import de.onecode.compass.CommonContext
				import javax.`annotation`.processing.Generated
				
				@Generated
				public abstract class subCommonContext(
				private val navHostController: NavHostController,
					) : CommonContext(navHostController) { 
					public fun leaveSubGraph() {
						navHostController.popBackStack(route = "sub", inclusive = true)
					} 
				}
			"""
		)
	}
}
