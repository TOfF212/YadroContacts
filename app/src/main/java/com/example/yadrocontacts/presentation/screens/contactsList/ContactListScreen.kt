package com.example.yadrocontacts.presentation.screens.contactsList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.yadrocontacts.presentation.screens.contactsList.components.ContactCard
import com.example.yadrocontacts.presentation.screens.contactsList.components.DeleteContactsFAB

@Preview
@Composable
fun ContactListScreen() {
    Scaffold(
        floatingActionButton = { DeleteContactsFAB() },
        floatingActionButtonPosition = FabPosition.Center
    ) {
            paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(9.dp)
            .padding(top = 20.dp)
            .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Column() {
                ContactCard()
                ContactCard()
                ContactCard()
                ContactCard()
                ContactCard()
            }
        }
    }

}