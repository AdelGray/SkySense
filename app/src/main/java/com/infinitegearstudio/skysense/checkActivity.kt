package com.infinitegearstudio.skysense

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

enum class ProviderType{
    BASIC,
    GOOGLE
}

class checkActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)

        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        val provider: String? = bundle?.getString("provider")

        setup(email?: "",provider?: "")

        //GUARDAR DATOS

        val prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider",provider)
        prefs.apply()
    }

    private fun setup(email:String,provider:String){

        val btLogOut = findViewById<Button>(R.id.btLogOut)

        val etEmail = findViewById<EditText>(R.id.etConEmail)
        val etPassword = findViewById<EditText>(R.id.etConPassword)

        val btSave = findViewById<Button>(R.id.btSave)

        val etName = findViewById<EditText>(R.id.etName)
        val etPais = findViewById<EditText>(R.id.etPais)
        val etCiudad = findViewById<EditText>(R.id.etCiudad)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)


        title="inicio"

        etEmail.setText(email)
        etPassword.setText(provider)

        btLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()


            val intent = Intent(this,AuthActivity::class.java)
            startActivity(intent)
            finish()
        }

        btSave.setOnClickListener {


            val userData = hashMapOf(
                "provider" to provider,
                "name" to etName.text.toString(),
                "country" to etPais.text.toString(),
                "city" to etCiudad.text.toString(),
                "phone" to etTelefono.text.toString()
            )


            db.collection("users").document(email).set(userData)
                .addOnSuccessListener {
                    // Éxito al guardar en la base de datos
                    showAlert("Registro Exitoso", "Datos guardados Correctamente")
                    val intent = Intent(this,HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    // Manejar errores aquí, si es necesario
                    Log.e(TAG, "Error al guardar en la base de datos", e)
                }
        }



    }

    private fun showAlert(tittle:String,message:String){

        val builder= AlertDialog.Builder(this)
        builder.setTitle((title))
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}