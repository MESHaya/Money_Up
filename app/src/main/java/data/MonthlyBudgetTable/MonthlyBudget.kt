package data.MonthlyBudgetTable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import data.UserTable.User

@Entity(
    tableName = "monthlyBudget",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("user_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MonthlyBudget(
    @PrimaryKey(autoGenerate = true)

    val monthlyBudget_id: Int = 0,

    @ColumnInfo(name = "user_id")
    val user_id: Int,

    val monthlyBudget_name: String,

    val month: String,

    val total_budget: Int,

    val min_amont: Int,

    val max_amount:Int,

)
