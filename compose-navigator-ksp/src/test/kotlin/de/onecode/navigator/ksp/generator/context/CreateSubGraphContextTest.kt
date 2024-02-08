package de.onecode.navigator.ksp.generator.context

import com.google.common.truth.Truth.assertThat
import de.onecode.navigator.ksp.buildTestFile
import de.onecode.navigator.ksp.descriptions.DestinationDescription
import de.onecode.navigator.ksp.descriptions.SubGraphDescription
import org.junit.jupiter.api.Test

class CreateSubGraphContextTest {
	@Test
	fun `SubGraph Context`() {
		val description1 = DestinationDescription("foo", parameters = emptyList(), navigationTargets = emptyList(), isHome = true, isTop = false)
		val description2 = DestinationDescription("bar", parameters = emptyList(), navigationTargets = emptyList(), isHome = false, isTop = false)
		val subGraph = SubGraphDescription("sub", listOf(description1, description2))

		val code = buildTestFile {
			addType(createSubGraphContext(subGraph))
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.navigation.NavHostController
				|import de.onecode.navigator.CommonContext
				|import javax.`annotation`.processing.Generated
				|
				|@Generated
				|public abstract class subCommonContext(
                |  private val navHostController: NavHostController,
				|) : CommonContext(navHostController) {
                |  public fun leaveSubGraph() {
                |    navHostController.popBackStack(route = "sub", inclusive = true)
                |  }
				|}
				|
				""".trimMargin()
			)
	}
}