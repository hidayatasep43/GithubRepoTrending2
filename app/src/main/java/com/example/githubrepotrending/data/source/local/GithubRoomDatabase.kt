package com.example.githubrepotrending.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubrepotrending.data.model.GithubRepo

@Database(entities = arrayOf(GithubRepo::class), version = 1, exportSchema = false)
public abstract class GithubRoomDatabase: RoomDatabase() {

    abstract fun githubRepoDao(): GithubRepoDao

    companion object {

        //singleton prevents multiple instanse of database opening at the same time
        @Volatile
        private var INSTANCE: GithubRoomDatabase? = null

        fun getDatabase(context: Context): GithubRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GithubRoomDatabase::class.java,
                    "github_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }


}