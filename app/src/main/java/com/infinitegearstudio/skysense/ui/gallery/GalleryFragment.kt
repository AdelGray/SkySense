package com.infinitegearstudio.skysense.ui.gallery

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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.infinitegearstudio.skysense.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null


    // Obtén una referencia a la base de datos
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = database.getReference("sensors")



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {


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


                        for (horaSnapshot in  dataSnapshot.child("lm393").children.last().children) {
                            luminousintensity =
                                horaSnapshot.child("luminousintensity").value.toString().toFloat()
                            entries.add(Entry(count, luminousintensity))
                            luminousintensityList.add(luminousintensity)
                            count++
                        }


                        val etluminousintensity = binding.etluminousintensity
                        var luminousintensityNow = dataSnapshot.child("lm393").children.last().children.last().child("luminousintensity").value.toString() + "-Lum"


                        etluminousintensity.setText(luminousintensityNow)


                        val dataSet = LineDataSet(entries, "Luminosidad")
                        val dataSets: ArrayList<ILineDataSet> = ArrayList()
                        dataSets.add(dataSet)

                        val lineData = LineData(dataSets)
                        lineChart.data = lineData

                        // Personalizar el gráfico según tus necesidades
                        lineChart.description.text = "Luminosidad vs Tiempo"
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
