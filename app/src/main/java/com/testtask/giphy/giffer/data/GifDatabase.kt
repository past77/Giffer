package com.testtask.giphy.giffer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.testtask.giphy.giffer.data.dao.GifDao
import com.testtask.giphy.giffer.models.ImageData

@Database(entities = [ImageData::class], version = 1)
abstract class GifDatabase: RoomDatabase() {

    abstract fun gifDao(): GifDao
}