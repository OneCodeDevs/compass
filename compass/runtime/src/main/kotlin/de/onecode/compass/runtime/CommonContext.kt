package de.onecode.compass.runtime

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

abstract class CommonContext(
  protected val navHostController: NavHostController,
  protected val navBackStackEntry: NavBackStackEntry,
) {
  val savedStateHandle: SavedStateHandle
    get() = navBackStackEntry.savedStateHandle

  fun setResult(name: String, value: Any?) {
    navHostController.previousBackStackEntry?.savedStateHandle?.set(name, value)
  }

  fun popBackStack() {
    navHostController.popBackStack()
  }
}
