package com.infinitegearstudio.skysense.ui.settings

import android.content.ContentValues
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Switch
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.infinitegearstudio.skysense.R
import com.infinitegearstudio.skysense.databinding.FragmentBarometerBinding
import com.infinitegearstudio.skysense.databinding.FragmentSettingsBinding
import com.infinitegearstudio.skysense.ui.barometer.BarometerViewModel
import kotlin.concurrent.thread

class SettingsFragment : Fragment() {



    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val swStation = binding.swStation
        val swBmp280 = binding.swBarometer
        val swDht22 = binding.swThermometer
        val swLm393 = binding.swPhotometer






        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("stationConf")

        swStation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                myRef.child("station_on").setValue(1);
            } else {
                myRef.child("station_on").setValue(0);
            }
        }


        swBmp280.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                myRef.child("bmp280_on").setValue(1);
            } else {
                myRef.child("bmp280_on").setValue(0);
            }
        }

        swDht22.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                myRef.child("dht22_on").setValue(1);
            } else {
                myRef.child("dht22_on").setValue(0);
            }
        }

        swLm393.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                myRef.child("lm393_on").setValue(1);
            } else {
                myRef.child("lm393_on").setValue(0);
            }
        }





        val spinner: Spinner = binding.spStation



// Obtener el contexto desde el fragmento
        val context: Context = requireContext()

// Crear un ArrayAdapter usando un array de strings y un diseño simple para el spinner
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            context,
            R.array.opciones_array,  // R.array.opciones_array debe ser un array de strings en tu archivo de recursos
            android.R.layout.simple_spinner_item
        )

// Especificar el diseño que se usará cuando se despliega el Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

// Asignar el adaptador al Spinner
        spinner.adapter = adapter

        val selectedValue: String = spinner.selectedItem.toString()

        // Obtén todos los datos en el nodo
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (_binding != null) {


                        // Obtiene el valor directamente como Long
                        val stationOn = dataSnapshot.child("station_on").getValue(Long::class.java)
                        val bmp280On = dataSnapshot.child("bmp280_on").getValue(Long::class.java)
                        val dht22on = dataSnapshot.child("dht22_on").getValue(Long::class.java)
                        val lm393on = dataSnapshot.child("lm393_on").getValue(Long::class.java)
                        val refreshRate =
                            dataSnapshot.child("refresh_rate").getValue(Long::class.java)


                        // Convierte el valor a String antes de usarlo
                        val stationOnString = stationOn?.toString()
                        val bmp280OnString = bmp280On?.toString()
                        val dht22onOnString = dht22on?.toString()
                        val lm393onOnString = lm393on?.toString()
                        val refreshRateOnString = refreshRate?.toString()

                        swStation.isChecked = stationOnString == "1"
                        swBmp280.isChecked = bmp280OnString == "1"
                        swDht22.isChecked = dht22onOnString == "1"
                        swLm393.isChecked = lm393onOnString == "1"




                        spinner.setSelection(
                            when (refreshRateOnString) {
                                "60" -> 4
                                "30" -> 3
                                "15" -> 2
                                "5" -> 1
                                "1" -> 0
                                else -> 0
                            }
                        )



                        spinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parentView: AdapterView<*>?,
                                    selectedItemView: View?,
                                    position: Int,
                                    id: Long
                                ) {


                                    // Código a ejecutar cuando se selecciona un elemento en el Spinner
                                    val selectedItem = spinner.selectedItem.toString()
                                    println("Elemento seleccionado: $selectedItem")



                                    myRef.child("refresh_rate").setValue(
                                        when (selectedItem) {
                                            "1 min" -> 1
                                            "5 min" -> 5
                                            "15 min" -> 15
                                            "30 min" -> 30
                                            "60 min" -> 60
                                            else -> 60
                                        }
                                    )


                                    // Puedes realizar acciones adicionales aquí según la selección
                                }

                                override fun onNothingSelected(parentView: AdapterView<*>?) {
                                    // Código a ejecutar cuando no se ha seleccionado nada en el Spinner
                                    println("Ningún elemento seleccionado")
                                    // Puedes realizar acciones adicionales aquí si no se ha seleccionado nada
                                }
                            }


                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar error en la lectura de datos
                Log.w(ContentValues.TAG, "Error al leer datos: ${error.message}")
            }
        })

        return root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}