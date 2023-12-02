package com.infinitegearstudio.skysense

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class termsConditions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_conditions)

        val btTerms = findViewById<Button>(R.id.btTerms)

        btTerms.setOnClickListener {

            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()

        }



    }
}