package com.example.yadrocontacts.presentation.screens.contactsList

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.yadrocontacts.presentation.screens.contactsList.components.ContactCard
import com.example.yadrocontacts.presentation.screens.contactsList.components.DeleteContactsFAB
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.yadrocontacts.presentation.screens.contactsList.entity.UiEvent
import com.example.yadrocontacts.presentation.screens.contactsList.entity.UiState


@Composable
fun ContactListScreen(
    viewModel: ContactListViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val ui by viewModel.ui.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    when(ui){
        is UiState.Success -> {
            Scaffold(
                floatingActionButton = { DeleteContactsFAB(onClick = {
                    viewModel.deleteRepeatContacts()
                }) },
                floatingActionButtonPosition = FabPosition.Center
            ) {
                    paddingValues ->
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(9.dp)
                    .padding(top = 20.dp)
                    .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    LazyColumn() {
                        items((ui as UiState.Success).contacts.size){
                            ContactCard(contact = (ui as UiState.Success).contacts[it])
                        }
                    }

                }
            }
        }
        is UiState.Error ->{}
        is UiState.Loading -> {}
    }


}
