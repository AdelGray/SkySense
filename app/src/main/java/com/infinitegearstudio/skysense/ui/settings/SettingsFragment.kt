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
                    // Obtiene el valor directamente como Long
                    val stationOn = dataSnapshot.child("station_on").getValue(Long::class.java)
                    val bmp280On = dataSnapshot.child("bmp280_on").getValue(Long::class.java)
                    val dht22on = dataSnapshot.child("dht22_on").getValue(Long::class.java)
                    val lm393on = dataSnapshot.child("lm393_on").getValue(Long::class.java)


                    // Convierte el valor a String antes de usarlo
                    val stationOnString = stationOn?.toString()
                    val bmp280OnString = bmp280On?.toString()
                    val dht22onOnString = dht22on?.toString()
                    val lm393onOnString = lm393on?.toString()

                    swStation.isChecked = stationOnString == "1"
                    swBmp280.isChecked = bmp280OnString == "1"
                    swDht22.isChecked = dht22onOnString == "1"
                    swLm393.isChecked = lm393onOnString == "1"
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