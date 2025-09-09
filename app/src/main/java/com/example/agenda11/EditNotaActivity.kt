package com.example.agenda11

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.agenda11.databinding.ActivityEditNotaBinding // Asegúrate de que este binding esté regenerado si cambiaste IDs

class EditNotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNotaBinding
    private val notaDao by lazy { NotaDatabase.getDatabase(this).notaDao() }
    private val notaViewModel: NotaViewModel by viewModels { NotaViewModelFactory(notaDao) }
    private var notaId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notaId = intent.getIntExtra("notaId", -1).takeIf { it != -1 }

        if (notaId != null) {
            notaViewModel.getNotaById(notaId!!).observe(this) { nota ->
                nota?.let {
                    binding.editNombreNota.setText(it.titulo)
                    binding.editNombre.setText(it.nombre)
                    binding.editTelefono.setText(it.telefono)
                    // Aquí deberías popular el campo de correo si estás editando
                    binding.editCorreoElectronico.setText(it.correoElectronico) // NUEVO: Popular correo
                    binding.editDescripcion.setText(it.descripcion)
                }
            }
        }

        binding.btnGuardar.setOnClickListener {
            val titulo = binding.editNombreNota.text.toString()
            val nombre = binding.editNombre.text.toString()
            val telefono = binding.editTelefono.text.toString()
            val correo = binding.editCorreoElectronico.text.toString() // NUEVO: Leer correo
            val descripcion = binding.editDescripcion.text.toString()

            // Validación básica (puedes expandirla)
            if (titulo.isBlank() || nombre.isBlank()) {
                // Mostrar algún mensaje de error al usuario, por ejemplo, con un Toast o Snackbar
                // Toast.makeText(this, "Título y Nombre no pueden estar vacíos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // No continuar si los campos requeridos están vacíos
            }

            val nota = Nota(
                id = notaId ?: 0, // Si notaId es null (nueva nota), usa 0 o tu valor por defecto para autogeneración
                titulo = titulo,
                nombre = nombre,
                telefono = telefono,
                correoElectronico = correo, // NUEVO: Pasar correo
                descripcion = descripcion
            )

            if (notaId != null) {
                notaViewModel.update(nota)
            } else {
                notaViewModel.insert(nota)
            }
            finish()
        }
    }
}
