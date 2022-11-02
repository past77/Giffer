package com.testtask.giphy.giffer.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.testtask.giphy.giffer.api.GiphyApi
import com.testtask.giphy.giffer.data.models.ImageData

class GiphyPagingSource(
    private val giphyApi: GiphyApi,
    private val query: String
) : PagingSource<Int, ImageData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageData> {
        TODO("Not yet implemented")
    }

    override fun getRefreshKey(state: PagingState<Int, ImageData>): Int? {
        TODO("Not yet implemented")
    }
}