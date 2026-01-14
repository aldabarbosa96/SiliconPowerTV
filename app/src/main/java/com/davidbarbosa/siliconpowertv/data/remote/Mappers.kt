package com.davidbarbosa.siliconpowertv.data.remote

import com.davidbarbosa.siliconpowertv.domain.model.TvShow
import com.davidbarbosa.siliconpowertv.domain.model.TvShowDetail

fun TvItemDto.toDomain(): TvShow = TvShow(
    id = id, name = name, posterPath = posterPath, voteAverage = voteAverage
)

fun TvDetailDto.toDomain(): TvShowDetail = TvShowDetail(
    id = id,
    name = name,
    overview = overview,
    voteAverage = voteAverage,
    numberOfSeasons = numberOfSeasons
)
