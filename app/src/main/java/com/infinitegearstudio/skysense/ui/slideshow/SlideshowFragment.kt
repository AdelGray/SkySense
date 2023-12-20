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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.infinitegearstudio.skysense.databinding.FragmentSlideshowBinding
import java.util.Locale


// Obtén una referencia a la base de datos
val database: FirebaseDatabase = FirebaseDatabase.getInstance()
val myRef: DatabaseReference = database.getReference("sensors").child("dht22")

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root


        slideshowViewModel.text.observe(viewLifecycleOwner) {






            val barChart: BarChart = binding.barChart
            val listaDias: MutableList<DataSnapshot> = mutableListOf()

            myRef.addValueEventListener(object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d(ContentValues.TAG, "dias encontrado.-.-.-.-.-.-.-.-.-.-.-.-")

                        for (daySnapshot in dataSnapshot.children) {
                            listaDias.add(daySnapshot)
                        }

                        Log.d(ContentValues.TAG, "Tamaño: " + listaDias.size)

                        // Configura los datos de ejemplo para las barras
                        val entries = mutableListOf<BarEntry>()

                        val temperatureList: ArrayList<Float> = ArrayList()

                        val humidityList: ArrayList<Float> = ArrayList()
                        var temperature:Float

                        var count: Float = 0f

                        for (horaSnapshot in listaDias.reversed()[0].children) {
                            temperature = horaSnapshot.child("temperature").value.toString().toFloat()
                            entries.add(BarEntry(count,temperature))
                            temperatureList.add(temperature)
                            count++
                        }


                        val etTemperaturePort = binding.etTemperaturePort
                        val etTemperature = binding.etTemperature
                        val tvHumidity = binding.tvHumidity

                        var temperatureNow= Math.round(listaDias.reversed()[0].children.reversed()[0].child("temperature").value.toString().toFloat()).toString()+"°C"
                        var humidityNow = Math.round(listaDias.reversed()[0].children.reversed()[0].child("humidity").value.toString().toFloat()).toString()+"%"

                        etTemperaturePort.setText(temperatureNow)
                        etTemperature.setText(temperatureNow)

                        tvHumidity.setText(humidityNow)


//-------------------------

                        val tvFecha = binding.tvFecha
                        var tvDia = binding.tvDia
                        // Obtener la fecha y hora actual
                        val fechaYHoraActual = LocalDateTime.now()

                        // Formatear la fecha y hora según tus necesidades
                        val fecha = DateTimeFormatter.ofPattern("d MMMM", Locale.getDefault())
                        val dia = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault())
                        val fechaFormateada = fechaYHoraActual.format(fecha)
                        val horaFormateada = fechaYHoraActual.format(dia)
                        // Imprimir la fecha y hora actual


                        // Imprimir la fecha y hora actual
                        tvDia.setText(horaFormateada)
                        tvFecha.setText(fechaFormateada)

                        println("Fecha y hora actual: $fechaFormateada")




                        // Configurar el conjunto de datos de barras
                        val dataSet = BarDataSet(entries, "Temperatura")
                        val barData = BarData(dataSet)

                        // Configurar el BarChart
                        configureBarChart(barChart)

                        // Establecer los datos en el BarChart
                        barChart.data = barData

                        // Convierte el valor a String antes de usarlo
                        //val valorString = valorLong?.toString()


                    } else {
                        Log.d(ContentValues.TAG, "no encontrado.-.-.-.-.-.-.-.-.-.-.-.-")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar error en la lectura de datos
                    Log.w(ContentValues.TAG, "Error al leer datos: ${error.message}")
                }
            })








        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configureBarChart(chart: BarChart) {
        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.axisRight.isEnabled = false
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.animateY(1000)
    }
}