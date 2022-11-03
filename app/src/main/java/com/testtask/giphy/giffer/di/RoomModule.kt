package com.testtask.giphy.giffer.di

import android.content.Context
import androidx.room.Room
import com.testtask.giphy.giffer.data.dao.GifDao
import com.testtask.giphy.giffer.data.db.GifDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): GifDatabase {
        return Room.databaseBuilder(
            appContext,
            GifDatabase::class.java,
            "gif-db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesProductDao(imageDatabase: GifDatabase): GifDao {
        return imageDatabase.gifDao()
    }

}