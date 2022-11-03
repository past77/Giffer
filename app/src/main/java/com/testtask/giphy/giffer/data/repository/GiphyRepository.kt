package com.testtask.giphy.giffer.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.testtask.giphy.giffer.data.models.ImageData
import javax.inject.Singleton

@Singleton
interface GiphyRepository {
    fun getGifs(query: String): LiveData<PagingData<ImageData>>
}