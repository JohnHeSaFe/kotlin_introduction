package com.example.intro_proyecto_dam2.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.intro_proyecto_dam2.Nurse
import com.example.intro_proyecto_dam2.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NurseViewModel : ViewModel() {

    private val _allNurses = mutableListOf(
        Nurse(id=1,first_name = "Justin", last_name = "Suarez", email = "zjs.suarez@asd.com",password="asd1", profile_picture = R.drawable.asd),
        Nurse(id=2,first_name = "John", last_name= "Salango", email = "henardsalango@asd.com",password="asd2", profile_picture = R.drawable.dawdsawdsawd),
        Nurse(id=3, first_name = "Angelo", last_name = "Pozo" ,email = "apozonigga@asd.com",password="asd3", profile_picture = R.drawable.dsadasd),
        Nurse(id=4, first_name = "Marc", last_name ="Munta", email = "marcmunta@asd.com",password="asd4", profile_picture = R.drawable.wdsawdsad)
    )

    private val _nurseList = MutableStateFlow<List<Nurse>>(_allNurses)
    val nurseList = _nurseList.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

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

    fun login(email: String, pass: String): Boolean {
        return _allNurses.any {
            it.email.equals(email, ignoreCase = true) && it.password == pass
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