package com.davidbarbosa.siliconpowertv.di

import com.davidbarbosa.siliconpowertv.BuildConfig
import com.davidbarbosa.siliconpowertv.data.remote.TmdbService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Configuración de red (OkHttp, Retrofit y Moshi).
 * Añadimos un interceptor para incluir la api_key automáticamente y dejamos logging básico para facilitar debugging sin demasiado ruido.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.themoviedb.org/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val apiKeyInterceptor = Interceptor { chain ->
            val originalReq = chain.request()
            val originalUrl: HttpUrl = originalReq.url
            val newUrl =
                originalUrl.newBuilder().addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                    .build()
            val newReq = originalReq.newBuilder().url(newUrl).build()
            chain.proceed(newReq)
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder().addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()

    @Provides
    @Singleton
    fun provideTmdbService(retrofit: Retrofit): TmdbService =
        retrofit.create(TmdbService::class.java)
}
