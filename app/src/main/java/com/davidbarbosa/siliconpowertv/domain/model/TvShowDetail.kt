package com.davidbarbosa.siliconpowertv.domain.model

data class TvShowDetail(
    val id: Long,
    val name: String,
    val overview: String,
    val voteAverage: Double,
    val numberOfSeasons: Int?
)
