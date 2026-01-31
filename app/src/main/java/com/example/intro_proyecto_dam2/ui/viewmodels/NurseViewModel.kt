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

    enum class RegisterError {
        EMAIL_EXISTS,
        INVALID_RESPONSE,
        SERVER_ERROR,
        NETWORK_ERROR
    }

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

    private fun updateLocalCache(updated: Nurse) {
        val index = _allNurses.indexOfFirst { it.id == updated.id }
        if (index >= 0) {
            _allNurses[index] = updated
        } else {
            _allNurses.add(updated)
        }
        onSearchTextChange(_searchText.value)
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
    fun register(nurse: Nurse, onSuccess: () -> Unit, onError: (RegisterError) -> Unit) {
        if (_allNurses.any { it.email.equals(nurse.email, ignoreCase = true) }) {
            onError(RegisterError.EMAIL_EXISTS)
            return
        }

        viewModelScope.launch {
            try {
                val payload = mutableMapOf(
                    "email" to nurse.email,
                    "password" to nurse.password,
                    "first_name" to nurse.first_name,
                    "last_name" to nurse.last_name
                )
                if (nurse.id > 0) {
                    payload["nurse_id"] = nurse.id.toString()
                }

                val response = RetrofitInstance.api.register(payload)
                val body = response.body()
                if (response.isSuccessful) {
                    val created = buildNurseFromApi(body, nurse)
                    if (created.id != 0) {
                        currentUser = created
                        updateLocalCache(created)
                        onSuccess()
                    } else {
                        onError(RegisterError.INVALID_RESPONSE)
                    }
                } else {
                    if (response.code() == 409) {
                        onError(RegisterError.EMAIL_EXISTS)
                    } else {
                        onError(RegisterError.SERVER_ERROR)
                    }
                }
            } catch (e: Exception) {
                onError(RegisterError.NETWORK_ERROR)
            }
        }
    }



    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(mapOf("email" to email, "password" to pass))
                val data = response.body()
                val authenticated = parseBoolean(data?.get("authenticated"))
                if (response.isSuccessful && authenticated != false) {
                    val fallback = Nurse(
                        id = 0,
                        first_name = "",
                        last_name = "",
                        email = email,
                        password = pass,
                        profile_picture = null
                    )
                    val user = buildNurseFromApi(data, fallback)
                    if (user.id != 0) {
                        currentUser = user
                        updateLocalCache(user)
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

    fun updateProfile(nurse: Nurse, onSuccess: () -> Unit, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                // Call PUT /nurse/{id}
                val response = RetrofitInstance.api.updateNurse(nurse.id, nurse)
                if (response.isSuccessful) {
                    currentUser = nurse // Update local UI
                    updateLocalCache(nurse)
                    onSuccess()
                } else {
                    onError("Update failed")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(e.message ?: "Update failed")
            }
        }
    }

    fun deleteAccount(onSuccess: () -> Unit, onError: (String) -> Unit = {}) {
        val id = currentUser?.id ?: return
        viewModelScope.launch {
            try {
                // Call DELETE /nurse/{id}
                val response = RetrofitInstance.api.deleteNurse(id)
                if (response.isSuccessful) {
                    currentUser = null // Close session
                    onSuccess()
                } else {
                    onError("Delete failed")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(e.message ?: "Delete failed")
            }
        }
    }

    fun updateProfilePicture(base64Image: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val user = currentUser ?: run {
            onError("No active session")
            return
        }
        val updated = user.copy(profile_picture = base64Image)
        updateProfile(updated, onSuccess, onError)
    }

    fun deleteProfilePicture(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val user = currentUser ?: run {
            onError("No active session")
            return
        }
        val updated = user.copy(profile_picture = null)
        updateProfile(updated, onSuccess, onError)
    }

    fun logout() {
        currentUser = null
    }

    private fun buildNurseFromApi(data: Map<String, Any>?, fallback: Nurse): Nurse {
        if (data == null) return fallback

        val id = parseInt(
            data["nurse_id"] ?: data["id"] ?: data["user_id"]
        ) ?: fallback.id

        val firstName = data["first_name"] as? String
            ?: data["firstName"] as? String
            ?: fallback.first_name
        val lastName = data["last_name"] as? String
            ?: data["lastName"] as? String
            ?: fallback.last_name
        val email = data["email"] as? String ?: fallback.email
        val profilePicture = data["profile_picture"] as? String
            ?: data["profilePicture"] as? String
            ?: fallback.profile_picture

        return fallback.copy(
            id = id,
            first_name = firstName,
            last_name = lastName,
            email = email,
            profile_picture = profilePicture
        )
    }

    private fun parseInt(value: Any?): Int? {
        return when (value) {
            is Number -> value.toInt()
            is String -> value.toIntOrNull()
            else -> null
        }
    }

    private fun parseBoolean(value: Any?): Boolean? {
        return when (value) {
            is Boolean -> value
            is Number -> value.toInt() != 0
            is String -> value.equals("true", ignoreCase = true) || value == "1"
            else -> null
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
