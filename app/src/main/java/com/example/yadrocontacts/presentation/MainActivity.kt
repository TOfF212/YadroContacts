package com.example.yadrocontacts.presentation

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.yadrocontacts.presentation.screens.contactsList.ContactListScreen
import com.example.yadrocontacts.ui.theme.YadroContactsTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YadroContactsTheme {
                val permissionState by viewModel.isPermissionGranted.collectAsState()

                when (permissionState) {
                false -> {
                    MultiplePermissions(viewModel)
                }
                true -> {
                    ContactListScreen()
                }
            }
            }
            }
        }
    }

@Composable
fun MultiplePermissions(viewModel: MainViewModel) {



    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permission ->
            viewModel.onPermissionResult(permission)
        },
    )

    Column (horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()) {
        Text(text = "Пожалуйста, предоставьте разрешения, без них приложение не будет работать",
            modifier = Modifier.padding(20.dp))
        Button(onClick = {launcher.launch(viewModel.permissionsList)}){
            Text(text = "Предоставить разрешения")
        }
    }
}

