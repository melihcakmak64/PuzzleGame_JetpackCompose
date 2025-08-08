package com.solardevtech.puzzle.data.repository

import com.solardevtech.puzzle.data.network.PicsumApi
import com.solardevtech.puzzle.model.ImageItem
import javax.inject.Inject


class ImageRepository @Inject constructor(private val api: PicsumApi) {
    suspend fun fetchImages(): List<ImageItem> = api.getImages()
}
