package com.testtask.giphy.giffer.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.testtask.giphy.giffer.data.models.DeletedItems
import com.testtask.giphy.giffer.data.models.ImageData


@Dao
interface GifDao {


    @Insert(onConflict = REPLACE)
    suspend fun insertImage(image: ImageData)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(images: List<ImageData>)

    @Query("SELECT * FROM image_table")
    suspend fun getAllImages(): List<ImageData>

    @Query("SELECT * FROM image_table")
    fun getAllImageModel(): PagingSource<Int, ImageData>

    @Query("DELETE FROM image_table")
    suspend fun clearAllGifs()

    @Transaction
    suspend fun modifyList(images: List<ImageData>, deletedItems: List<DeletedItems>) {
        insertAll(images)
        clearEarlyDeletedData(deletedItems.map { it.deleteId })
    }

    @Query("delete from image_table where id in (:idList)")
    fun clearEarlyDeletedData(idList: List<String>)

    @Query("DELETE FROM image_table WHERE id = :id")
    suspend fun deleteGifById(id: String?)

    @Query("SELECT * FROM image_table")
    fun pagingSource(): PagingSource<Int, ImageData>
}