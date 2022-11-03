package com.testtask.giphy.giffer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.testtask.giphy.giffer.data.dao.DeletedItemsDao
import com.testtask.giphy.giffer.data.dao.GifDao
import com.testtask.giphy.giffer.data.dao.RemoteKeysDao
import com.testtask.giphy.giffer.data.models.DeletedItems
import com.testtask.giphy.giffer.data.models.ImageData
import com.testtask.giphy.giffer.data.models.RemoteKeys

@Database(entities = [ImageData::class, RemoteKeys::class, DeletedItems::class], version = 15)
abstract class GifDatabase: RoomDatabase() {

    abstract fun gifDao(): GifDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun deletedItemsDao(): DeletedItemsDao
}