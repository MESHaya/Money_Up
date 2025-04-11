package data.CategoryTable

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true) val category_id:Int=0,
    val user_id:Int,
    val category_name:String,
    val category_limit:Int
)
