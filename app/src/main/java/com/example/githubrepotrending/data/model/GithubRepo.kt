package com.example.githubrepotrending.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "github_repo_table")
data class GithubRepo(
    @PrimaryKey(autoGenerate = true)
    var repoID: Int = 0,
    val author: String = "",
    val name: String = "",
    val avatar: String = "",
    val description: String = "",
    val language: String = "",
    val languageColor: String = "",
    val stars: Int = 0,
    val forks: Int = 0,
    var isExpanded: Boolean = false
    )