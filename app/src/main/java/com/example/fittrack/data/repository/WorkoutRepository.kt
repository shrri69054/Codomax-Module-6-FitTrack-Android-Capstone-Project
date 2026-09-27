package com.example.fittrack.data.repository

import com.example.fittrack.data.local.WorkoutDao
import com.example.fittrack.data.local.WorkoutEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository pattern: single source of truth that the ViewModel talks to,
 * hiding whether data comes from Room, a network call, or a cache.
 */
class WorkoutRepository(private val dao: WorkoutDao) {

    fun getWorkouts(userId: String): Flow<List<WorkoutEntity>> = dao.getWorkoutsForUser(userId)

    fun getTotalSteps(userId: String): Flow<Int?> = dao.getTotalSteps(userId)

    fun getTotalCalories(userId: String): Flow<Int?> = dao.getTotalCalories(userId)

    suspend fun addWorkout(workout: WorkoutEntity) = dao.insert(workout)

    suspend fun deleteWorkout(workout: WorkoutEntity) = dao.delete(workout)

    suspend fun updateWorkout(workout: WorkoutEntity) = dao.update(workout)
}
