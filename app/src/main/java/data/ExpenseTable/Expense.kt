package data.ExpenseTable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import data.UserTable.User
import data.CategoryTable.Category


@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("category_id"),
            childColumns = arrayOf("category_id"),
            onDelete = ForeignKey.CASCADE  // optional but useful for keeping data consistent
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("user_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val expense_id: Int = 0,

    @ColumnInfo(name = "user_id")
    val user_id: Int,  // links to the User table

    @ColumnInfo(name = "category_id")
    val category_id: Int,  // links to the Category table

    val expenseName : String,

    val amount: Int,  // the amount spent

    val date: String,  // changed to String for readability (e.g. "2025-04-13")

    val time: String,  // changed to String (e.g. "14:30"), you could also use Date/Time types if you want

    val description: String,  // what was this expense?

    val photo: String  // file path or URI to image of receipt, etc.
)
