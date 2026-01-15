package com.davidbarbosa.siliconpowertv.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TvDetailDao {

    @Query("SELECT * FROM tv_detail WHERE id = :id AND language = :language LIMIT 1")
    suspend fun get(id: Long, language: String): TvDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: TvDetailEntity)
}
