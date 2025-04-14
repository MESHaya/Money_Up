package data.ExpenseTable

import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    fun getAllExpenses(): Flow<List<Expense>>

    fun getExpensesStream(expense_id :Int) : Flow<Expense?>

    suspend fun insertExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    suspend fun updateExpense(expense: Expense)
}