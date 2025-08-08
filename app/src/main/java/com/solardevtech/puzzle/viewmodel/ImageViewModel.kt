package com.solardevtech.puzzle.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solardevtech.puzzle.data.repository.ImageRepository
import com.solardevtech.puzzle.model.ImageItem
import com.solardevtech.puzzle.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {


    private val _images = mutableStateOf<Resource<List<ImageItem>?>>(Resource.Loading())
    val images: MutableState<Resource<List<ImageItem>?>> = _images


    init {
        loadImages()
    }

     fun loadImages() {
        _images.value=Resource.Loading()
        viewModelScope.launch {

            val result = withContext(Dispatchers.IO) {
                repository.fetchImages()
            }
            _images.value = result


        }

    }
}
