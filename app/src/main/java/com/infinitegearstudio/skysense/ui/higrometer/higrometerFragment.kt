package com.infinitegearstudio.skysense.ui.higrometer

import android.content.ContentValues
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.infinitegearstudio.skysense.R
import com.infinitegearstudio.skysense.databinding.FragmentBarometerBinding
import com.infinitegearstudio.skysense.databinding.FragmentHigrometerBinding
import com.infinitegearstudio.skysense.databinding.FragmentTermometerBinding
import com.infinitegearstudio.skysense.ui.barometer.BarometerViewModel
import com.infinitegearstudio.skysense.ui.termometer.TermometerViewModel

class higrometerFragment : Fragment() {

    private var _binding: FragmentHigrometerBinding? = null
    private val binding get() = _binding!!


    // Obtén una referencia a la base de datos
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = database.getReference("sensors")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val higrometerViewModel = ViewModelProvider(this).get(HigrometerViewModel::class.java)

        _binding = FragmentHigrometerBinding.inflate(inflater, container, false)
        val root: View = binding.root


        higrometerViewModel.text.observe(viewLifecycleOwner) {

            val lineChart: LineChart = binding.lineChart


            myRef.addValueEventListener(object :
                ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {

                        val entries = mutableListOf<Entry>()
                        val humidityList: ArrayList<Float> = ArrayList()
                        var count: Float = 0f


                        for (horaSnapshot in dataSnapshot.child("dht22").children.last().children) {
                            var humidity = horaSnapshot.child("humidity").value.toString().toFloat()
                            entries.add(Entry(count, humidity))
                            humidityList.add(humidity)
                            count++
                        }

                        val etHumidity = binding.etHumidity
                        var humidityNow = dataSnapshot.child("dht22").children.last().children.last().child("humidity").value.toString() +" %"

                        etHumidity.setText(humidityNow)


                        val dataSet = LineDataSet(entries, "Humedad")
                        val dataSets: ArrayList<ILineDataSet> = ArrayList()
                        dataSets.add(dataSet)

                        val lineData = LineData(dataSets)
                        lineChart.data = lineData

                        // Personalizar el gráfico según tus necesidades
                        lineChart.description.text = "Humedad vs Tiempo"
                        lineChart.setTouchEnabled(true)
                        lineChart.isDragEnabled = true
                        lineChart.setScaleEnabled(true)

                        // Mostrar el gráfico
                        lineChart.invalidate()



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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}