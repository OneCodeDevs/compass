package io.redandroid.navigator.runtime

import androidx.navigation.NavHostController

abstract class CommonContext(
  private val navHostController: NavHostController,
) {
  fun popBackStack() {
    navHostController.popBackStack()
  }
}
