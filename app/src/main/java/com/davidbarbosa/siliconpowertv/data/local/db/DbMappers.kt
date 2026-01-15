package com.davidbarbosa.siliconpowertv.data.local.db

import com.davidbarbosa.siliconpowertv.domain.model.TvShowDetail

private fun parseGenres(csv: String?): List<String> =
    csv?.split('|')?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList()

fun TvDetailEntity.toDomain(): TvShowDetail = TvShowDetail(
    id = id,
    name = name,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    numberOfSeasons = numberOfSeasons,
    firstAirDate = firstAirDate,
    genres = parseGenres(genresCsv)
)
