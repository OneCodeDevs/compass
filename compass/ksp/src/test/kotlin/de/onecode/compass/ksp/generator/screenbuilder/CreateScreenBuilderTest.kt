package de.onecode.compass.ksp.generator.screenbuilder

import de.onecode.compass.ksp.assertGeneratedCode
import de.onecode.compass.ksp.buildTestFile
import de.onecode.compass.ksp.descriptions.DestinationDescription
import org.junit.jupiter.api.Test

@Suppress("RedundantVisibilityModifier")
class CreateScreenBuilderTest {
	@Test
	fun `ScreenBuilder interface`() {
		val description1 = DestinationDescription("foo", parameters = emptyList(), navigationTargets = emptyList(), isHome = true, isTop = false)
		val description2 = DestinationDescription("bar", parameters = emptyList(), navigationTargets = emptyList(), isHome = false, isTop = false)

		val code = buildTestFile {
			addType(createScreenBuilderInterface(listOf(description1, description2)))
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
				public interface ScreenBuilder {
				  public fun fooScreen(composable: @Composable fooContext.() -> Unit)
				
				  public fun barScreen(composable: @Composable barContext.() -> Unit)
				}
			"""
		)
	}

	@Test
	fun `ScreenBuilder implementation`() {
		val description1 = DestinationDescription("foo", parameters = emptyList(), navigationTargets = emptyList(), isHome = true, isTop = false)
		val description2 = DestinationDescription("bar", parameters = emptyList(), navigationTargets = emptyList(), isHome = false, isTop = false)

		val code = buildTestFile {
			addType(createScreenBuilderImplementation(listOf(description1, description2)))
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.compose.runtime.Composable
				import de.onecode.compass.ScreenBuilder
				import de.onecode.compass.barContext
				import de.onecode.compass.fooContext
				import javax.`annotation`.processing.Generated
				import kotlin.Unit
				
				@Generated
				private class ScreenBuilderImpl : ScreenBuilder {
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