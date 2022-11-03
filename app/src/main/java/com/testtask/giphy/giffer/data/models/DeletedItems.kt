package com.testtask.giphy.giffer.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_items")
data class DeletedItems(
    @PrimaryKey
    val deleteId: String,
)

