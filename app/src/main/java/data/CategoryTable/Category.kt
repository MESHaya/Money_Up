package data.CategoryTable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import data.UserTable.User

@Entity(
    tableName = "category",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("user_id"),  // column name in the User entity
            childColumns = arrayOf("user_id"),   // matching column in this table
            onDelete = ForeignKey.CASCADE        // optional: delete categories if user is deleted
        )
    ]
)
data class Category(
    @PrimaryKey(autoGenerate = true)
    val category_id: Int = 0,

    @ColumnInfo(name = "user_id")
    val user_id: Int,

    val category_name: String,
    val category_description: String,
    val category_color: String,
    val category_icon: String
    //val category_limit: Int
)
