package dev.alamodi.mapapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.alamodi.mapapp.Location

@Database(entities = [Location::class ], version=1,exportSchema = false)
@TypeConverters(LocationTypeConverter::class)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao;
}