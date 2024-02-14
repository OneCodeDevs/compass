package de.onecode.compass.runtime

import androidx.navigation.NavHostController

abstract class CommonContext(
  private val navHostController: NavHostController,
) {
  fun popBackStack() {
    navHostController.popBackStack()
  }
}
