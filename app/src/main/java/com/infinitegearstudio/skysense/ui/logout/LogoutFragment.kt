package com.infinitegearstudio.skysense.ui.logout

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.infinitegearstudio.skysense.R
import com.infinitegearstudio.skysense.databinding.FragmentHomeBinding
import com.infinitegearstudio.skysense.databinding.FragmentLogoutBinding
import com.infinitegearstudio.skysense.ui.home.HomeViewModel

class LogoutFragment : Fragment() {

    private var _binding: FragmentLogoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val logoutViewModel = ViewModelProvider(this).get(LogoutViewModel::class.java)

        _binding = FragmentLogoutBinding.inflate(inflater, container, false)
        val root: View = binding.root


       logoutViewModel.text.observe(viewLifecycleOwner) {




            var btLogOut = binding.btLogOut


            val etEmail = binding.etConEmail
            val etPass = binding.edPass

            // Recuperar datos de SharedPreferences
            // Recuperar datos de SharedPreferences usando requireContext()
            val sharedPreferences = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)

            val email = sharedPreferences.getString("email", "")
            val provider = sharedPreferences.getString("provider", "")



            etEmail.setText(email)
            etPass.setText(provider)

            logoutViewModel.text.observe(viewLifecycleOwner, Observer { newText ->
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}