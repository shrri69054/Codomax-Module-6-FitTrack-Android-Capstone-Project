package com.example.fittrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fittrack.data.local.WorkoutEntity
import com.example.fittrack.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val repository: WorkoutRepository,
    private val userId: String
) : ViewModel() {

    val workouts: StateFlow<List<WorkoutEntity>> =
        repository.getWorkouts(userId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalSteps: StateFlow<Int> =
        repository.getTotalSteps(userId).let { flow ->
            MutableStateFlow(0).also { state ->
                viewModelScope.launch { flow.collect { state.value = it ?: 0 } }
            }
        }

    val totalCalories: StateFlow<Int> =
        repository.getTotalCalories(userId).let { flow ->
            MutableStateFlow(0).also { state ->
                viewModelScope.launch { flow.collect { state.value = it ?: 0 } }
            }
        }

    fun addWorkout(type: String, durationMinutes: Int, caloriesBurned: Int, steps: Int) {
        viewModelScope.launch {
            repository.addWorkout(
                WorkoutEntity(
                    userId = userId,
                    type = type,
                    durationMinutes = durationMinutes,
                    caloriesBurned = caloriesBurned,
                    steps = steps,
                    dateMillis = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteWorkout(workout: WorkoutEntity) {
        viewModelScope.launch { repository.deleteWorkout(workout) }
    }

    class Factory(
        private val repository: WorkoutRepository,
        private val userId: String
    ) : androidx.lifecycle.ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WorkoutViewModel(repository, userId) as T
        }
    }
}
