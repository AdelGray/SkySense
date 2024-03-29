package com.infinitegearstudio.skysense.ui.termometer

import android.content.ContentValues
import android.graphics.Color
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.infinitegearstudio.skysense.databinding.FragmentGalleryBinding
import com.infinitegearstudio.skysense.databinding.FragmentSlideshowBinding
import com.infinitegearstudio.skysense.databinding.FragmentTermometerBinding
import com.infinitegearstudio.skysense.ui.gallery.GalleryViewModel

class TermometerFragment : Fragment() {



        private var _binding: FragmentTermometerBinding? = null
        private val binding get() = _binding!!


    // Obtén una referencia a la base de datos
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = database.getReference("sensors")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val termometerViewModel = ViewModelProvider(this).get(TermometerViewModel::class.java)

        _binding = FragmentTermometerBinding.inflate(inflater, container, false)
        val root: View = binding.root




        termometerViewModel.text.observe(viewLifecycleOwner) {




                val lineChart: LineChart = binding.lineChart
                val listaDias: MutableList<DataSnapshot> = mutableListOf()

                myRef.addValueEventListener(object :
                    ValueEventListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {

                            if (_binding != null) {

                                val entries = mutableListOf<Entry>()
                                val luminousintensityList: ArrayList<Float> = ArrayList()
                                var luminousintensity: Float

                                var count: Float = 0f


                                for (horaSnapshot in dataSnapshot.child("bmp280").children.last().children) {
                                    luminousintensity =
                                        horaSnapshot.child("temperature").value.toString().toFloat()
                                    entries.add(Entry(count, luminousintensity))
                                    luminousintensityList.add(luminousintensity)
                                    count++
                                }


                                val etluminousintensity = binding.etluminousintensity
                                var luminousintensityNow =
                                    dataSnapshot.child("bmp280").children.last().children.last()
                                        .child("temperature").value.toString() + " °C"


                                etluminousintensity.setText(luminousintensityNow)


                                val dataSet = LineDataSet(entries, "Temperatura")
                                dataSet.color = Color.parseColor("#1E88E5")
                                dataSet.lineWidth = 1.8f
                                val dataSets: ArrayList<ILineDataSet> = ArrayList()
                                dataSets.add(dataSet)

                                val lineData = LineData(dataSets)
                                lineChart.data = lineData

                                // Personalizar el gráfico según tus necesidades
                                lineChart.description.text = "Temperatura vs Tiempo"
                                lineChart.setTouchEnabled(true)
                                lineChart.isDragEnabled = true
                                lineChart.setScaleEnabled(true)

                                // Mostrar el gráfico
                                lineChart.invalidate()


                            } else {
                                Log.d(ContentValues.TAG, "no encontrado.-.-.-.-.-.-.-.-.-.-.-.-")
                            }
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
       // viewModel = ViewModelProvider(this).get(TermometerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}