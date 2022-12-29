package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid

@Entity
data class DatabaseAsteroid constructor(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

// convert from database Asteroids object to normal Asteroid object
fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
       Asteroid(
           id = it.id,
           codename = it.codename,
           relativeVelocity = it.relativeVelocity,
           absoluteMagnitude = it.absoluteMagnitude,
           distanceFromEarth = it.distanceFromEarth,
           estimatedDiameter = it.estimatedDiameter,
           isPotentiallyHazardous = it.isPotentiallyHazardous,
           closeApproachDate = it.closeApproachDate,
       )
    }
}