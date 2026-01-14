package com.davidbarbosa.siliconpowertv.data.remote

import com.davidbarbosa.siliconpowertv.domain.model.TvShow

fun TvItemDto.toDomain(): TvShow = TvShow(
    id = id, name = name, posterPath = posterPath, voteAverage = voteAverage
)
