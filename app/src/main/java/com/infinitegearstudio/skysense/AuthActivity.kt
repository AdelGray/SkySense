package com.infinitegearstudio.skysense

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider


class AuthActivity : AppCompatActivity() {

    private  val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración de firebase completa")

        setup()
        session()
    }

    private fun session(){

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)

        val email:String? = prefs.getString("email",null)
        val provider:String? = prefs.getString("provider",null)

        if(email != null && provider != null){
            showHome(email,ProviderType.valueOf(provider))
        }
    }


    private  fun setup(){

        //Elementos
        val btSignup = findViewById<Button>(R.id.btSignup)
        val btLogin = findViewById<Button>(R.id.btLogin)
        val btGoogle = findViewById<Button>(R.id.btGoogle)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etRePassword = findViewById<EditText>(R.id.etRePassword)

        val cbTerms = findViewById<CheckBox>(R.id.cbTerms)
        val etTerm = findViewById<TextView>(R.id.etTerms)

        title = "Autentificacion"

        etTerm.setOnClickListener {

            val intent = Intent(this, termsConditions::class.java)
            startActivity(intent)
            finish()
        }


        btGoogle.setOnClickListener {

            if (cbTerms.isChecked) {
                val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

                val googleClient: GoogleSignInClient = GoogleSignIn.getClient(this, googleConf)
                googleClient.signOut()
                // Inicia la actividad de inicio de sesión de Google
                val signInIntent = googleClient.signInIntent
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
            }
            else{
                showAlert("Advertencia","Debe aceptar los terminos y condiciones")
            }

        }

        btSignup.setOnClickListener {
            if(etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty() && (etPassword.text.toString() == etRePassword.text.toString()) && cbTerms.isChecked){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(etEmail.text.toString(),etPassword.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        showHome(it.result?.user?.email?:"",ProviderType.BASIC)
                    }
                    else{
                        showAlert("ERROR","Ingrese Los campos y acepte los términos y condiciones")
                    }
                }
            }else{
                showAlert("ERROR","Ingrese Los campos y acepte los términos y condiciones")
            }
        }

            btLogin.setOnClickListener {
                if(etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()){
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(etEmail.text.toString(),etPassword.text.toString()).addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome(it.result?.user?.email?:"",ProviderType.BASIC)
                        }
                        else{
                            showAlert("ERROR","Error de auntentificación")
                        }
                    }
                }
            }

    }

    private fun showAlert(title:String,message:String){

        val builder= AlertDialog.Builder(this)
        builder.setTitle((title))
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email:String,provider:ProviderType){

        val intent = Intent(this, checkActivity::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider.name)
        }

        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            showHome(account.email ?: "", ProviderType.GOOGLE)
                        } else {
                            showAlert("ERROR","Error de auntentificación")
                        }
                    }
                } else {
                    showAlert("ERROR","Error de auntentificación")
                }
            } catch (e: ApiException) {
                showAlert("ERROR","Error de auntentificación")
            }
        } else {
            showAlert("ERROR","Error de auntentificación")
        }
    }


}