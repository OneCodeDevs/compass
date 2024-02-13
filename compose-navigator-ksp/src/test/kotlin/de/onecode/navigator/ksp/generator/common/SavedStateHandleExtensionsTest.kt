package de.onecode.navigator.ksp.generator.common

import com.google.common.truth.Truth.assertThat
import de.onecode.navigator.ksp.buildTestFile
import de.onecode.navigator.ksp.descriptions.ParameterDescription
import org.junit.jupiter.api.Test

class SavedStateHandleExtensionsTest {
	@Test
	fun `SavedStateHandle parameter extension`() {
		val code = buildTestFile {
			addFunction(createParameterExtensionOnSavedStateHandle(ParameterDescription("foo", "String")))
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.lifecycle.SavedStateHandle
				|import javax.`annotation`.processing.Generated
				|import kotlin.String
				|
				|@Generated
				|public fun SavedStateHandle.getFoo(): String = get<String>("foo") ?:
                |    error("Required parameter foo not provided")
				|
			""".trimMargin()
			)
	}
}