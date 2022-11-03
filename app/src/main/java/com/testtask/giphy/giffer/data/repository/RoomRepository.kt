package com.testtask.giphy.giffer.data.repository

import com.testtask.giphy.giffer.data.models.DeletedItems

interface RoomRepository {
    suspend fun deleteGitById(id: String?)
    suspend fun addIdToDeletedItems(id: DeletedItems)
}