package data.ExpenseTable


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Update
import data.CategoryNameTotal
import data.CategoryTotal
import kotlinx.coroutines.flow.Flow



@Dao
interface ExpenseDao {

    @Query("""
    SELECT category_id, SUM(amount) AS totalAmount
    FROM expenses
    WHERE date BETWEEN :startDate AND :endDate
    GROUP BY category_id
""")
    fun getCategoryTotalsBetweenDates(startDate: String, endDate: String): Flow<List<CategoryTotal>>



    @Query("""
    SELECT c.category_name, SUM(e.amount) AS totalAmount
    FROM expenses e
    JOIN category c ON e.category_id = c.category_id
    WHERE e.date BETWEEN :startDate AND :endDate
    GROUP BY e.category_id
""")
    fun getCategoryNameTotalsBetweenDates(startDate: String, endDate: String): Flow<List<CategoryNameTotal>>

    @Query("""
    SELECT c.category_name, SUM(e.amount) AS totalAmount
    FROM expenses e
    JOIN category c ON e.category_id = c.category_id
    GROUP BY e.category_id
""")
    fun getCategoryNameTotals(): Flow<List<CategoryNameTotal>>


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

    @Query("SELECT * FROM expenses WHERE category_id = :categoryId")
    fun getExpensesByCategory(categoryId: Int): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE user_id = :userId")
    fun getExpensesByUser(userId: Int): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date = :date")
    fun getExpensesByDate(date: String): Flow<List<Expense>>

}