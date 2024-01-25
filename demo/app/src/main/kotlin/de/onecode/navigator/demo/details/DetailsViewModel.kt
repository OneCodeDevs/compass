package de.onecode.navigator.demo.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class DetailsViewModel(
	savedStateHandle: SavedStateHandle,
) : ViewModel() {
	val parameterInViewModel = savedStateHandle.get<Int>("myParam")
}