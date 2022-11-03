package com.testtask.giphy.giffer.data.repository

import com.testtask.giphy.giffer.data.dao.DeletedItemsDao
import com.testtask.giphy.giffer.data.dao.GifDao
import com.testtask.giphy.giffer.data.models.DeletedItems
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val gifDao: GifDao,
    private val deletedItemsDao: DeletedItemsDao
) : RoomRepository {

    override suspend fun deleteGitById(id: String?) {
        gifDao.deleteGifById(id)
    }

    override suspend fun addIdToDeletedItems(id: DeletedItems) {
        deletedItemsDao.insertId(id)
    }
}