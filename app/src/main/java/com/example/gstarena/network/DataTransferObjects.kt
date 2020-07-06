package com.example.gstarena.network

import com.example.gstarena.database.DatabaseAdmin
import com.example.gstarena.database.DatabaseContest
import com.example.gstarena.database.DatabaseProblem
import com.example.gstarena.database.DatabaseStatistics
import com.example.gstarena.domain.Rating
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkContest(
    val type : String,
    val _id : String,
    val code : String,
    val name : String,
    val description : String,
    val startsAt : String,
    val endsAt : String,
    val contestAdmin: List<NetworkAdmin>)

@JsonClass(generateAdapter = true)
data class NetworkAdmin(
    val ratings : Int,
    val _id : String,
    val name : String,
    val username : String,
    val email : String
)

@JsonClass(generateAdapter = true)
data class NetworkStatistics (
    val name : String,
    val language : String,
    val acceptedCount : Int
)

@JsonClass(generateAdapter = true)
data class User (
    val _id: String,
    val name : String,
    val username : String,
    val email : String,
    val about : String?,
    val ratings : Int,
    val codeforcesLink : String?,
    val githubLink : String?,
    val codechefLink : String?,
    val submissionStats: SubmissionStats,
    val rantingChange : List<NetworkRating>
)

@JsonClass(generateAdapter = true)
data class NetworkRating(
    val contestCode: String,
    val ratings: Int
)

@JsonClass(generateAdapter = true)
data class SubmissionStats (
    val accepted : Int,
    val wrongAnswer : Int,
    val compilationError : Int,
    val runtimeError : Int,
    val timeLimitExceeded : Int
)

@JsonClass(generateAdapter = true)
data class NetworkProblem constructor(
    val _id : String,
    val name : String,
    val code : String,
    val points : Int,
    val contestCode : String
)


fun List<NetworkContest>.asDatabaseContest() : Array<DatabaseContest> {
    return map {
        DatabaseContest(it._id,it.code,it.name,it.description,it.startsAt,it.endsAt)
    }.toTypedArray()
}

fun List<NetworkAdmin>.asDatabaseAdmin(contestId : String) :  Array<DatabaseAdmin> {
    return map {
        DatabaseAdmin(it._id,contestId,it.name,it.username,it.email,it.ratings)
    }.toTypedArray()
}

fun List<NetworkStatistics>.asDatabaseStatistics() : Array<DatabaseStatistics> {
    return map {
        DatabaseStatistics(it.name,it.language,it.acceptedCount)
    }.toTypedArray()
}

fun List<NetworkProblem>.asDatabaseProblem() : Array<DatabaseProblem> {
    return map {
        DatabaseProblem(it._id,it.name,it.code,it.points,it.contestCode)
    }.toTypedArray()
}

fun List<NetworkRating>.asDomainRating() : List<Rating> {
    return map {
        Rating(it.contestCode,it.ratings)
    }
}