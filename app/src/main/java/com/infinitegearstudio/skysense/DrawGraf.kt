package com.infinitegearstudio.skysense

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class DrawGraf : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_graf)



        // Obtener referencia al LineChart desde el diseño
        val lineChart: LineChart = findViewById(R.id.lineChart)

        // Configurar datos de ejemplo
        val entries = mutableListOf<Entry>()
        entries.add(Entry(1f, 20f))
        entries.add(Entry(2f, 15f))
        entries.add(Entry(3f, 25f))

        val dataSet = LineDataSet(entries, "Label")
        val lineData = LineData(dataSet)

        // Configurar el LineChart
        configureChart(lineChart)

        // Establecer los datos en el LineChart
        lineChart.data = lineData
    }

    private fun configureChart(chart: LineChart) {
        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.axisRight.isEnabled = false
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)

    }
}