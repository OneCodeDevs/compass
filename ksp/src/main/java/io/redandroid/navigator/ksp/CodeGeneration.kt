package io.redandroid.navigator.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import io.redandroid.navigator.api.Destination
import java.util.*

private const val PACKAGE = "io.redandroid.navigator"
private const val NAVIGATOR_COMPOSABLE_NAME = "Navigator"

fun CodeGenerator.generateCode(destinations: List<DestinationDescription>, dependencies: Dependencies) {
	val home = destinations.firstOrNull { it.isHome }?.name ?: error("Couldn't find a ${Destination::class.simpleName} marked as home")

	createNewFile(
		dependencies = dependencies,
		packageName = PACKAGE,
		fileName = NAVIGATOR_COMPOSABLE_NAME
	).use { navigatorFile ->
		navigatorFile += """
package $PACKAGE

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController

private val startDestination: String = "$home"

@Composable
fun $NAVIGATOR_COMPOSABLE_NAME(
	navHostController: NavHostController = rememberNavController(),
	builder: ScreenBuilder.() -> Unit
) {
	NavHost(navController = navHostController, startDestination = startDestination) {
		val screenBuilder = ScreenBuilder()
		screenBuilder.builder()
		
${
			destinations.joinToString(separator = "\n") { destination ->
				val params = destination.parameters.joinToString("/") { "{${it.name}}" }
				val paramSuffix = if (params.isNotBlank()) "/$params" else ""
				val destinationScreenName = destination.name.replaceFirstChar { it.lowercase(Locale.getDefault()) }

				listOf(
					"\t\tcomposable(route = \"${destination.name}$paramSuffix\") {",
					"\t\t\tscreenBuilder.${destinationScreenName}Composable?.invoke(${destination.name}Context(navHostController, it))",
					"\t\t}"
				).joinToString(separator = "\n")
			}
		}
	}
}
		
class ScreenBuilder {
${
			destinations.joinToString(separator = "\n") { destination ->
				val destinationScreenName = destination.name.replaceFirstChar { it.lowercase(Locale.getDefault()) }
				val composableProperty = "${destinationScreenName}Composable"
				listOf(
					"\tinternal var $composableProperty: (@Composable ${destination.name}Context.() -> Unit)? = null",
					"\tfun ${destinationScreenName}Screen(composable: @Composable ${destination.name}Context.() -> Unit) {",
					"\t\t$composableProperty = composable",
					"\t}"
				).joinToString(separator = "\n")
			}
		}
}
		
${
			destinations.joinToString(separator = "\n") { destination ->
				val params = destination.parameters.joinToString(separator = "\n") { parameter ->
					listOf(
						"\tval ${parameter.name}: ${parameter.type}",
						"\t\tget() = navBackStackEntry.arguments?.getString(\"${parameter.name}\")?.to${parameter.type.typeString()}OrNull() ?: error(\"Required parameter ${parameter.name} not provided\")"
					).joinToString(separator = "\n")
				}

				val navigations = destination.navigationTargets.joinToString(separator = "\n") { navigation ->
					val parameterDefinition = navigation.parameters.joinToString { "${it.name}: ${it.type}" }
					val paramsRoute = navigation.parameters.joinToString(separator = "/") { "\${${it.name}}" }
					val paramsRouteWithSlash = if (paramsRoute.isNotBlank()) "/$paramsRoute" else ""

					listOf(
						"\tfun navigateTo${navigation.name}($parameterDefinition) {",
						"\t\tnavHostController.navigate(\"${navigation.name}$paramsRouteWithSlash\")",
						"\t}"
					).joinToString(separator = "\n")
				}


				listOf(
					"class ${destination.name}Context(private val navHostController: NavHostController, private val navBackStackEntry: NavBackStackEntry) {",
					params,
					navigations,
					"}"
				).joinToString(separator = "\n")
			}
		}
	""".trimIndent()
	}
}