package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    // get all Asteroids
    @Query("select * from databaseasteroid order by closeApproachDate Asc")
    fun getAllAsteroid(): LiveData<List<DatabaseAsteroid>>
    // get week Asteroids
    @Query("select * from databaseasteroid WHERE closeApproachDate >= :startDate and closeApproachDate <= :endDate order by closeApproachDate asc")
    fun getWeekAsteroids(startDate: String, endDate: String): LiveData<List<DatabaseAsteroid>>
    // get today Asteroids (note that start date == end date)
    @Query("select * from databaseasteroid WHERE closeApproachDate >= :startDate and closeApproachDate <= :startDate order by closeApproachDate asc")
    fun getTodayAsteroids(startDate: String): LiveData<List<DatabaseAsteroid>>
    // insert Asteroids in the database
    // note: where are inserting DatabaseAsteroid model not the default Asteroid
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAsteroids(vararg asteroids: DatabaseAsteroid)
}

// define the database
@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}
// use singleton pattern to get an instance of the database outer class
private lateinit var INSTANCE: AsteroidDatabase
fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "videos"
            ).build()
        }
    }
    return INSTANCE
}

