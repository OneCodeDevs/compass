package de.onecode.navigator.demo.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import de.onecode.navigator.getMyParam

class DetailsViewModel(
	savedStateHandle: SavedStateHandle,
) : ViewModel() {
	val parameterInViewModel = savedStateHandle.getMyParam()
}