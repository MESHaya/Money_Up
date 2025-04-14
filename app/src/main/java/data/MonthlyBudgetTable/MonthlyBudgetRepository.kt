package data.MonthlyBudgetTable


import kotlinx.coroutines.flow.Flow

interface MonthlyBudgetRepository {


    fun getAllBudgetsStream() : Flow<List<MonthlyBudget>>

    fun getBudgetsStream(monthlyBudget_id:Int) : Flow<MonthlyBudget?>

    suspend fun insertBudget(monthlyBudget: MonthlyBudget)

    suspend fun deleteBudget(monthlyBudget: MonthlyBudget)

    suspend fun updateBudget(monthlyBudget: MonthlyBudget)

}