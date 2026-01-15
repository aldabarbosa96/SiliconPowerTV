package com.davidbarbosa.siliconpowertv.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.davidbarbosa.siliconpowertv.data.local.db.PopularTvDao
import com.davidbarbosa.siliconpowertv.data.local.db.TvDetailDao
import com.davidbarbosa.siliconpowertv.data.local.db.TvDetailEntity
import com.davidbarbosa.siliconpowertv.data.local.db.toDomain
import com.davidbarbosa.siliconpowertv.data.paging.PopularPagingSource
import com.davidbarbosa.siliconpowertv.data.remote.TmdbService
import com.davidbarbosa.siliconpowertv.data.remote.toDomain
import com.davidbarbosa.siliconpowertv.domain.model.TvShow
import com.davidbarbosa.siliconpowertv.domain.model.TvShowDetail
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Centralizamos aquí el acceso a datos (TMDB + Room) para no mezclarlo con la UI.
 * En detalle consultamos primero Room y, si no hay caché, pedimos a la API y guardamos el resultado.
 * En listado devolvemos Paging y aprovechamos para cachear páginas y poder mostrar datos offline.
 */

@Singleton
class TvRepository @Inject constructor(
    private val service: TmdbService,
    private val popularTvDao: PopularTvDao,
    private val tvDetailDao: TvDetailDao
) {

    suspend fun getDetail(tvId: Long, language: String): TvShowDetail {
        val cached = tvDetailDao.get(tvId, language)
        if (cached != null) return cached.toDomain()

        val dto = try {
            service.getTvDetail(tvId = tvId, language = language)
        } catch (e: IOException) {
            throw IOException("OFFLINE_NO_CACHE")
        }

        val genresList =
            (dto.genres ?: emptyList()).map { it.name.trim() }.filter { it.isNotBlank() }

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
                firstAirDate = dto.firstAirDate,
                genresCsv = genresList.joinToString("|"),
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

    suspend fun getRecommendations(tvId: Long, language: String, limit: Int = 10): List<TvShow> {
        val resp = service.getTvRecommendations(tvId = tvId, page = 1, language = language)
        return resp.results.asSequence().filter { it.id != tvId }.take(limit).map { it.toDomain() }
            .toList()
    }
}
