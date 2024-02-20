@file:Suppress("LateinitUsage")

package de.onecode.compass.runtime

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.navigation.NavHostController

lateinit var LocalNavHostController: ProvidableCompositionLocal<NavHostController>
