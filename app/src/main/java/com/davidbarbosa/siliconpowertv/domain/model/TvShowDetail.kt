package com.davidbarbosa.siliconpowertv.domain.model

data class TvShowDetail(
    val id: Long,
    val name: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val numberOfSeasons: Int?,
    val firstAirDate: String? = null,
    val genres: List<String> = emptyList()
)
