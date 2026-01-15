package com.davidbarbosa.siliconpowertv.data.local.db

import androidx.room.Entity

@Entity(
    tableName = "popular_tv", primaryKeys = ["id", "language"]
)
data class PopularTvEntity(
    val id: Long,
    val language: String,
    val name: String,
    val posterPath: String?,
    val voteAverage: Double,
    val sortIndex: Long,
    val cachedAtMs: Long
)
