package com.testtask.giphy.giffer.data.repository

import androidx.paging.*
import androidx.room.withTransaction
import com.testtask.giphy.giffer.data.api.GiphyApi
import com.testtask.giphy.giffer.data.db.GifDatabase
import com.testtask.giphy.giffer.data.models.ImageData
import com.testtask.giphy.giffer.data.models.RemoteKeys
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class GiphyMediator(
    private val api: GiphyApi,
    private val query: String,
    private val database: GifDatabase
) : RemoteMediator<Int, ImageData>() { //PagingSource<Int, ImageData>() {

    val DEFAULT_PAGE_INDEX = 1

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
        println("R# page - $page")
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
                database.remoteKeysDao().insertAll(keys)
               database.gifDao().insertAll(response.data)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }


    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, ImageData>): Any? {
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





















//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageData> {
//        val position = params.key ?: 1
//        println("R# position - $position")
//        return try {
//            // Start refresh at page 1 if undefined.
//
//            println("query - $query")
//            val response = giphyApi.searchImages(
//                query = query,
//                offset = position * 20,
//                limit = NETWORK_PAGE_SIZE
//            )
////            response.data.forEach { println("id-- ${it.url}") }
////            val gifDao = database.gifDao()
////            gifDao.insertAllImages(response.data)
////            val gifs = gifDao.getAllImages()
//            val gifs = response.data
//            println("gifs - $gifs")
//            println("nextKeyPosition - $position")
////            println("bool - ${gifDao.getAllImages().isEmpty()}")
//            LoadResult.Page(
//                data = gifs,// gifDao.getAllImages(),
//                prevKey = if (position == 1) null else position - 1, // null - only forward
//                nextKey = if (gifs.isEmpty()) null else position + 1 //(gifDao.getAllImages().isEmpty()) null else position + 1
//            )
//        } catch (e: IOException) {
//            println("R# IOEXCPTION")
//            return LoadResult.Error(e)
//        } catch (e: HttpException) {
//            println("R# HTTPEXCEPTION")
//            // HttpException for any non-2xx HTTP status codes.
//            return LoadResult.Error(e)
//        }
//    }

    //    override fun getRefreshKey(state: PagingState<Int, ImageData>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, ImageData>
//    ): MediatorResult {
//        println("LOAD- $loadType")
//        return try {
//            val page = when (loadType) {
//                LoadType.REFRESH -> {
//                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
//                    remoteKeys?.nextKey?.minus(1) ?: 1
//                }
//                LoadType.PREPEND -> {
//                    val remoteKeys = getRemoteKeyForFirstItem(state)
//                    val prevKey = remoteKeys?.prevKey
//                    if (prevKey == null) {
//                        return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//                    }
//                    prevKey
//                }
//                LoadType.APPEND -> {
//                    val remoteKeys = getRemoteKeyForLastItem(state)
//                    println("APPEND key: $remoteKeys")
//                    val nextKey = remoteKeys?.nextKey
//                    if (nextKey == null) {
//                        return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//                    }
//                    nextKey
//                }

//            val page = when (loadType) {
//                LoadType.REFRESH -> null
//                LoadType.PREPEND ->
//                    return MediatorResult.Success(endOfPaginationReached = true)
//                LoadType.APPEND -> {
//                    val lastItem = state.lastItemOrNull()
//                        ?: return MediatorResult.Success(
//                            endOfPaginationReached = true
//                        )
//                    lastItem.id
//                }
//            }
//            println("R# page - $page")
//            println(state.config.pageSize)
//            val response = giphyApi.searchImages(
//                query = query,
//                offset = page,
//                limit = state.config.pageSize
//            )
//            val gifDao = database.gifDao()
//            val endOfPaginationReached = response.data.isEmpty()
//            database.withTransaction {
//                if (loadType == LoadType.REFRESH) {
//                    database.remoteKeysDao().clearRemoteKeys()
//                    gifDao.clearAllGifs()
//                }
//
//                val prevKey = if (page == 1) null else page - 1
//                val nextKey = if (endOfPaginationReached) null else page + 1
//                val keys = response.data.map {
//                    RemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
//                }
//                database.remoteKeysDao().insertAll(keys)
//                database.gifDao().insertAllImages(response.data)
//                // Insert new users into database, which invalidates the
//                // current PagingData, allowing Paging to present the updates
//                // in the DB.
//                gifDao.insertAllImages(response.data)
//            }
//
//            MediatorResult.Success(
//                endOfPaginationReached = endOfPaginationReached
//            )
//        } catch (e: IOException) {
//            return MediatorResult.Error(e)
//        } catch (e: HttpException) {
//            return MediatorResult.Error(e)
//        }
//    }
//
//    private suspend fun getRemoteKeyClosestToCurrentPosition(
//        state: PagingState<Int, ImageData>
//    ): RemoteKeys? {
//        // The paging library is trying to load data after the anchor position
//        // Get the item closest to the anchor position
//        return state.anchorPosition?.let { position ->
//            state.closestItemToPosition(position)?.id?.let { repoId ->
//                database.remoteKeysDao().remoteKeysRepoId(repoId)
//            }
//        }
//    }
//
//    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ImageData>): RemoteKeys? {
//        // Get the first page that was retrieved, that contained items.
//        // From that first page, get the first item
//        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
//            ?.let { repo ->
//                // Get the remote keys of the first items retrieved
//                database.remoteKeysDao().remoteKeysRepoId(repo.id)
//            }
//    }
//
//    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ImageData>): RemoteKeys? {
//        // Get the last page that was retrieved, that contained items.
//        // From that last page, get the last item
//        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
//            ?.let { repo ->
//                // Get the remote keys of the last item retrieved
//                database.remoteKeysDao().remoteKeysRepoId(repo.id)
//            }
//    }

//    override fun getRefreshKey(state: PagingState<Int, ImageData>): Int? {
//        TODO("Not yet implemented")
//    }

//}