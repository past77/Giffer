package com.testtask.giphy.giffer.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.testtask.giphy.giffer.data.models.DeletedItems

@Dao
interface DeletedItemsDao {
    @Insert
    suspend fun insertId(id: DeletedItems)

    @Query("SELECT * FROM deleted_items")
    suspend fun getDeletedIds(): List<DeletedItems>
}