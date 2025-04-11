package data.CategoryTable

import data.UserTable.User
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getAllCategoriesStream() : Flow<List<Category>>

    fun getCategoriesStream(category_id:Int) : Flow<Category?>

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun updateCategory(category: Category)
}