package data.MonthlyBudgetTable

import data.CategoryTable.Category
import kotlinx.coroutines.flow.Flow

class OfflineMonthlyBudgetRepository(
    private val monthlyBudgetDao: MonthlyBudgetDao
) : MonthlyBudgetRepository {

    override fun getAllBudgetsStream(): Flow<List<MonthlyBudget>> =
        monthlyBudgetDao.getAllBudgets()


    override fun getBudgetsStream(monthlyBudget_id:Int) : Flow<MonthlyBudget?> = monthlyBudgetDao.getBudget(monthlyBudget_id)

    override suspend fun insertBudget(monthlyBudget: MonthlyBudget) {
        monthlyBudgetDao.insert(monthlyBudget)
    }

    override suspend fun deleteBudget(monthlyBudget: MonthlyBudget) {
        monthlyBudgetDao.delete(monthlyBudget)
    }

    override suspend fun updateBudget(monthlyBudget: MonthlyBudget) {
        monthlyBudgetDao.update(monthlyBudget)
    }
}
