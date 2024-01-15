package com.infinitegearstudio.skysense.ui.slideshow

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.infinitegearstudio.skysense.R
import com.infinitegearstudio.skysense.databinding.FragmentHomeBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.infinitegearstudio.skysense.databinding.FragmentSlideshowBinding
import com.infinitegearstudio.skysense.ui.home.HomeViewModel
import java.util.Locale


// Obtén una referencia a la base de datos
val database: FirebaseDatabase = FirebaseDatabase.getInstance()
val myRef: DatabaseReference = database.getReference("sensors")

class SlideshowFragment : Fragment() {


    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = com.infinitegearstudio.skysense.ui.slideshow.database.getReference("sensors")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val SlideshowViewModel = ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root



        SlideshowViewModel.text.observe(viewLifecycleOwner) {
            if (_binding != null && isAdded) {  // Verificar si el fragmento está agregado
                myRef.addValueEventListener(object : ValueEventListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {

                            if (_binding != null) {

                                val etTemperaturePort = binding.etTemperaturePort
                                val etTemperature = binding.etTemperature
                                val tvHumidity = binding.tvHumidity
                                val tvAtm = binding.tvAtm
                                val tvLum = binding.tvLum


                                // Verificar si las vistas no son nulas antes de realizar operaciones
                                if (etTemperaturePort != null && etTemperature != null &&
                                    tvHumidity != null && tvAtm != null && tvLum != null
                                ) {

                                    if (etTemperaturePort != null) {
                                        Log.d(
                                            "HomeFragment",
                                            "etTemperaturePort no es nulo"
                                        )
                                        var temperatureNow =
                                            dataSnapshot.child("dht22").children.last().children.last()
                                                .child("temperature").value.toString() + " °C"
                                        var humidityNow =
                                            dataSnapshot.child("dht22").children.last().children.last()
                                                .child("humidity").value.toString() + " %"
                                        var luminosityNow =
                                            dataSnapshot.child("lm393").children.last().children.last()
                                                .child("luminousintensity").value.toString() + " -Lum"
                                        var atmNow =
                                            dataSnapshot.child("bmp280").children.last().children.last()
                                                .child("pressure").value.toString() + " -Atm"

                                        etTemperaturePort.setText(temperatureNow)
                                        etTemperature.setText(temperatureNow)
                                        tvHumidity.setText(humidityNow)
                                        tvLum.setText(luminosityNow)
                                        tvAtm.setText(atmNow)

                                    }


                                } else {
                                    Log.e("HomeFragment", "Al menos una vista es nula.")
                                }
                            } else {
                                Log.d(ContentValues.TAG, "no encontrado.")
                            }
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Manejar error en la lectura de datos
                        Log.w(ContentValues.TAG, "Error al leer datos: ${error.message}")
                    }
                })
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}