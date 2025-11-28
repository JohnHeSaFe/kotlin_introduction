package com.example.intro_proyecto_dam2


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.ListItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Person


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp





@Preview(showBackground = true )
@Composable
fun SearchNursePreview() {
    SearchNurse(onNurseClick = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchNurse(onNurseClick: (Int) -> Unit) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    SearchBar(
        query = query,
        onQueryChange = { query = it },
        onSearch = { active = false },
        active = active,
        onActiveChange = { active = it },
        placeholder = { Text("Search Nurse...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (active) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    modifier = Modifier.clickable {
                        if (query.isNotEmpty()) {
                            query = ""
                        } else {
                            active = false
                        }
                    }
                )
            }
        }
    ) {
        val filteredNurses = Nurses.filter { Nurse ->
            Nurse.name.contains(query, ignoreCase = true) ||
                    Nurse.email.contains(query, ignoreCase = true)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredNurses) { nurse ->
                ListItem(
                    headlineContent = { Text(nurse.name) },
                    supportingContent = { Text(nurse.email) },
                    leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.clickable {
                        query = nurse.name
                        active = false
                        onNurseClick(nurse.id)
                    }
                )
            }
        }
    }
}
