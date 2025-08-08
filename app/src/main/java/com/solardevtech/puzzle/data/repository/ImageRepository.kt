package com.solardevtech.puzzle.data.repository

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.solardevtech.puzzle.data.network.PicsumApi
import com.solardevtech.puzzle.model.ImageItem
import com.solardevtech.puzzle.util.Resource
import javax.inject.Inject

class ImageRepository @Inject constructor(private val api: PicsumApi
) {
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

    suspend fun loadBitmapFromUrl(context: Context, url: String): Bitmap? {
        return    try {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false)
                .build()
            val result = loader.execute(request)
            if (result is SuccessResult) {
                result.drawable.toBitmap()
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

}