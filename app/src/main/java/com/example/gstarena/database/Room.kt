package com.example.gstarena.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContestsDao {

    @Transaction
    @Query("select * from databasecontest")
    fun getContests() : LiveData<List<ContestWithAdmin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContests(vararg contests: DatabaseContest)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAdmins(vararg admins : DatabaseAdmin)
}

@Dao
interface StatisticsDao {

    @Query("select * from databasestatistics")
    fun getStatistics() : LiveData<List<DatabaseStatistics>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStatistics(vararg statistics: DatabaseStatistics)

}

@Dao
interface ProblemsDao {

    @Query("select * from databaseproblem")
    fun getProblems() : LiveData<List<DatabaseProblem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProblems(vararg problems: DatabaseProblem)

}

@Database(entities = [DatabaseContest::class,DatabaseAdmin::class,DatabaseStatistics::class,DatabaseProblem::class],
    version = 3)
abstract class ContestsDatabase : RoomDatabase() {
    abstract val contestsDao: ContestsDao
    abstract val statisticsDao : StatisticsDao
    abstract val problemsDao : ProblemsDao
}

private lateinit var INSTANCE: ContestsDatabase

fun getDatabase(context: Context): ContestsDatabase {
    synchronized(ContestsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                ContestsDatabase::class.java,
                "contests").build()
        }
    }
    return INSTANCE
}