package io.redandroid.navigator.ksp.generator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName

internal const val PACKAGE = "io.redandroid.navigator"
internal const val NAVIGATOR_COMPOSABLE_NAME = "Navigator"
internal const val SCREEN_BUILDER = "ScreenBuilder"
internal const val COMMON_CONTEXT = "CommonContext"
internal const val LOCAL_NAV_HOST_CONTROLLER = "LocalNavHostController"

internal val screenBuilderClass = ClassName(PACKAGE, SCREEN_BUILDER)
internal val composableClass = ClassName("androidx.compose.runtime", "Composable")
internal val composeAnnotation = AnnotationSpec.builder(composableClass).build()
internal val navHostControllerClass = ClassName("androidx.navigation", "NavHostController")
internal val navBackStackEntryClass = ClassName("androidx.navigation", "NavBackStackEntry")
internal val navGraphBuilderClass = ClassName("androidx.navigation", "NavGraphBuilder")
internal val rememberNavControllerName = MemberName("androidx.navigation.compose", "rememberNavController")