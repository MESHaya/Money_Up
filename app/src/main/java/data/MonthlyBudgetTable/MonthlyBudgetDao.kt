package data.MonthlyBudgetTable

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Update
import data.CategoryTable.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthlyBudgetDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(monthlyBudget: MonthlyBudget)

    @Update
    suspend fun update(monthlyBudget: MonthlyBudget)

    @Delete
    suspend fun delete(monthlyBudget: MonthlyBudget)

    @Query("SELECT * from monthlyBudget WHERE monthlyBudget_id = :monthlyBudget_id")
    fun getBudget(monthlyBudget_id:Int): Flow<MonthlyBudget>

    @Query("SELECT * from monthlyBudget ORDER BY monthlyBudget_name ASC")
    fun getAllBudgets(): Flow<List<MonthlyBudget>>
}