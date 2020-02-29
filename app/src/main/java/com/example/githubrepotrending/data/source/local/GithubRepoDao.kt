package com.example.githubrepotrending.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubrepotrending.data.model.GithubRepo

@Dao
interface GithubRepoDao {

    @Query("SELECT * FROM github_repo_table")
    suspend fun getAllRepo(): List<GithubRepo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(githubRepo: GithubRepo)

    @Query("DELETE FROM github_repo_table")
    suspend fun deleteAll()

    @Transaction
    open suspend fun setGithubRepoData(items: List<GithubRepo>) {
        deleteAll()
        for (item in items) {
            insert(item)
        }
    }

}