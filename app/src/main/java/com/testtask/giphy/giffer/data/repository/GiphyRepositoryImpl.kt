package com.testtask.giphy.giffer.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.testtask.giphy.giffer.data.api.GiphyApi
import com.testtask.giphy.giffer.data.db.GifDatabase
import com.testtask.giphy.giffer.data.models.ImageData
import javax.inject.Inject

class GiphyRepositoryImpl @Inject constructor(
    private val api: GiphyApi,
    private val database: GifDatabase
    ): GiphyRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getGifs(query: String): LiveData<PagingData<ImageData>> {
        val pagingSourceFactory = { database.gifDao().getAllImageModel() }
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = GiphyMediator(api, query = query, database)
        ).liveData
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false)
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}