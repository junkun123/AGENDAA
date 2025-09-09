package com.example.agenda11

import androidx.lifecycle.*
// import androidx.lifecycle.Transformations // Esta línea fue eliminada
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotaViewModel(private val dao: NotaDao) : ViewModel() {

    private val _searchQuery = MutableLiveData<String>("")

    val notas: LiveData<List<Nota>> = _searchQuery.switchMap { query ->
        if (query.isNullOrEmpty()) {
            dao.getAllNotas()
        } else {
            dao.searchNotas("%$query%")
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun insert(nota: Nota) = viewModelScope.launch(Dispatchers.IO) { dao.insert(nota) }
    fun update(nota: Nota) = viewModelScope.launch(Dispatchers.IO) { dao.update(nota) }
    fun delete(nota: Nota) = viewModelScope.launch(Dispatchers.IO) { dao.delete(nota) }
    fun getNotaById(id: Int): LiveData<Nota> = dao.getNotaById(id)
}
