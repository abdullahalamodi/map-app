package dev.alamodi.mapapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var locationList: Array<String>
    private val mapViewModel: MapViewModel by lazy {
        ViewModelProviders.of(this).get(MapViewModel::class.java)
    }
    private lateinit var category: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationList = Location.getCategoriesList()
        setContentView(R.layout.activity_main)
        category_s.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, Location.getCategoriesList())
        category_s.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                category = locationList[p2]
            }
        }

        add_btn.setOnClickListener {
            if (category.isNotBlank() &&
                title_ev.text.isNotBlank() &&
                address_ev.text.isNotBlank() &&
                lat_ev.text.isNotBlank() &&
                lng_ev.text.isNotBlank()
            ) {
                val location = Location(
                    category = category,
                    title = title_ev.text.toString(),
                    snippet = address_ev.text.toString(),
                    lat = lat_ev.text.toString().toDouble(),
                    lng = lng_ev.text.toString().toDouble()
                )
                mapViewModel.addLocation(location)
                Toast.makeText(this, "location add successfully", Toast.LENGTH_SHORT).show()
                clearViews()
            } else {
                Toast.makeText(this, "some fields are empty !!", Toast.LENGTH_SHORT).show()
            }
        }
        map_btn.setOnClickListener {
            MapActivity.newInstance(this).apply {
                startActivity(this);
            }
        }
    }


    private fun clearViews() {
        title_ev.setText("")
        address_ev.setText("")
        lat_ev.setText("")
        lng_ev.setText("")
    }
}