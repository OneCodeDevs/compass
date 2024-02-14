package de.onecode.compass.ksp.generator.navigation

import com.google.common.truth.Truth.assertThat
import de.onecode.compass.ksp.buildTestFile
import org.junit.jupiter.api.Test

class CreateRememberCompassControllerTest {
	@Test
	fun `RememberCompassController function`() {
		val code = buildTestFile {
			addFunction(createRememberCompassController())
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.compose.runtime.Composable
				|import androidx.compose.runtime.remember
				|import androidx.navigation.NavHostController
				|import androidx.navigation.compose.rememberNavController
				|import de.onecode.compass.CompassController
				|import javax.`annotation`.processing.Generated
				|
				|@Generated
				|@Composable
				|public fun rememberCompassController(navController: NavHostController = rememberNavController()):
				|    CompassController = remember(key1 = navController) {
				|  CompassController(navController)
				|}
				|
				""".trimMargin()
			)
	}
}