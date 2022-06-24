package com.varun.grogu.data.di

import com.varun.grogu.BuildConfig
import com.varun.grogu.data.remote.Http
import com.varun.grogu.data.remote.api.StarWarsAPI
import com.varun.grogu.data.repositories.StarWarsRepositoryImpl
import com.varun.grogu.domain.repositories.StarWarsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Http.getRetrofitClient("${BuildConfig.BASE_URL}/api/")
    }

    @Provides
    @Singleton
    fun provideStarWarsRepository(retrofit: Retrofit): StarWarsRepository {
        return StarWarsRepositoryImpl(retrofit.create(StarWarsAPI::class.java))
    }
}