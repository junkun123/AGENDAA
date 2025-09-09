package com.example.agenda11

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotaAdapter(
    private val onEditClick: (Nota) -> Unit,
    private val onDeleteClick: (Nota) -> Unit
) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {

    private var notas = listOf<Nota>()
    private var expandedPosition = RecyclerView.NO_POSITION

    inner class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvNombreNota)
        val layoutDetalles: View = itemView.findViewById(R.id.layoutDetalles)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvTelefono: TextView = itemView.findViewById(R.id.tvTelefono)
        val tvCorreoElectronico: TextView = itemView.findViewById(R.id.tvCorreoElectronico)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val btnEditarNota: ImageButton = itemView.findViewById(R.id.btnEditarNota)
        val btnBorrarNota: ImageButton = itemView.findViewById(R.id.btnBorrarNota)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nota, parent, false)
        return NotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val nota = notas[position]
        holder.tvTitulo.text = nota.titulo
        holder.tvNombre.text = "Nombre: ${nota.nombre}"
        holder.tvTelefono.text = "Tel: ${nota.telefono}"
        holder.tvCorreoElectronico.text = "Correo: ${nota.correoElectronico}"
        holder.tvDescripcion.text = nota.descripcion

        val isExpanded = position == expandedPosition
        holder.layoutDetalles.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.btnEditarNota.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.btnBorrarNota.visibility = if (isExpanded) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            val previousExpandedPosition = expandedPosition
            expandedPosition = if (isExpanded) {
                RecyclerView.NO_POSITION
            } else {
                holder.adapterPosition
            }

            if (previousExpandedPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(previousExpandedPosition)
            }
            if (expandedPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(expandedPosition)
            }
        }

        holder.btnEditarNota.setOnClickListener {
            onEditClick(nota)
        }

        holder.btnBorrarNota.setOnClickListener {
            onDeleteClick(nota)
        }
    }

    override fun getItemCount(): Int = notas.size

    fun setNotas(list: List<Nota>) {
        notas = list
        // Si la posición expandida actualmente está fuera de los límites de la nueva lista, reseteala.
        if (expandedPosition >= notas.size) {
            expandedPosition = RecyclerView.NO_POSITION
        }
        // Si la lista está vacía y algo estaba expandido, resetealo.
        if (notas.isEmpty() && expandedPosition != RecyclerView.NO_POSITION) {
            expandedPosition = RecyclerView.NO_POSITION
        }
        notifyDataSetChanged()
    }

    // NUEVO: Método para expandir un ítem programáticamente
    fun expandItem(position: Int) {
        if (position >= 0 && position < notas.size) {
            if (expandedPosition != position) { // Solo actuar si no es ya el expandido
                val previousExpandedPosition = expandedPosition
                expandedPosition = position
                if (previousExpandedPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousExpandedPosition) // Colapsar el anterior
                }
                notifyItemChanged(expandedPosition) // Expandir el nuevo
            } else {
                // Si ya está expandido y se pide expandir de nuevo (ej. tras un refresh de lista),
                // asegurarse de que se re-renderice correctamente.
                 notifyItemChanged(position)
            }
        }
    }

    // NUEVO: Método para colapsar el ítem actualmente expandido programáticamente
    fun collapseExpandedItem() {
        if (expandedPosition != RecyclerView.NO_POSITION) {
            val previouslyExpanded = expandedPosition
            expandedPosition = RecyclerView.NO_POSITION
            notifyItemChanged(previouslyExpanded)
        }
    }
}
