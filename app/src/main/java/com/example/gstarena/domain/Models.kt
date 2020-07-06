package com.example.gstarena.domain

data class Contest(
    val _id : String,
    val code : String,
    val name : String,
    val description : String,
    val startsAt : String,
    val endsAt : String,
    val admins : List<Admin>
)

data class Admin(
    val _id : String,
    val name : String,
    val username : String,
    val email : String,
    val ratings : Int
)

data class Statistics (
    val name : String,
    val language : String,
    val acceptedCount : Int
)

data class Problem(
    val _id : String,
    val name : String,
    val code : String,
    val points : Int,
    val contestCode : String
)

data class Rating(
    val contestCode: String,
    val ratings: Int
)