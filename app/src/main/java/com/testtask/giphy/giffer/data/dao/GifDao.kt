package com.testtask.giphy.giffer.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import com.testtask.giphy.giffer.models.ImageData


@Dao
interface GifDao {


    @Insert(onConflict = REPLACE)
    suspend fun insertImage(image: ImageData)

    @Insert(onConflict = REPLACE)
    suspend fun insertAllImages(images: List<ImageData>)

//    @Query("SELECT * FROM image_table")
//    fun getAllImages(): LiveData<List<ImageData>>
//
//    @Query("DELETE FROM image_table")
//    suspend fun clearTable()
}