package de.onecode.compass.ksp.generator.screenbuilder

import de.onecode.compass.ksp.assertGeneratedCode
import de.onecode.compass.ksp.buildTestFile
import de.onecode.compass.ksp.descriptions.DestinationDescription
import de.onecode.compass.ksp.descriptions.SubGraphDescription
import org.junit.jupiter.api.Test

@Suppress("RedundantVisibilityModifier", "ClassName")
class CreateSubGraphBuilderTest {
	@Test
	fun `SubGraphBuilder interface`() {
		val description1 = DestinationDescription(name = "foo", parameters = emptyList(), navigationTargets = emptyList(), isHome = true, isTop = false)
		val description2 = DestinationDescription(name = "bar", parameters = emptyList(), navigationTargets = emptyList(), isHome = false, isTop = false)
		val subGraph = SubGraphDescription(name = "sub", listOf(description1, description2))

		val code = buildTestFile {
			addType(createSubGraphBuilderInterface(subGraph))
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.compose.runtime.Composable
				import de.onecode.compass.barContext
				import de.onecode.compass.fooContext
				import javax.`annotation`.processing.Generated
				import kotlin.Unit
				
				@Generated
				public interface subScreenBuilder {
				  public fun fooScreen(composable: @Composable fooContext.() -> Unit)
				
				  public fun barScreen(composable: @Composable barContext.() -> Unit)
				}
			"""
		)
	}

	@Test
	fun `ScreenBuilder implementation`() {
		val description1 = DestinationDescription(name = "foo", parameters = emptyList(), navigationTargets = emptyList(), isHome = true, isTop = false)
		val description2 = DestinationDescription(name = "bar", parameters = emptyList(), navigationTargets = emptyList(), isHome = false, isTop = false)
		val subGraph = SubGraphDescription(name = "sub", listOf(description1, description2))

		val code = buildTestFile {
			addType(createSubGraphBuilderImplementation(subGraph))
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.compose.runtime.Composable
				import de.onecode.compass.barContext
				import de.onecode.compass.fooContext
				import de.onecode.compass.subScreenBuilder
				import javax.`annotation`.processing.Generated
				import kotlin.Unit
				
				@Generated
				private class subScreenBuilderImpl : subScreenBuilder {
				  internal var fooComposable: @Composable (fooContext.() -> Unit)? = null
				
				  internal var barComposable: @Composable (barContext.() -> Unit)? = null
				
				  override fun fooScreen(composable: @Composable fooContext.() -> Unit) {
				    fooComposable = composable
				  }
				
				  override fun barScreen(composable: @Composable barContext.() -> Unit) {
				    barComposable = composable
				  }
				}
			"""
		)
	}
}
