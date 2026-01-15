package com.davidbarbosa.siliconpowertv.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.davidbarbosa.siliconpowertv.data.local.db.PopularTvDao
import com.davidbarbosa.siliconpowertv.data.paging.PopularPagingSource
import com.davidbarbosa.siliconpowertv.data.remote.TmdbService
import com.davidbarbosa.siliconpowertv.data.remote.toDomain
import com.davidbarbosa.siliconpowertv.domain.model.TvShow
import com.davidbarbosa.siliconpowertv.domain.model.TvShowDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvRepository @Inject constructor(
    private val service: TmdbService, private val popularTvDao: PopularTvDao
) {
    suspend fun getDetail(tvId: Long, language: String): TvShowDetail {
        val dto = service.getTvDetail(tvId = tvId, language = language)
        return dto.toDomain()
    }

    fun popularPaging(language: String): Flow<PagingData<TvShow>> {
        return Pager(config = PagingConfig(
            pageSize = 20, initialLoadSize = 20, enablePlaceholders = false
        ), pagingSourceFactory = {
            PopularPagingSource(
                service = service, dao = popularTvDao, language = language
            )
        }).flow
    }

    suspend fun getCachedPopular(language: String): List<TvShow> {
        return popularTvDao.getAllByLanguage(language).map { e ->
            TvShow(
                id = e.id, name = e.name, posterPath = e.posterPath, voteAverage = e.voteAverage
            )
        }
    }
}
