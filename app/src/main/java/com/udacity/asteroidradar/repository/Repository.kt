package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Repository(private val database: AsteroidDatabase) {

    private fun getStartDate(): String {
        val calendar = Calendar.getInstance()
        return formatTheDate(calendar.time)
    }

    private fun getEndDate(): String {
        val calendar = Calendar.getInstance()
        // add 7 days to today
        calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
        return formatTheDate(calendar.time)
    }

    // format the date with YYYY-MM-dd and return the date as a String
    private fun formatTheDate(date: Date): String {
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        // return the date
        return dateFormat.format(date)
    }

    // get Asteroids
    suspend fun fetchAndUpdateAsteroids() {
        withContext(Dispatchers.IO) {
            // get the data from the internet
            var asteroidList: ArrayList<Asteroid>
            val asteroidsResponseBody =
                AsteroidApi.retrofitService.getAsteroids(
                    startDate = getStartDate(),
                    endDate = getEndDate(),
                )
            asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidsResponseBody))
            // insert the data to the database
            database.asteroidDao.insertAllAsteroids(*asteroidList.asDatabaseModel())
        }
    }

    // get all Asteroids
    val allAsteroid: LiveData<List<Asteroid>> = Transformations.map(
        // map asteroids from database to default asteroids
        database.asteroidDao.getAllAsteroid()
    ) {
        it.asDomainModel()
    }

    // get week Asteroids
    val weekAsteroid: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getWeekAsteroids(getStartDate(), getEndDate())
    ) {
        it.asDomainModel()
    }

    // get today Asteroids
    val todayAsteroid: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getTodayAsteroids(getStartDate())
    ) {
        it.asDomainModel()
    }


    // get ImageOfTheDay
    suspend fun fetchImageOfTheDay(): PictureOfDay {
        val pictureOfTheDay: PictureOfDay
        withContext(Dispatchers.IO) {
            pictureOfTheDay = AsteroidApi.retrofitService.getImageOfTheDay()
        }
        return pictureOfTheDay
    }

}