package com.solardevtech.puzzle.data.repository

import com.solardevtech.puzzle.data.network.PicsumApiService
import com.solardevtech.puzzle.model.ImageItem


class ImageRepository(private val api: PicsumApiService) {
    suspend fun fetchImages(): List<ImageItem> = api.getImages()
}
