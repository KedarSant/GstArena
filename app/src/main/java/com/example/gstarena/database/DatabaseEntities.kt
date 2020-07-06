package com.example.gstarena.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.gstarena.domain.Admin
import com.example.gstarena.domain.Contest
import com.example.gstarena.domain.Problem
import com.example.gstarena.domain.Statistics

@Entity
data class DatabaseContest constructor(
    @PrimaryKey
    val _id : String,
    val code : String,
    val name : String,
    val description : String,
    val startsAt : String,
    val endsAt : String
)

@Entity
data class DatabaseAdmin constructor(
    @PrimaryKey
    val _id : String,
    val contestId : String,
    val name : String,
    val username : String,
    val email : String,
    val ratings : Int
)

@Entity
data class DatabaseProblem constructor(
    @PrimaryKey
    val _id : String,
    val name : String,
    val code : String,
    val points : Int,
    val contestCode : String
)

class ContestWithAdmin() {
    @Embedded
    lateinit var contest: DatabaseContest

    @Relation(
        parentColumn = "_id",
        entityColumn = "contestId"
    )
    lateinit var admins: List<DatabaseAdmin>
}

@Entity
data class DatabaseStatistics(
    val name : String,
    @PrimaryKey
    val language : String,
    val acceptedCount : Int
)

fun List<ContestWithAdmin>.asDomainContests() : List<Contest> {
    return map {
        Contest(
            _id = it.contest._id,
            code = it.contest.code,
            name = it.contest.name,
            description = it.contest.description,
            startsAt = it.contest.startsAt,
            endsAt = it.contest.endsAt,
            admins = it.admins.map {it->
                it.asDomainAdmin()
            }
        )
    }
}

fun DatabaseAdmin.asDomainAdmin() : Admin {
    return Admin(_id,name, username, email,ratings)
}

fun List<DatabaseStatistics>.asDomainStatistics() : List<Statistics> {
    return map {
        Statistics(
            name = it.name,
            language = it.language,
            acceptedCount = it.acceptedCount
        )
    }
}

fun List<DatabaseProblem>.asDomainProblem() : List<Problem> {
    return map{
        Problem(it._id,it.name,it.code,it.points,it.contestCode)
    }
}