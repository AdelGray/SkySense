package com.infinitegearstudio.skysense.ui.barometer

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
import com.infinitegearstudio.skysense.databinding.FragmentTermometerBinding
import com.infinitegearstudio.skysense.ui.termometer.TermometerViewModel

class BarometerFragment : Fragment() {

    private var _binding: FragmentBarometerBinding? = null
    private val binding get() = _binding!!


    // Obtén una referencia a la base de datos
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = database.getReference("sensors").child("bmp280")



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val barometerViewModel = ViewModelProvider(this).get(BarometerViewModel::class.java)

        _binding = FragmentBarometerBinding.inflate(inflater, container, false)
        val root: View = binding.root


        barometerViewModel.text.observe(viewLifecycleOwner) {


            val lineChart: LineChart = binding.lineChart
            val listaDias: MutableList<DataSnapshot> = mutableListOf()

            myRef.addValueEventListener(object :
                ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d(ContentValues.TAG, "dias encontrado.-.-.-.-.-.-.-.-.-.-.-.-")

                        for (daySnapshot in dataSnapshot.children) {
                            listaDias.add(daySnapshot)
                        }

                        Log.d(ContentValues.TAG, "Tamaño: " + listaDias.size)
                        val entries = mutableListOf<Entry>()

                        val luminousintensityList: ArrayList<Float> = ArrayList()
                        var luminousintensity: Float

                        var count: Float = 0f


                        for (horaSnapshot in listaDias.reversed()[0].children) {
                            luminousintensity =
                                horaSnapshot.child("pressure").value.toString().toFloat()
                            entries.add(Entry(count, luminousintensity))
                            luminousintensityList.add(luminousintensity)
                            count++
                        }

                        Log.d(ContentValues.TAG, "Tamaño entriees: " + entries.size)
                        val etluminousintensity = binding.etluminousintensity
                        var luminousintensityNow = (listaDias.reversed()[0].children.reversed()[0].child("pressure").value.toString().toFloat()).toString() + "- Atm"


                        etluminousintensity.setText(luminousintensityNow)


                        val dataSet = LineDataSet(entries, "Presión")
                        val dataSets: ArrayList<ILineDataSet> = ArrayList()
                        dataSets.add(dataSet)

                        val lineData = LineData(dataSets)
                        lineChart.data = lineData

                        // Personalizar el gráfico según tus necesidades
                        lineChart.description.text = "Presión vs Tiempo"
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