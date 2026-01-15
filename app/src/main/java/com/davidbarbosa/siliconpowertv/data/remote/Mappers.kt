package com.davidbarbosa.siliconpowertv.data.remote

import com.davidbarbosa.siliconpowertv.domain.model.TvShow
import com.davidbarbosa.siliconpowertv.domain.model.TvShowDetail

fun TvItemDto.toDomain(): TvShow = TvShow(
    id = id, name = name, posterPath = posterPath, voteAverage = voteAverage
)

fun TvDetailDto.toDomain(): TvShowDetail {
    val genresList = (genres ?: emptyList()).map { it.name.trim() }.filter { it.isNotBlank() }

    return TvShowDetail(
        id = id,
        name = name,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage,
        numberOfSeasons = numberOfSeasons,
        firstAirDate = firstAirDate,
        genres = genresList
    )
}
