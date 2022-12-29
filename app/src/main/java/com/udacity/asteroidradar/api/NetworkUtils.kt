package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.util.Constants
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun parseAsteroidsJsonResult(jsonResult: JSONObject): ArrayList<Asteroid> {
    // get the near_earth_objects map from the API
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")
    // list of asteroids
    val asteroidList = ArrayList<Asteroid>()
    // get the list of dates
    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    // loop on every day in the seven days
    // every day has multiple asteroids objects
    for (formattedDate in nextSevenDaysFormattedDates) {
        // check if near_earth_objects map has the date
        if (nearEarthObjectsJson.has(formattedDate)) {
            // every day has multiple asteroids objects
            val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)
            // from i = 0 to i = dateAsteroidJsonArray.length
            // loop in day asteroids to add them in the list of asteroids
            for (i in 0 until dateAsteroidJsonArray.length()) {
                val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
                val id = asteroidJson.getLong("id")
                val codename = asteroidJson.getString("name")
                val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
                val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                    .getJSONObject("kilometers").getDouble("estimated_diameter_max")

                val closeApproachData = asteroidJson
                    .getJSONArray("close_approach_data").getJSONObject(0)
                val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                    .getDouble("kilometers_per_second")
                val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                    .getDouble("astronomical")
                val isPotentiallyHazardous = asteroidJson
                    .getBoolean("is_potentially_hazardous_asteroid")

                val asteroid = Asteroid(
                    id, codename, formattedDate, absoluteMagnitude,
                    estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous
                )
                asteroidList.add(asteroid)
            }
        }
    }

    return asteroidList
}

private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    // define a DateList to store dates
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    // from 0 to 7
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        // define the current time
        val currentTime = calendar.time
        // define a date format to format the dates by YYYY-MM-dd format
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        // add the date after formatted to the date list
        formattedDateList.add(dateFormat.format(currentTime))
        // get the next date from calender
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}


// note: getJsonObject() : map
// note: getString(), getDouble(): String and Double
// note: getJsonArray(): []