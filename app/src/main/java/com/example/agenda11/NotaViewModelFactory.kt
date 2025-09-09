package com.example.agenda11

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NotaViewModelFactory(private val dao: NotaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotaViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
