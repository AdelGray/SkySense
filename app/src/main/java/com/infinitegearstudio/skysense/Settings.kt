package com.infinitegearstudio.skysense

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val swStation = findViewById<Switch>(R.id.switch4)


        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("stationConf")

        swStation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Por ejemplo, si deseas editar un campo llamado "nombre" a un nuevo valor "NuevoNombre"
             myRef.child("station_on").setValue(1);



            } else {
                // Acciones cuando el switch está desactivado (opción 2)
                // Puedes realizar acciones adicionales aquí
                myRef.child("station_on").setValue(0);
            }
        }


        val spinner: Spinner = findViewById(R.id.spinner)

        // Crear un ArrayAdapter usando un array de strings y un diseño simple para el spinner
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.opciones_array,  // R.array.opciones_array debe ser un array de strings en tu archivo de recursos
            android.R.layout.simple_spinner_item
        )

        // Especificar el diseño que se usará cuando se despliega el Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asignar el adaptador al Spinner
        spinner.adapter = adapter





        // Obtén todos los datos en el nodo
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Obtiene el valor directamente como Long
                    val stationOn = dataSnapshot.child("station_on").getValue(Long::class.java)
                    //val dht22on = dataSnapshot.child("dht22_on").getValue(Long::class.java)
                    //val bmp208On = dataSnapshot.child("bmp208_on").getValue(Long::class.java)

                    // Convierte el valor a String antes de usarlo
                    val stationOnString = stationOn?.toString()
                    //val dht22onOnString = dht22on?.toString()
                    //val bmp208OnString = bmp208On?.toString()

                    swStation.isChecked = stationOnString == "1"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar error en la lectura de datos
                Log.w(ContentValues.TAG, "Error al leer datos: ${error.message}")
            }
        })



        }





}