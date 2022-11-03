package com.testtask.giphy.giffer.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.testtask.giphy.giffer.data.models.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE repoId = :id")
    suspend fun remoteKeysRepoId(id: String): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}

//@Dao
//interface RemoteKeysDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAll(remoteKey: List<RemoteKeys>)
//
//    @Query("SELECT * FROM remotekeys WHERE repoId = :id")
//    suspend fun remoteKeysDoggoId(id: String): RemoteKeys?
//
//    @Query("DELETE FROM remotekeys")
//    suspend fun clearRemoteKeys()
//}