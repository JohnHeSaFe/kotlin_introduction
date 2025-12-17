package com.example.intro_proyecto_dam2.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.intro_proyecto_dam2.Nurse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NurseViewModel : ViewModel() {

    private val exampleNurses = listOf(
        Nurse(id=1,first_name = "Justin", last_name = "Suarez", email = "zjs.suarez@asd.com",password="asd1"),
        Nurse(id=2,first_name = "John Henard", last_name= "Henard", email = "henardsalango@asd.com",password="asd2"),
        Nurse(id=3, first_name = "Angelo Pozo", last_name = "Pozo" ,email = "apozonigga@asd.com",password="asd3"),
        Nurse(id=4, first_name = "Marc Munta", last_name ="Munta", email = "marcmunta@asd.com",password="asd4")
    )

    private val _nurseList = MutableStateFlow(exampleNurses)
    val nurseList = _nurseList.asStateFlow()

    // 1. Add state for the query text
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    // 2. Modified function to update text AND filter list
    fun onSearchTextChange(text: String) {
        _searchText.value = text

        // Filter logic
        if (text.isEmpty()) {
            _nurseList.value = exampleNurses
        } else {
            _nurseList.value = exampleNurses.filter { nurse ->
                (nurse.first_name + " " + nurse.last_name).contains(text, ignoreCase = true) ||
                        nurse.email.contains(text, ignoreCase = true)
            }
        }
    }
}