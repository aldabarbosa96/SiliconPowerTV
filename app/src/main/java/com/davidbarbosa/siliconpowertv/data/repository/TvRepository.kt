package com.davidbarbosa.siliconpowertv.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.davidbarbosa.siliconpowertv.data.local.db.PopularTvDao
import com.davidbarbosa.siliconpowertv.data.local.db.TvDetailDao
import com.davidbarbosa.siliconpowertv.data.local.db.TvDetailEntity
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
    private val service: TmdbService,
    private val popularTvDao: PopularTvDao,
    private val tvDetailDao: TvDetailDao
) {
    suspend fun getDetail(tvId: Long, language: String): TvShowDetail {
        // 1) Room primero
        val cached = tvDetailDao.get(tvId, language)
        if (cached != null) {
            return TvShowDetail(
                id = cached.id,
                name = cached.name,
                overview = cached.overview,
                voteAverage = cached.voteAverage,
                numberOfSeasons = cached.numberOfSeasons
            )
        }

        // 2) Si no hay, TMDB
        val dto = service.getTvDetail(tvId = tvId, language = language)

        // 3) Guardar en Room
        tvDetailDao.upsert(
            TvDetailEntity(
                id = dto.id,
                language = language,
                name = dto.name,
                overview = dto.overview,
                posterPath = dto.posterPath,
                backdropPath = dto.backdropPath,
                voteAverage = dto.voteAverage,
                numberOfSeasons = dto.numberOfSeasons,
                cachedAtMs = System.currentTimeMillis()
            )
        )
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
