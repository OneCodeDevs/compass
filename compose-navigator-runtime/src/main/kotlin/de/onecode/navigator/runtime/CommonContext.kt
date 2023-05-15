package de.onecode.navigator.runtime

import androidx.navigation.NavHostController

abstract class CommonContext(
  private val navHostController: NavHostController,
) {
  fun popBackStack() {
    navHostController.popBackStack()
  }
}
