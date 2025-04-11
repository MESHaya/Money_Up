package data.CategoryTable

import data.UserTable.User
import kotlinx.coroutines.flow.Flow

class OfflineCategoryRepository (private val categoryDao: CategoryDao) : CategoryRepository{


    override fun getAllCategoriesStream(): Flow<List<Category>>  = categoryDao.getAllCategories()

    override fun getCategoriesStream(category_id: Int): Flow<Category?> = categoryDao.getCategory(category_id)

    override suspend fun insertCategory(category: Category) {
        categoryDao.insert( category)
    }

    override suspend fun deleteCategory(category: Category) {
       categoryDao.delete(category)
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.update(category)
    }



}