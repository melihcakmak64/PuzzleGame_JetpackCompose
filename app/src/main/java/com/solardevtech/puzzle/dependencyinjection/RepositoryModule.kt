package com.solardevtech.puzzle.dependencyinjection

import com.solardevtech.puzzle.data.network.PicsumApi
import com.solardevtech.puzzle.data.repository.ImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideQuestionRepository(questionAPI: PicsumApi) = ImageRepository(questionAPI)
}