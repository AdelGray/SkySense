package com.infinitegearstudio.skysense.ui.aboutus

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.infinitegearstudio.skysense.R
import com.infinitegearstudio.skysense.databinding.FragmentAboutusBinding
import com.infinitegearstudio.skysense.databinding.FragmentSettingsBinding

class AboutusFragment : Fragment() {

        private var _binding: FragmentAboutusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAboutusBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}