package com.davidbarbosa.siliconpowertv.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE tv_detail ADD COLUMN firstAirDate TEXT")
            db.execSQL("ALTER TABLE tv_detail ADD COLUMN genresCsv TEXT")
        }
    }

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java, "siliconpowertv.db"
        ).addMigrations(MIGRATION_2_3).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providePopularTvDao(db: AppDatabase): PopularTvDao = db.popularTvDao()

    @Provides
    fun provideTvDetailDao(db: AppDatabase): TvDetailDao = db.tvDetailDao()
}
