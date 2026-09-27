package com.example.fittrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fittrack.auth.AuthViewModel

@Composable
fun ProfileScreen(authViewModel: AuthViewModel, onLoggedOut: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("User ID: ${authViewModel.currentUserId ?: "unknown"}", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(24.dp))
        Button(onClick = {
            authViewModel.logout()
            onLoggedOut()
        }) {
            Text("Log Out")
        }
    }
}
