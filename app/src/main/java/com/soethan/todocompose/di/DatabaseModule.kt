package com.soethan.todocompose.di

import android.content.Context
import androidx.room.Room
import com.soethan.todocompose.data.ToDoDatabase
import com.soethan.todocompose.data.repositories.DataStoreRepository
import com.soethan.todocompose.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        ToDoDatabase::class.java,
        DATABASE_NAME
    ).build()

//    @Singleton
//    @Provides
//    fun provideDataStore(@ApplicationContext context: Context):DataStoreRepository{
//        return DataStoreRepository(context = context)
//    }

    @Singleton
    @Provides
    fun provideDao(database: ToDoDatabase) = database.toDoDao()

}