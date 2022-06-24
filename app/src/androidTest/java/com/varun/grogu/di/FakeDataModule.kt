package com.varun.grogu.di

import com.varun.grogu.data.FakeStarWarsRepository
import com.varun.grogu.data.di.DataModule
import com.varun.grogu.domain.repositories.StarWarsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DataModule::class])
object FakeDataModule {

    @Provides
    @Singleton
    fun provideStarWarsRepository(): StarWarsRepository {
        return FakeStarWarsRepository()
    }
}