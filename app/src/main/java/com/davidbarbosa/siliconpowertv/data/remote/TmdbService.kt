package com.davidbarbosa.siliconpowertv.data.remote

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Endpoints de TMDB.
 * La api_key se añade desde OkHttp (interceptor) para no repetir parámetros ni ensuciar las llamadas.
 */

interface TmdbService {

    @GET("3/tv/popular")
    suspend fun getPopularTv(
        @Query("page") page: Int, @Query("language") language: String = "es-ES"
    ): PopularTvResponseDto

    @GET("3/tv/{tv_id}")
    suspend fun getTvDetail(
        @Path("tv_id") tvId: Long, @Query("language") language: String = "es-ES"
    ): TvDetailDto

    @GET("3/tv/{tv_id}/recommendations")
    suspend fun getTvRecommendations(
        @Path("tv_id") tvId: Long,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): TvRecommendationsResponseDto
}

data class PopularTvResponseDto(
    val page: Int, val results: List<TvItemDto>, @Json(name = "total_pages") val totalPages: Int
)

data class TvItemDto(
    val id: Long,
    val name: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "vote_average") val voteAverage: Double
)

data class GenreDto(
    val id: Int, val name: String
)

data class TvDetailDto(
    val id: Long,
    val name: String,
    val overview: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "number_of_seasons") val numberOfSeasons: Int?,
    @Json(name = "first_air_date") val firstAirDate: String?,
    val genres: List<GenreDto>?
)


data class TvRecommendationsResponseDto(
    val page: Int, val results: List<TvItemDto>, @Json(name = "total_pages") val totalPages: Int
)

