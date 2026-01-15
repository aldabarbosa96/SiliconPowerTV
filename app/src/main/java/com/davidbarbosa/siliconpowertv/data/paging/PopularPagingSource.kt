package com.davidbarbosa.siliconpowertv.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.davidbarbosa.siliconpowertv.data.local.db.PopularTvDao
import com.davidbarbosa.siliconpowertv.data.local.db.PopularTvEntity
import com.davidbarbosa.siliconpowertv.data.remote.TmdbService
import com.davidbarbosa.siliconpowertv.data.remote.toDomain
import com.davidbarbosa.siliconpowertv.domain.model.TvShow

/**
 * Cargamos páginas desde TMDB y guardamos en Room con un índice de orden para poder reconstruir
 * el listado si el refresh falla o no hay conexión.
 */
class PopularPagingSource(
    private val service: TmdbService, private val dao: PopularTvDao, private val language: String
) : PagingSource<Int, TvShow>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShow> {
        return try {
            val page = params.key ?: 1
            val resp = service.getPopularTv(page = page, language = language)

            // datos para UI
            val data = resp.results.map { it.toDomain() }

            // guardamso caché en Room
            val now = System.currentTimeMillis()
            val baseIndex = (page - 1L) * 20L // TMDB parece dar 20aprox por página
            val entities = resp.results.mapIndexed { index, dto ->
                PopularTvEntity(
                    id = dto.id,
                    language = language,
                    name = dto.name,
                    posterPath = dto.posterPath,
                    voteAverage = dto.voteAverage,
                    sortIndex = baseIndex + index.toLong(),
                    cachedAtMs = now
                )
            }
            dao.upsertAll(entities)

            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page >= resp.totalPages) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TvShow>): Int? {
        return state.anchorPosition?.let { anchor ->
            val page = state.closestPageToPosition(anchor)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }
}
