package com.example.agenda11

import android.content.Intent
import android.os.Bundle
import android.view.View // Importar View para View.GONE y View.VISIBLE
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agenda11.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val notaDao by lazy { NotaDatabase.getDatabase(this).notaDao() }
    private val notaViewModel: NotaViewModel by viewModels { NotaViewModelFactory(notaDao) }
    private lateinit var notaAdapter: NotaAdapter
    private var isNotasVisible = false // Estado para rastrear la visibilidad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración inicial de visibilidad (aunque el XML ya lo hace, es una buena práctica)
        binding.recyclerViewNotas.visibility = View.GONE
        binding.btnToggleVisibility.text = "Mostrar" // Texto inicial del botón

        notaAdapter = NotaAdapter(
            onEditClick = { notaParaEditar ->
                val intent = Intent(this, EditNotaActivity::class.java)
                intent.putExtra("notaId", notaParaEditar.id)
                startActivity(intent)
            },
            onDeleteClick = { notaParaBorrar ->
                mostrarDialogoConfirmacionBorrado(notaParaBorrar)
            }
        )

        binding.recyclerViewNotas.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewNotas.adapter = notaAdapter

        notaViewModel.notas.observe(this) { notas ->
            notaAdapter.setNotas(notas)

            val currentQuery = binding.searchView.query.toString()
            if (currentQuery.isNotEmpty() && notas.size == 1) {
                // Solo expandir si las notas son visibles
                if (isNotasVisible) {
                    notaAdapter.expandItem(0)
                }
            } else if (currentQuery.isNotEmpty() && notas.isEmpty()) {
                notaAdapter.collapseExpandedItem()
            } else if (currentQuery.isEmpty()) {
                notaAdapter.collapseExpandedItem()
            }
        }

        binding.fabAgregar.setOnClickListener {
            startActivity(Intent(this, EditNotaActivity::class.java))
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText ?: ""
                notaViewModel.setSearchQuery(query)
                return true
            }
        })

        // NUEVO: Listener para el botón de mostrar/ocultar
        binding.btnToggleVisibility.setOnClickListener {
            isNotasVisible = !isNotasVisible // Alternar el estado
            if (isNotasVisible) {
                binding.recyclerViewNotas.visibility = View.VISIBLE
                binding.btnToggleVisibility.text = "Ocultar"
                // Si hay una búsqueda activa con un solo resultado, y ahora estamos mostrando las notas, expandirla.
                val currentQuery = binding.searchView.query.toString()
                val currentNotas = notaViewModel.notas.value ?: emptyList()
                if (currentQuery.isNotEmpty() && currentNotas.size == 1) {
                    notaAdapter.expandItem(0)
                }
            } else {
                binding.recyclerViewNotas.visibility = View.GONE
                binding.btnToggleVisibility.text = "Mostrar"
                notaAdapter.collapseExpandedItem() // Colapsar cualquier ítem expandido al ocultar
            }
        }

        // Manejar el botón de retroceso para volver a CajonActivity
        onBackPressedDispatcher.addCallback(this) {
            val intent = Intent(this@MainActivity, CajonActivity::class.java)
            // Asegúrate de que CajonActivity sea la única tarea en la pila de retroceso
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish() // Cierra MainActivity
        }
    }

    private fun mostrarDialogoConfirmacionBorrado(nota: Nota) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Borrado")
            .setMessage("¿Estás seguro de que quieres borrar la nota \"${nota.titulo}\"?")
            .setPositiveButton("Borrar") { _, _ ->
                notaViewModel.delete(nota)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
