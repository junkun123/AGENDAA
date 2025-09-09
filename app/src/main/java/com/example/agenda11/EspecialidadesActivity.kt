package com.example.agenda11

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agenda11.databinding.ActivityEspecialidadesBinding

class EspecialidadesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEspecialidadesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEspecialidadesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar listeners para los botones de especialidades
        binding.buttonDesarrolloSoftware.setOnClickListener {
            abrirLink("https://sites.google.com/inti.edu.sv/nuevoingreso/OfertaAcademica/desarrollo_de_software?authuser=0")
        }
        binding.buttonElectronica.setOnClickListener {
            abrirLink("https://sites.google.com/inti.edu.sv/nuevoingreso/OfertaAcademica/electronica?authuser=0")
        }
        binding.buttonSistemasElectricos.setOnClickListener {
            abrirLink("https://sites.google.com/inti.edu.sv/nuevoingreso/OfertaAcademica/sistemas_electricos?authuser=0")
        }
        binding.buttonMecanicaAutomotriz.setOnClickListener {
            abrirLink("https://sites.google.com/inti.edu.sv/nuevoingreso/OfertaAcademica/mantenimiento_automotriz?authuser=0")
        }
        binding.buttonMecanicaIndustrial.setOnClickListener {
            abrirLink("https://sites.google.com/inti.edu.sv/nuevoingreso/OfertaAcademica/mecanica_industrial?authuser=0") // Considera si esta URL es la misma para Mecánica Industrial o necesita una diferente
        }
        binding.buttonInfraestructuraTecnologica.setOnClickListener { // Este ahora es "Infraestructura y Sistemas Informáticos"
            abrirLink("https://sites.google.com/inti.edu.sv/nuevoingreso/OfertaAcademica/infraestructura_tecnologica_y_servicios_informaticos?authuser=0") // Reemplaza con la URL combinada
        }

        binding.buttonMatriculateYa.setOnClickListener {
            abrirLink("https://sites.google.com/inti.edu.sv/nuevoingreso/nuevo-ingreso/proceso_de_matricula?authuser=0") // Reemplaza con la URL de matrícula real
        }

        // Configurar listener para el botón de Facebook
        binding.buttonFacebook.setOnClickListener {
            abrirLink("https://www.facebook.com/tecnicosinti") // Reemplaza con tu URL de Facebook
        }
    }

    private fun abrirLink(url: String) {
        if (url.startsWith("URL_")) {
            Toast.makeText(this, "URL no configurada: " + url, Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "No se puede abrir el enlace", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}
