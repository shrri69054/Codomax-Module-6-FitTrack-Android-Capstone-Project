package com.example.fittrack

import com.example.fittrack.data.local.WorkoutEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class WorkoutEntityTest {

    @Test
    fun workoutEntity_storesFieldsCorrectly() {
        val workout = WorkoutEntity(
            userId = "user123",
            type = "Running",
            durationMinutes = 30,
            caloriesBurned = 250,
            steps = 4000,
            dateMillis = 1000L
        )
        assertEquals("Running", workout.type)
        assertEquals(30, workout.durationMinutes)
        assertEquals(4000, workout.steps)
    }
}
