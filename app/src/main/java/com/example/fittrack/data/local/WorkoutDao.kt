package com.example.fittrack.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: WorkoutEntity): Long

    @Update
    suspend fun update(workout: WorkoutEntity)

    @Delete
    suspend fun delete(workout: WorkoutEntity)

    @Query("SELECT * FROM workouts WHERE userId = :userId ORDER BY dateMillis DESC")
    fun getWorkoutsForUser(userId: String): Flow<List<WorkoutEntity>>

    @Query("SELECT SUM(steps) FROM workouts WHERE userId = :userId")
    fun getTotalSteps(userId: String): Flow<Int?>

    @Query("SELECT SUM(caloriesBurned) FROM workouts WHERE userId = :userId")
    fun getTotalCalories(userId: String): Flow<Int?>
}
