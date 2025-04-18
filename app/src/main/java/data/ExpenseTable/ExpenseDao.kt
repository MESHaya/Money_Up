package data.ExpenseTable


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expense: Expense)

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(category: Expense)

    @Query("SELECT * from expenses WHERE expense_id = :expense_id")
    fun getExpense(expense_id: Int): Flow<Expense>

    @Query("SELECT * from expenses ORDER BY  expenseName ")
    fun getAllExpenses(): Flow<List<Expense>>
}