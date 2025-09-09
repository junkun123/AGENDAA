package com.example.agenda11

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.agenda11.databinding.ActividadCajonBinding

class CajonActivity : AppCompatActivity() {

    private lateinit var binding: ActividadCajonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActividadCajonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAbrirAgenda.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.buttonAbrirCalculadora.setOnClickListener {
            val intent = Intent(this, CalculadoraActivity::class.java)
            startActivity(intent)
        }

        binding.buttonAbrirEspecialidades.setOnClickListener { // Asegúrate que el ID del botón coincida
            val intent = Intent(this, EspecialidadesActivity::class.java)
            startActivity(intent)
        }
    }
}
