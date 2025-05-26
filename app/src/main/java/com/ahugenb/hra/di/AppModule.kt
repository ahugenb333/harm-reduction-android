package com.ahugenb.hra.di

import android.content.Context
import com.ahugenb.hra.sync.SyncRepository
import com.ahugenb.hra.sync.SyncRepositoryImpl
import com.ahugenb.hra.tracker.db.DatabaseBuilder
import com.ahugenb.hra.tracker.db.DayDao
import com.ahugenb.hra.tracker.db.DayDatabase
import com.ahugenb.hra.tracker.db.DayRepository
import com.ahugenb.hra.tracker.db.DayRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDayRepository(dayRepositoryImpl: DayRepositoryImpl): DayRepository

    @Binds
    @Singleton
    abstract fun bindSyncRepository(syncRepositoryImpl: SyncRepositoryImpl): SyncRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDayDatabase(@ApplicationContext context: Context): DayDatabase {
        return DatabaseBuilder.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideDayDao(database: DayDatabase): DayDao {
        return database.dayDao()
    }
}
