package com.example.fittrack.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fittrack.data.local.AppDatabase
import com.example.fittrack.data.repository.WorkoutRepository
import com.example.fittrack.sensor.StepCounterManager
import com.example.fittrack.viewmodel.WorkoutViewModel

@Composable
fun HomeScreen(
    userId: String,
    onAddWorkout: () -> Unit,
    onOpenProfile: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { WorkoutRepository(AppDatabase.getInstance(context).workoutDao()) }
    val viewModel: WorkoutViewModel = viewModel(factory = WorkoutViewModel.Factory(repository, userId))

    val stepManager = remember { StepCounterManager(context) }
    DisposableEffect(Unit) {
        stepManager.start()
        onDispose { stepManager.stop() }
    }
    val liveSteps by stepManager.steps

    val workouts by viewModel.workouts.collectAsState()
    val totalSteps by viewModel.totalSteps.collectAsState()
    val totalCalories by viewModel.totalCalories.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FitTrack") },
                actions = {
                    IconButton(onClick = onOpenProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddWorkout) {
                Icon(Icons.Default.Add, contentDescription = "Add workout")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            // Hardware sensor card
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Live Steps Today (sensor)", style = MaterialTheme.typography.titleMedium)
                    Text(
                        if (stepManager.isSensorAvailable) "$liveSteps steps"
                        else "Step sensor not available on this device",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatCard("Logged Steps", "$totalSteps")
                StatCard("Calories", "$totalCalories kcal")
            }

            Spacer(Modifier.height(16.dp))
            Text("Workout History", style = MaterialTheme.typography.titleMedium)

            LazyColumn {
                items(workouts) { workout ->
                    ListItem(
                        headlineContent = { Text(workout.type) },
                        supportingContent = { Text("${workout.durationMinutes} min · ${workout.caloriesBurned} kcal · ${workout.steps} steps") }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String) {
    Card(modifier = Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium)
            Text(value, style = MaterialTheme.typography.titleLarge)
        }
    }
}
