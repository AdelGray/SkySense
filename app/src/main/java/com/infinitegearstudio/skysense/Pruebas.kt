package com.infinitegearstudio.skysense

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

// Importa las clases necesarias
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

// Obtén una referencia a la base de datos
val database: FirebaseDatabase = FirebaseDatabase.getInstance()
val myRef: DatabaseReference = database.getReference("nombre_de_tu_nodo_en_la_base_de_datos")

class Pruebas : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pruebas)

        val texto: TextView = findViewById(R.id.salida)
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("sensors").child("dht22")

        // Obtener referencia al BarChart desde el diseño
        val barChart: BarChart = findViewById(R.id.barChart)

        val listaDias: MutableList<DataSnapshot> = mutableListOf()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d(TAG, "dias encontrado.-.-.-.-.-.-.-.-.-.-.-.-")

                    for (daySnapshot in dataSnapshot.children) {
                        listaDias.add(daySnapshot)
                    }

                    Log.d(TAG, "Tamaño: " + listaDias.size)

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
                    Log.d(TAG, "no encontrado.-.-.-.-.-.-.-.-.-.-.-.-")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar error en la lectura de datos
                Log.w(TAG, "Error al leer datos: ${error.message}")
            }
        })
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

