package com.davidbarbosa.siliconpowertv.data.remote

import com.davidbarbosa.siliconpowertv.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object TmdbNetwork {

    private const val BASE_URL = "https://api.themoviedb.org/"

    // Añadimos key a todas las requests
    private val apiKeyInterceptor = Interceptor { chain ->
        val originalReq = chain.request()
        val originalUrl: HttpUrl = originalReq.url
        val newUrl =
            originalUrl.newBuilder().addQueryParameter("api_key", BuildConfig.TMDB_API_KEY).build()
        val newReq = originalReq.newBuilder().url(newUrl).build()

        chain.proceed(newReq)
    }

    private val loggingInterceptor: Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttp: OkHttpClient =
        OkHttpClient.Builder().addInterceptor(apiKeyInterceptor).addInterceptor(loggingInterceptor)
            .build()

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(okHttp)
        .addConverterFactory(MoshiConverterFactory.create(moshi)).build()

    val service: TmdbService = retrofit.create(TmdbService::class.java)
}
