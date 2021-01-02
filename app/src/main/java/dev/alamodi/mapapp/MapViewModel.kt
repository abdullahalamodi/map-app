package dev.alamodi.mapapp

import androidx.lifecycle.ViewModel

class MapViewModel :ViewModel() {

    private val locationRepository = LocationRepository.get()

    fun addLocation(location: Location) = locationRepository.addLocation(location)

    fun updateLocation(location: Location) = locationRepository.updateLocation(location)

    fun getLocations(category:String) = locationRepository.getLocations(category)

}