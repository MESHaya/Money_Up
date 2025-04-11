package data.CategoryTable

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: Category)

    @Update
   suspend fun update(category: Category)

   @Delete
   suspend fun delete(category: Category)

   @Query("SELECT * from category WHERE category_id = :category_id")
   fun getCategory(category_id:Int): Flow<Category>

   @Query("SELECT * from category ORDER BY category_name ASC")
   fun getAllCategories(): Flow<List<Category>>
}