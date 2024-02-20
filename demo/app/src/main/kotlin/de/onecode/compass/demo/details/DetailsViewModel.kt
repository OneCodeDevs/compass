package de.onecode.compass.demo.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import de.onecode.compass.getMyParam

class DetailsViewModel(
	savedStateHandle: SavedStateHandle,
) : ViewModel() {
	val parameterInViewModel = savedStateHandle.getMyParam()
}
