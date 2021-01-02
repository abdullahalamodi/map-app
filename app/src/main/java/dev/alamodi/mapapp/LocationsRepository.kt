package dev.alamodi.mapapp

import dev.alamodi.mapapp.database.LocationDatabase
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "locations-database"
class LocationRepository private constructor(context: Context) {

    private val database: LocationDatabase = Room.databaseBuilder(
        context.applicationContext,
        LocationDatabase::class.java,
        DATABASE_NAME
    )
        .build()

    private val locationDao = database.locationDao();
    private val executor = Executors.newSingleThreadExecutor();

    fun getLocations(category:String?): LiveData<List<Location>> = locationDao.getLocations(category)

    fun getLocation(id: UUID): LiveData<Location?> = locationDao.getLocation(id);

    fun updateLocation(location: Location) {
        executor.execute {
            locationDao.updateLocation(location)
        }
    }

    fun addLocation(location: Location) {
        executor.execute {
            locationDao.addLocation(location)
        }
    }


    companion object {
        private var INSTANCE: LocationRepository? = null;

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = LocationRepository(context)
            }
        }

        fun get(): LocationRepository {
            return INSTANCE ?:
            throw IllegalStateException("com.abdullahalamodi.mapapp.dev.alamodi.mapapp.TasksRepository must be initialized")
        }
    }
}