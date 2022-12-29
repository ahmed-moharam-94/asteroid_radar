package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {



    // define a database object to use it in the repository
    private val database = getDatabase(application)

    // define the repository
    private val repository = Repository(database)

    // create a live data for the image so it can be updated when the url is ready
    private val _imageOfTheDay = MutableLiveData<PictureOfDay>()
    val imageOfTheDay: LiveData<PictureOfDay>
        get() = _imageOfTheDay

    init {

        viewModelScope.launch {
            // fetch Asteroids
            try {
                repository.fetchAndUpdateAsteroids()
                _imageOfTheDay.value = repository.fetchImageOfTheDay()
            } catch (exception: Exception) {
                Log.i("Exception", exception.toString())
            }
        }
    }

    // get Asteroids from repository
    val allAsteroids = repository.allAsteroid
    val weekAsteroids = repository.weekAsteroid
    val todayAsteroids = repository.todayAsteroid

    private val _asteroid = MutableLiveData<Asteroid>()
    val asteroid: LiveData<Asteroid>
    get() = _asteroid
    fun navigateToDetailFragment(asteroid: Asteroid) {
        _asteroid.value = asteroid
    }
    fun navigateToDetailFragmentComplete() {
        _asteroid.value = null
    }

}