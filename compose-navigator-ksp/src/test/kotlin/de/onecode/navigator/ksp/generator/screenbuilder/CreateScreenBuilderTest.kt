package de.onecode.navigator.ksp.generator.screenbuilder

import com.google.common.truth.Truth.assertThat
import de.onecode.navigator.ksp.buildTestFile
import de.onecode.navigator.ksp.descriptions.DestinationDescription
import org.junit.jupiter.api.Test

class CreateScreenBuilderTest {
	@Test
	fun `ScreenBuilder interface`() {
		val description1 = DestinationDescription("foo", parameters = emptyList(), navigationTargets = emptyList(), isHome = true, isTop = false)
		val description2 = DestinationDescription("bar", parameters = emptyList(), navigationTargets = emptyList(), isHome = false, isTop = false)

		val code = buildTestFile {
			addType(createScreenBuilderInterface(listOf(description1, description2)))
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.compose.runtime.Composable
				|import de.onecode.navigator.barContext
				|import de.onecode.navigator.fooContext
				|import javax.`annotation`.processing.Generated
				|import kotlin.Unit
				|
				|@Generated
				|public interface ScreenBuilder {
				|  public fun fooScreen(composable: @Composable fooContext.() -> Unit)
				|
				|  public fun barScreen(composable: @Composable barContext.() -> Unit)
				|}
				|
				""".trimMargin()
			)
	}

	@Test
	fun `ScreenBuilder implementation`() {
		val description1 = DestinationDescription("foo", parameters = emptyList(), navigationTargets = emptyList(), isHome = true, isTop = false)
		val description2 = DestinationDescription("bar", parameters = emptyList(), navigationTargets = emptyList(), isHome = false, isTop = false)

		val code = buildTestFile {
			addType(createScreenBuilderImplementation(listOf(description1, description2)))
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.compose.runtime.Composable
				|import de.onecode.navigator.ScreenBuilder
				|import de.onecode.navigator.barContext
				|import de.onecode.navigator.fooContext
				|import javax.`annotation`.processing.Generated
				|import kotlin.Unit
				|
				|@Generated
				|private class ScreenBuilderImpl : ScreenBuilder {
				|  internal var fooComposable: @Composable (fooContext.() -> Unit)? = null
				|
				|  internal var barComposable: @Composable (barContext.() -> Unit)? = null
				|
				|  override fun fooScreen(composable: @Composable fooContext.() -> Unit) {
				|    fooComposable = composable
				|  }
				|
				|  override fun barScreen(composable: @Composable barContext.() -> Unit) {
				|    barComposable = composable
				|  }
				|}
				|
				""".trimMargin()
			)
	}
}