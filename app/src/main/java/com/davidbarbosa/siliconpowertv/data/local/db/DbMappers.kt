package com.davidbarbosa.siliconpowertv.data.local.db

import com.davidbarbosa.siliconpowertv.domain.model.TvShowDetail

fun TvDetailEntity.toDomain(): TvShowDetail = TvShowDetail(
    id = id,
    name = name,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    numberOfSeasons = numberOfSeasons
)
