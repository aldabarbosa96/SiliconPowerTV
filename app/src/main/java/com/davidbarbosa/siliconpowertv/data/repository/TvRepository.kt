package com.davidbarbosa.siliconpowertv.data.repository

import androidx.paging.PagingData
import com.davidbarbosa.siliconpowertv.data.remote.TmdbNetwork
import com.davidbarbosa.siliconpowertv.data.remote.toDomain
import com.davidbarbosa.siliconpowertv.domain.model.TvShow
import com.davidbarbosa.siliconpowertv.domain.model.TvShowDetail
import kotlinx.coroutines.flow.Flow
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.davidbarbosa.siliconpowertv.data.paging.PopularPagingSource


class TvRepository {

    suspend fun getPopular(page: Int, language: String): List<TvShow> {
        val resp = TmdbNetwork.service.getPopularTv(page = page, language = language)
        return resp.results.map { it.toDomain() }
    }

    suspend fun getDetail(tvId: Long, language: String): TvShowDetail {
        val dto = TmdbNetwork.service.getTvDetail(tvId = tvId, language = language)
        return dto.toDomain()
    }

    fun popularPaging(language: String): Flow<PagingData<TvShow>> {
        return Pager(config = PagingConfig(
            pageSize = 20, enablePlaceholders = false
        ), pagingSourceFactory = {
            PopularPagingSource(
                service = com.davidbarbosa.siliconpowertv.data.remote.TmdbNetwork.service,
                language = language
            )
        }).flow
    }

}
