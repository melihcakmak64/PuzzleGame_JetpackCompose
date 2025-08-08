package com.solardevtech.puzzle.data.network


import com.solardevtech.puzzle.model.ImageItem
import retrofit2.http.GET
import retrofit2.http.Query

interface PicsumApiService {
    @GET("list")
    suspend fun getImages(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): List<ImageItem>
}
