package com.example.agenda11

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.agenda11.databinding.ActivityLoginBinding
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(this@LoginActivity, RegistroActivity::class.java))
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Error de autenticación: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Autenticación fallida",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        // Modificado para solicitar credenciales del dispositivo (PIN, patrón, contraseña)
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación Requerida")
            .setSubtitle("Usa el PIN, patrón o contraseña de tu dispositivo para continuar")
            .setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL) // Clave del cambio
            // .setNegativeButtonText("Cancelar") // Ya no es necesario con setAllowedAuthenticators para DEVICE_CREDENTIAL, el sistema lo maneja.
            .build()


        binding.buttonIngresar.setOnClickListener {
            val usuarioIngresado = binding.editTextUsuario.text.toString().trim()
            val contrasenaIngresada = binding.editTextContrasena.text.toString().trim()

            if (usuarioIngresado.isEmpty() || contrasenaIngresada.isEmpty()) {
                Toast.makeText(this, getString(R.string.login_error_campos_vacios_login), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPreferences = getSharedPreferences("UsuariosAgendaApp", Context.MODE_PRIVATE)
            val usuariosRegistrados = sharedPreferences.getStringSet("lista_usuarios", HashSet()) ?: HashSet()

            val credencialBuscada = "$usuarioIngresado:$contrasenaIngresada"

            if (usuariosRegistrados.contains(credencialBuscada)) {
                val intent = Intent(this, CajonActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonRegistrar.setOnClickListener {
            val biometricManager = BiometricManager.from(this)
            // Modificado para verificar la capacidad de autenticación con credenciales del dispositivo
            val authenticators = BiometricManager.Authenticators.DEVICE_CREDENTIAL
            
            when (val canAuthResult = biometricManager.canAuthenticate(authenticators)) {
                BiometricManager.BIOMETRIC_SUCCESS -> // Significa que se puede mostrar el prompt para PIN/patrón/contraseña
                    biometricPrompt.authenticate(promptInfo)
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                    // Este error es menos común para DEVICE_CREDENTIAL, pero se mantiene por si acaso.
                    Toast.makeText(this, "No hay soporte para credenciales de dispositivo.", Toast.LENGTH_SHORT).show()
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                    // También menos común para DEVICE_CREDENTIAL.
                    Toast.makeText(this, "Las credenciales de dispositivo no están disponibles actualmente.", Toast.LENGTH_SHORT).show()
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    // Significa que no hay PIN, patrón o contraseña configurados en el dispositivo.
                    Toast.makeText(this, "No tienes un PIN, patrón o contraseña configurado. Por favor, configura uno en los ajustes de tu dispositivo.", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, "Error de autenticación desconocido, código: $canAuthResult", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
