package de.onecode.compass.ksp.generator.common

import de.onecode.compass.ksp.assertGeneratedCode
import de.onecode.compass.ksp.buildTestFile
import de.onecode.compass.ksp.descriptions.ParameterDescription
import org.junit.jupiter.api.Test

@Suppress("RedundantVisibilityModifier", "UnusedReceiverParameter")
class SavedStateHandleExtensionsTest {
	@Test
	fun `SavedStateHandle parameter extension`() {
		val code = buildTestFile {
			addFunction(createParameterExtensionOnSavedStateHandle(ParameterDescription(name = "foo", type = "String", required = true)))
		}

		assertGeneratedCode(
			generated = code,
			expected =
			"""
				import androidx.lifecycle.SavedStateHandle
				import javax.`annotation`.processing.Generated
				import kotlin.String
				
				@Generated
				public fun SavedStateHandle.getFoo(): String {
					 val arg = get<String>("foo")
					    ?: error("Required parameter foo not provided")
					return arg 
				}
			"""
		)
	}
}
