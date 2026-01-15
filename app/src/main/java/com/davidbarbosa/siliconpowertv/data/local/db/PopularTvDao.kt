package com.davidbarbosa.siliconpowertv.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PopularTvDao {

    @Query(
        """
        SELECT * FROM popular_tv 
        WHERE language = :language 
        ORDER BY sortIndex ASC
    """
    )
    suspend fun getAllByLanguage(language: String): List<PopularTvEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<PopularTvEntity>)

    @Query("DELETE FROM popular_tv WHERE language = :language")
    suspend fun clearByLanguage(language: String)

    @Query("DELETE FROM popular_tv WHERE cachedAtMs < :olderThanMs")
    suspend fun deleteOlderThan(olderThanMs: Long)
}

