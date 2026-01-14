package com.davidbarbosa.siliconpowertv.data.repository

import com.davidbarbosa.siliconpowertv.data.remote.TmdbNetwork
import com.davidbarbosa.siliconpowertv.data.remote.toDomain
import com.davidbarbosa.siliconpowertv.domain.model.TvShow
import com.davidbarbosa.siliconpowertv.domain.model.TvShowDetail

class TvRepository {

    suspend fun getPopular(page: Int, language: String): List<TvShow> {
        val resp = TmdbNetwork.service.getPopularTv(page = page, language = language)
        return resp.results.map { it.toDomain() }
    }

    suspend fun getDetail(tvId: Long, language: String): TvShowDetail {
        val dto = TmdbNetwork.service.getTvDetail(tvId = tvId, language = language)
        return dto.toDomain()
    }
}
