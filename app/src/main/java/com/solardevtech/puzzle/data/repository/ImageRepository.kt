package com.solardevtech.puzzle.data.repository

import com.solardevtech.puzzle.data.network.PicsumApi
import com.solardevtech.puzzle.model.ImageItem
import com.solardevtech.puzzle.util.Resource
import javax.inject.Inject


class ImageRepository @Inject constructor(private val api: PicsumApi) {
    suspend fun fetchImages(): Resource<List<ImageItem>?> {

        return try {
            val response = api.getImages()
            if (response.isSuccessful) {
                Resource.Success(data = response.body())
            } else {
                Resource.Error(message = "Network Response Failed!")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Unknown Error")
        }
    }
}
