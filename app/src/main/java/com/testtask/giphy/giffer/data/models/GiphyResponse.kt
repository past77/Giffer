package com.testtask.giphy.giffer.data.models

data class GiphyResponse(
    val data: List<ImageData>,
    val pagination: Pagination,
    val meta: Meta
)