package com.example.agenda11

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agenda11.databinding.ActivityRegistroBinding

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonConfirmarRegistro.setOnClickListener {
            val nuevoUsuario = binding.editTextNuevoUsuario.text.toString().trim()
            val nuevaContrasena = binding.editTextNuevaContrasena.text.toString().trim()

            if (nuevoUsuario.isEmpty() || nuevaContrasena.isEmpty()) {
                Toast.makeText(this, getString(R.string.registro_error_campos_vacios), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPreferences = getSharedPreferences("UsuariosAgendaApp", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            // 1. Obtener el Set actual. Si no existe, es un Set vacío.
            val usuariosActuales = sharedPreferences.getStringSet("lista_usuarios", HashSet<String>()) ?: HashSet<String>()

            // 2. Crear una NUEVA COPIA MODIFICABLE del Set para trabajar con ella.
            val copiaMutableUsuarios = HashSet<String>(usuariosActuales)

            // 3. Comprobar si el usuario ya existe en la copia.
            //    Se busca cualquier credencial que comience con "nombredeusuario:"
            if (copiaMutableUsuarios.any { it.startsWith("$nuevoUsuario:") }) {
                Toast.makeText(this, getString(R.string.registro_error_usuario_existe), Toast.LENGTH_SHORT).show()
            } else {
                // 4. Añadir la nueva credencial a la NUEVA COPIA.
                val nuevaCredencial = "$nuevoUsuario:$nuevaContrasena"
                copiaMutableUsuarios.add(nuevaCredencial)

                // 5. Guardar la NUEVA COPIA del Set en SharedPreferences.
                editor.putStringSet("lista_usuarios", copiaMutableUsuarios)
                editor.apply() // apply() es asíncrono, commit() es síncrono.
                               // Para asegurar la escritura podrías probar commit() si apply() sigue dando problemas.

                Toast.makeText(this, getString(R.string.registro_exitoso), Toast.LENGTH_SHORT).show()
                finish() // Cierra RegistroActivity y vuelve a LoginActivity
            }
        }
    }
}
