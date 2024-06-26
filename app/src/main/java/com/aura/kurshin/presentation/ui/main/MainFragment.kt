package com.aura.kurshin.presentation.ui.main

import android.content.Context
import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.aura.kurshin.databinding.FragmentMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveDismiss.setOnClickListener {
            val intent = Intent("com.example.myapp.CUSTOM_ACTION")
            requireActivity().sendBroadcast(intent)
        }

        updateUI()
    }

    private fun updateUI() {
        val sharedPreferences = requireActivity().getSharedPreferences("BootEvents", Context.MODE_PRIVATE)
        val bootCount = sharedPreferences.getInt("boot_count", 0)
        val bootEventsText = when {
            bootCount == 0 -> "No boots detected"
            else -> {
                val sb = StringBuilder()
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                for (i in 1..bootCount) {
                    val bootTime = sharedPreferences.getString("boot_time_$i", "") ?: ""
                    val count = sharedPreferences.getInt("boot_count_$i", 0)
                    sb.append("● $bootTime - $count\n")
                }
                sb.toString().trim()
            }
        }
        binding.message.text = bootEventsText
    }
}