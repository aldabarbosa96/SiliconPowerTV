package com.davidbarbosa.siliconpowertv.data.repository

import com.davidbarbosa.siliconpowertv.data.remote.TmdbNetwork
import com.davidbarbosa.siliconpowertv.data.remote.toDomain
import com.davidbarbosa.siliconpowertv.domain.model.TvShow

class TvRepository {

    suspend fun getPopular(page: Int, language: String): List<TvShow> {
        val resp = TmdbNetwork.service.getPopularTv(page = page, language = language)
        return resp.results.map { it.toDomain() }
    }
}
