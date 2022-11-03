package com.testtask.giphy.giffer.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.testtask.giphy.giffer.data.api.GiphyApi
import com.testtask.giphy.giffer.data.db.GifDatabase
import com.testtask.giphy.giffer.data.models.ImageData
import com.testtask.giphy.giffer.data.models.RemoteKeys
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class GiphyMediator(
    private val api: GiphyApi,
    private val query: String,
    private val database: GifDatabase
) : RemoteMediator<Int, ImageData>() {

    private val DEFAULT_PAGE_INDEX = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageData>
    ): MediatorResult {

        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }
        try {
            val response = api.searchImages(
                query = query,
                offset = page * 20,
                limit = state.config.pageSize
            )
            val isEndOfList = response.data.isEmpty()
           database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                   database.remoteKeysDao().clearRemoteKeys()
                    database.gifDao().clearAllGifs()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
               val keys = response.data.map {
                   RemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
               }
               val deletedItms = database.deletedItemsDao().getDeletedIds()
               database.remoteKeysDao().insertAll(keys)
               database.gifDao().modifyList(response.data, deletedItms)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }


    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, ImageData>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                remoteKeys?.nextKey ?: 0
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, ImageData>): RemoteKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { key -> database.remoteKeysDao().remoteKeysRepoId(key.id) }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, ImageData>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { key -> database.remoteKeysDao().remoteKeysRepoId(key.id) }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, ImageData>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                database.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }
}