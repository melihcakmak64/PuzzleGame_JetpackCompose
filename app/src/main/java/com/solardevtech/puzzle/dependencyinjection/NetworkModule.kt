package com.solardevtech.puzzle.dependencyinjection


import com.solardevtech.puzzle.data.network.PicsumApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.jvm.java


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideQuestionAPI(): PicsumApi {
        return Retrofit.Builder()
            .baseUrl("https://picsum.photos/v2/").addConverterFactory(GsonConverterFactory.create()).build()
            .create(PicsumApi::class.java)
    }

}