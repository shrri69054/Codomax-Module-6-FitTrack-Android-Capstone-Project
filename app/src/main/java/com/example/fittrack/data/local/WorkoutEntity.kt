package com.example.fittrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Local database model for a single logged workout session.
 */
@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val type: String,          // e.g. "Running", "Cycling", "Yoga"
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val steps: Int,
    val dateMillis: Long
)
