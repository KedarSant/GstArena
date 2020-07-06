package com.example.gstarena.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GstArenaService {

    @Headers(
        "Accept: application/vnd.arena+json;version=1",
        "User-Agent: Dalvik/2.1.0 (Linux; U; Android 10; Android SDK built for x86 Build/QSR1.190920.001)"
    )
    @GET("contests")
    fun getContestsAsync() : Deferred<List<NetworkContest>>

    @Headers(
        "Accept: application/vnd.arena+json;version=1",
        "User-Agent: Dalvik/2.1.0 (Linux; U; Android 10; Android SDK built for x86 Build/QSR1.190920.001)"
    )
    @GET("apps/statistics")
    fun getStatisticsAsync() : Deferred<List<NetworkStatistics>>

    @Headers(
        "Accept: application/vnd.arena+json;version=1",
        "User-Agent: Dalvik/2.1.0 (Linux; U; Android 10; Android SDK built for x86 Build/QSR1.190920.001)"
    )
    @GET("user")
    fun getUserAsync(@Query("email")email : String) : Deferred<User>

    @Headers(
        "Accept: application/vnd.arena+json;version=1",
        "User-Agent: Dalvik/2.1.0 (Linux; U; Android 10; Android SDK built for x86 Build/QSR1.190920.001)"
    )
    @GET("problems")
    fun getProblemsAsync() : Deferred<List<NetworkProblem>>

}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


object Network {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://arena.siesgst.ac.in/api/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val info = retrofit.create(GstArenaService::class.java)
}
