package com.example.fittrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fittrack.data.local.AppDatabase
import com.example.fittrack.data.repository.WorkoutRepository
import com.example.fittrack.viewmodel.WorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(userId: String, onDone: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { WorkoutRepository(AppDatabase.getInstance(context).workoutDao()) }
    val viewModel: WorkoutViewModel = viewModel(factory = WorkoutViewModel.Factory(repository, userId))

    var type by remember { mutableStateOf("Running") }
    var duration by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var steps by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text("Log Workout") }) }) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Workout Type") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = duration, onValueChange = { duration = it }, label = { Text("Duration (minutes)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = calories, onValueChange = { calories = it }, label = { Text("Calories burned") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = steps, onValueChange = { steps = it }, label = { Text("Steps") }, modifier = Modifier.fillMaxWidth())

            Button(
                onClick = {
                    viewModel.addWorkout(
                        type = type,
                        durationMinutes = duration.toIntOrNull() ?: 0,
                        caloriesBurned = calories.toIntOrNull() ?: 0,
                        steps = steps.toIntOrNull() ?: 0
                    )
                    onDone()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Workout")
            }
        }
    }
}
