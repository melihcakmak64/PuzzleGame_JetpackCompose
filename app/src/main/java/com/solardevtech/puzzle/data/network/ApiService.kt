package com.solardevtech.puzzle.data.network


import com.solardevtech.puzzle.model.ImageItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface PicsumApi {
    @GET("list")
    suspend fun getImages(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<List<ImageItem>>
}
