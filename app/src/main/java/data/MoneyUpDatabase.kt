package data

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room
import data.CategoryTable.Category
import data.CategoryTable.CategoryDao
import data.ExpenseTable.Expense
import data.ExpenseTable.ExpenseDao
import data.MonthlyBudgetTable.MonthlyBudget
import data.MonthlyBudgetTable.MonthlyBudgetDao
import data.UserTable.User
import data.UserTable.UserDao

@Database(entities = [User::class, Category :: class, MonthlyBudget :: class, Expense :: class] , version = 5, exportSchema = false)

abstract class MoneyUpDatabase: RoomDatabase(){

    //declare dao for tables
    abstract fun userDao(): UserDao
    abstract fun categoryDao() : CategoryDao
    abstract fun  monthlyBudgetDao() : MonthlyBudgetDao
    abstract fun expenseDao(): ExpenseDao

    companion object{  //allows access to the methods to create or get the database
        @Volatile
        private var Instance: MoneyUpDatabase? = null

        fun getDatabase(context: Context) : MoneyUpDatabase{

            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MoneyUpDatabase::class.java, "moneyup_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }

}