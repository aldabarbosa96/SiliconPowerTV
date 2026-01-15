package com.davidbarbosa.siliconpowertv.data.local.db

import androidx.room.Entity

@Entity(
    tableName = "tv_detail", primaryKeys = ["id", "language"]
)
data class TvDetailEntity(
    val id: Long,
    val language: String,
    val name: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val numberOfSeasons: Int?,
    val cachedAtMs: Long
)
