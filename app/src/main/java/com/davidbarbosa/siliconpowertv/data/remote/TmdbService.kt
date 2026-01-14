package com.davidbarbosa.siliconpowertv.data.remote

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    @GET("3/tv/popular")
    suspend fun getPopularTv(
        @Query("page") page: Int, @Query("language") language: String = "es-ES"
    ): PopularTvResponseDto

    @GET("3/tv/{tv_id}")
    suspend fun getTvDetail(
        @Path("tv_id") tvId: Long, @Query("language") language: String = "es-ES"
    ): TvDetailDto
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

data class TvDetailDto(
    val id: Long,
    val name: String,
    val overview: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "number_of_seasons") val numberOfSeasons: Int?
)
