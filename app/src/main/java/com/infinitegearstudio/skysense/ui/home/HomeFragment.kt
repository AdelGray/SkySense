package com.infinitegearstudio.skysense.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.infinitegearstudio.skysense.R
import com.infinitegearstudio.skysense.databinding.FragmentHomeBinding
import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.infinitegearstudio.skysense.AuthActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root




        homeViewModel.text.observe(viewLifecycleOwner) {




            var btLogOut = binding.btLogOut

            val etPassword = binding.etConPassword
            val etEmail = binding.etConEmail

            // Recuperar datos de SharedPreferences
            // Recuperar datos de SharedPreferences usando requireContext()
            val sharedPreferences = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)

            val email = sharedPreferences.getString("email", "")
            val provider = sharedPreferences.getString("provider", "")



            etEmail.setText(email)
            etPassword.setText(provider)

            homeViewModel.text.observe(viewLifecycleOwner, Observer { newText ->
                // Actualiza la interfaz de usuario cuando cambia el LiveData
                btLogOut.text = newText
            })




            // Configura un listener de clic para el botón
            btLogOut.setOnClickListener {
                // Realiza la acción deseada cuando se hace clic en el botón
                // Puedes actualizar el LiveData aquí si es necesario
                val sharedPreferences = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                FirebaseAuth.getInstance().signOut()

                sharedPreferences.clear()
                sharedPreferences.apply()
            }


        }
        return root



        
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}