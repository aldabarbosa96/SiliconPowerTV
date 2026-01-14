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
    suspend fun getDetail(tvId: Long, language: String): TvShowDetail {
        val dto = TmdbNetwork.service.getTvDetail(tvId = tvId, language = language)
        return dto.toDomain()
    }

    fun popularPaging(language: String): Flow<PagingData<TvShow>> {
        return Pager(config = PagingConfig(
            pageSize = 20, initialLoadSize = 20, enablePlaceholders = false
        ), pagingSourceFactory = {
            PopularPagingSource(service = TmdbNetwork.service, language = language)
        }).flow
    }

}
