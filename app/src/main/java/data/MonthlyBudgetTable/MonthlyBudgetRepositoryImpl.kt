
package data.MonthlyBudgetTable

import kotlinx.coroutines.flow.Flow

class MonthlyBudgetRepositoryImpl(private val monthlyBudgetDao: MonthlyBudgetDao) : MonthlyBudgetRepository {

    override fun getAllBudgetsStream(): Flow<List<MonthlyBudget>> {
        return monthlyBudgetDao.getAllBudgets()
    }

    override fun getBudgetsStream(monthlyBudget_id: Int): Flow<MonthlyBudget?> {
        return monthlyBudgetDao.getBudget(monthlyBudget_id)
    }

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
