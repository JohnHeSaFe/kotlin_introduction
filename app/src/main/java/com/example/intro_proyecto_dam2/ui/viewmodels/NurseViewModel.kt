package com.example.intro_proyecto_dam2.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intro_proyecto_dam2.Nurse
import com.example.intro_proyecto_dam2.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class NurseViewModel : ViewModel() {


    private val _allNurses = mutableListOf<Nurse>()

    private val _nurseList = MutableStateFlow<List<Nurse>>(emptyList())
    val nurseList = _nurseList.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    var currentUser by mutableStateOf<Nurse?>(null)
        private set

    init {
        fetchNurses()
    }

    fun fetchNurses() {
        viewModelScope.launch {

            try {
                val response = RetrofitInstance.api.getAllNurses()
                _allNurses.addAll(response)
                _nurseList.value = response
                onSearchTextChange(_searchText.value)
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



    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(mapOf("email" to email, "password" to pass))
                if (response.isSuccessful && response.body()?.get("authenticated") == true) {
                    val data = response.body()!!

                    // Save received data in the varaible currentUser
                    currentUser = Nurse(
                        id = (data["nurse_id"] as? Number)?.toInt() ?: 0,
                        first_name = data["first_name"] as? String ?: "",
                        last_name = data["last_name"] as? String ?: "",
                        email = data["email"] as? String ?: "",
                        password = pass,
                        profile_picture = data["profile_picture"] as? String
                    )
                    if (currentUser?.id != 0) {
                        onSuccess()
                    } else {
                        onError()
                    }
                } else {
                    onError()
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }

    fun updateProfile(nurse: Nurse, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                // Call PUT /nurse/{id}
                val response = RetrofitInstance.api.updateNurse(nurse.id, nurse)
                if (response.isSuccessful) {
                    currentUser = nurse // Update local UI
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteAccount(onSuccess: () -> Unit) {
        val id = currentUser?.id ?: return
        viewModelScope.launch {
            try {
                // Call DELETE /nurse/{id}
                val response = RetrofitInstance.api.deleteNurse(id)
                if (response.isSuccessful) {
                    currentUser = null // Close session
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun logout() {
        currentUser = null
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