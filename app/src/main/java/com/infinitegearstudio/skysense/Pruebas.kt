package com.infinitegearstudio.skysense

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

import com.github.mikephil.charting.charts.BarChart
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


        var texto: TextView= findViewById(R.id.salida)
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()

        val myRef: DatabaseReference = database.getReference("stationConf")

        val barChart: BarChart = findViewById(R.id.barChart)
        val daysOfWeek = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

        // Configura los datos de ejemplo para las barras
        val entries = mutableListOf<BarEntry>()
        entries.add(BarEntry(1f, 10f))
        entries.add(BarEntry(2f, 20f))
        entries.add(BarEntry(3f, 15f))
        entries.add(BarEntry(4f, 25f))
        entries.add(BarEntry(5f, 20f))
        entries.add(BarEntry(6f, 15f))
        entries.add(BarEntry(7f, 25f))

        val dataSet = BarDataSet(entries, "dias")
        val barData = BarData(dataSet)

        // Configura el BarChart
        configureBarChart(barChart)

        // Establece los datos en el BarChart
        barChart.data = barData


        // Obtén todos los datos en el nodo
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Obtiene el valor directamente como Long
                    val valorLong = dataSnapshot.child("station_on").getValue(Long::class.java)

                    // Convierte el valor a String antes de usarlo
                    val valorString = valorLong?.toString()

                    // Ahora puedes usar el valorString según tus necesidades
                    Log.d(TAG, "Encontrado: $valorString")

                    texto.text = "Estacion encendida: "+valorString
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar error en la lectura de datos
                Log.w(TAG, "Error al leer datos: ${error.message}")
            }
        })


// Ejemplo de consulta con orderByChild y equalTo
       // myRef.orderByChild("station_on").addValueEventListener(object : ValueEventListener {
            // ...




    }

    private fun configureBarChart(chart: BarChart) {
        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.axisRight.isEnabled = false
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)

        // Ajusta la animación si lo deseas
        chart.animateY(1000)


    }









}