package com.infinitegearstudio.skysense


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


import com.bumptech.glide.Glide



class splashscreen : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 100 // 3 seconds


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        var imageViewSplash = findViewById<ImageView>(R.id.imageViewSplash)

        // Cargar el GIF usando Glide
        Glide.with(this)
            .asGif()
            .load(R.drawable.gearsicon)
            .into(imageViewSplash)

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app's main activity
            //val intent = Intent(this, AuthActivity::class.java)
            //val intent = Intent(this, Settings::class.java)
            //val intent = Intent(this, Pruebas::class.java)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            // Close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }

}