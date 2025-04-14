package data.ExpenseTable

import kotlinx.coroutines.flow.Flow


class OfflineExpenseRepository(private val expenseDao: ExpenseDao) : ExpenseRepository {

    override fun getAllExpenses(): Flow<List<Expense>>  = expenseDao.getAllExpenses()

    override fun getExpensesStream(expense_id: Int): Flow<Expense?> =  expenseDao.getExpense(expense_id)

    override suspend fun insertExpense(expense: Expense) {
        expenseDao.insert(expense)
    }

    override suspend fun deleteExpense(expense: Expense) {
        expenseDao.delete(expense)
    }

    override suspend fun updateExpense(expense: Expense) {
        expenseDao.update(expense)
    }

}