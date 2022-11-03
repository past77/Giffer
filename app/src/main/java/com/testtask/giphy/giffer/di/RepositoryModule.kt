package com.testtask.giphy.giffer.di

import com.testtask.giphy.giffer.data.repository.GiphyRepository
import com.testtask.giphy.giffer.data.repository.GiphyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindRemoteRepository(repository: GiphyRepositoryImpl): GiphyRepository

//    @Binds
//    @ViewModelScoped
//    abstract fun bindImageRoomRepository(imageRoomRepository: ImageRoomRepositoryImpl): ImageRoomRepository
}
