package com.example.intro_proyecto_dam2.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intro_proyecto_dam2.Nurse
import com.example.intro_proyecto_dam2.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NurseViewModel : ViewModel() {


    private val _allNurses = mutableListOf<Nurse>()

    init {
        fetchNurses()
    }

    private val _nurseList = MutableStateFlow<List<Nurse>>(_allNurses)
    val nurseList = _nurseList.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()


    fun fetchNurses() {
        viewModelScope.launch {

            try {
                val response = RetrofitInstance.api.getAllNurses()
                _allNurses.addAll(response)
                _nurseList.value = response
            } catch (e: Exception) {
                println("Error: ${e.message}")
            } finally {

            }
        }
    }
    fun generateNextId(): Int {
        return (_allNurses.maxOfOrNull { it.id } ?: 0) + 1
    }

    // Register and login methods
    fun register(nurse: Nurse): Boolean {
        // Validate email repetition
        if (_allNurses.any { it.email.equals(nurse.email, ignoreCase = true) }) {
            return false
        }

        // Add nurse to memory
        _allNurses.add(nurse)

        onSearchTextChange(_searchText.value)

        return true
    }

    suspend fun login(email: String, pass: String): Boolean {
        return try {
            val response = RetrofitInstance.api.login(mapOf("email" to email, "password" to pass))

            val body = response.body()
            if (response.isSuccessful) {
                (body?.get("authenticated") ?: false)
            } else {
                false
            }
        } catch (e: Exception) {
            print(e.message)
            false
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text

        // Filter logic
        if (text.isEmpty()) {
            _nurseList.value = _allNurses.toList()
        } else {
            _nurseList.value = _allNurses.filter { nurse ->
                (nurse.first_name + " " + nurse.last_name).contains(text, ignoreCase = true) ||
                        nurse.email.contains(text, ignoreCase = true)
            }
        }
    }


}