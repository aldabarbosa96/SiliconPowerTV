package com.davidbarbosa.siliconpowertv.di

import android.content.Context
import androidx.room.Room
import com.davidbarbosa.siliconpowertv.data.local.db.AppDatabase
import com.davidbarbosa.siliconpowertv.data.local.db.PopularTvDao
import com.davidbarbosa.siliconpowertv.data.local.db.TvDetailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java, "siliconpowertv.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providePopularTvDao(db: AppDatabase): PopularTvDao = db.popularTvDao()

    @Provides
    fun provideTvDetailDao(db: AppDatabase): TvDetailDao = db.tvDetailDao()

}
