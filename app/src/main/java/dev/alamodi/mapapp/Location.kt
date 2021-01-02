package dev.alamodi.mapapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Location(
    @PrimaryKey
    var id:UUID = UUID.randomUUID(),
    var category: String = "",
    var title: String = "",
    var snippet: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0
) {

    companion object {
        fun getCategoriesList(): Array<String> {
            return arrayOf(
                "schools",
                "restaurants",
                "hotels",
                "markets",
                "stadiums"
            )
        }
    }
}