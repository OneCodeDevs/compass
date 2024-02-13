package de.onecode.navigator.ksp.generator.navigation

import com.google.common.truth.Truth.assertThat
import de.onecode.navigator.ksp.buildTestFile
import org.junit.jupiter.api.Test

class CreateRememberNavigatorControllerTest {
	@Test
	fun `RememberNavigatorController function`() {
		val code = buildTestFile {
			addFunction(createRememberNavigatorController())
		}

		assertThat(code)
			.isEqualTo(
				"""
				|import androidx.compose.runtime.Composable
				|import androidx.compose.runtime.remember
				|import androidx.navigation.NavHostController
				|import androidx.navigation.compose.rememberNavController
				|import de.onecode.navigator.NavigatorController
				|import javax.`annotation`.processing.Generated
				|
				|@Generated
				|@Composable
				|public fun rememberNavigatorController(navController: NavHostController = rememberNavController()):
				|    NavigatorController = remember(key1 = navController) {
				|  NavigatorController(navController)
				|}
				|
				""".trimMargin()
			)
	}
}