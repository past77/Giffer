package com.testtask.giphy.giffer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.testtask.giphy.giffer.data.models.DeletedItems
import com.testtask.giphy.giffer.data.repository.GiphyRepository
import com.testtask.giphy.giffer.data.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val giphyRepository: GiphyRepository,
    private val roomRepository: RoomRepository,
): ViewModel() {

    private val queryLiveData = MutableLiveData("all")

    val gifs = queryLiveData.switchMap { giphyRepository.getGifs(it).cachedIn(viewModelScope) }

    fun searchGifs(query: String) {
        queryLiveData.value = query
    }

    fun deleteGif(id: String) {
        viewModelScope.launch {
            with(roomRepository) {
                deleteGitById(id)
                addIdToDeletedItems(DeletedItems(id))
            }
        }
    }
}