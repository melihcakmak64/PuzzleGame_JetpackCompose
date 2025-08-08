package com.solardevtech.puzzle.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solardevtech.puzzle.data.network.RetrofitClient
import com.solardevtech.puzzle.data.repository.ImageRepository
import com.solardevtech.puzzle.model.ImageItem

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImageViewModel() : ViewModel() {
    private val repository = ImageRepository(RetrofitClient.apiService)

    private val _images = MutableStateFlow<List<ImageItem>>(emptyList())
    val images: StateFlow<List<ImageItem>> = _images

    init {
        loadImages()
    }

    private fun loadImages() {
        viewModelScope.launch {
            _images.value = repository.fetchImages()
        }
    }
}
