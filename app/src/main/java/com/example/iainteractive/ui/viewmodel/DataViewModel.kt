package com.example.iainteractive.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import com.example.iainteractive.data.repository.DataRepository
import com.example.iainteractive.data.model.VideogameModel

class DataViewModel(private val repository: DataRepository) : ViewModel() {

    private val _games = MutableLiveData<List<VideogameModel>>()
    private val _selectedGameId = MutableLiveData<String>()
    val selectedGameId: LiveData<String> get() = _selectedGameId
    val games: LiveData<List<VideogameModel>> get() = _games

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchGames() {
        viewModelScope.launch {
            try {
                val gamesList = repository.getGames()
                _games.value = gamesList
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }

    fun selectGame(id: Int) {
        _selectedGameId.value = id.toString()
    }
}
