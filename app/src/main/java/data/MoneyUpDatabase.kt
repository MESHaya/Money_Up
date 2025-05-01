package data

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import data.CategoryTable.Category
import data.CategoryTable.CategoryDao
import data.ExpenseTable.Expense
import data.ExpenseTable.ExpenseDao
import data.MonthlyBudgetTable.MonthlyBudget
import data.MonthlyBudgetTable.MonthlyBudgetDao
import data.UserTable.User
import data.UserTable.UserDao

@Database(entities = [User::class, Category :: class, MonthlyBudget :: class, Expense :: class] , version = 8, exportSchema = false)

abstract class MoneyUpDatabase: RoomDatabase(){

    //declare dao for tables
    abstract fun userDao(): UserDao
    abstract fun categoryDao() : CategoryDao
    abstract fun  monthlyBudgetDao() : MonthlyBudgetDao
    abstract fun expenseDao(): ExpenseDao

    companion object{  //allows access to the methods to create or get the database
        @Volatile
        private var Instance: MoneyUpDatabase? = null


        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Step 1: Create a temporary table without the column you want to remove
                database.execSQL("""
            CREATE TABLE category_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                category_name STRING NOT NULL,
                category_description STRING NOT NULL DEFAULT '',
                category_color STRING NOT NULL DEFAULT '',
                category_icon STRING NOT NULL DEFAULT ''
            )
        """)

                // Step 2: Copy the data from the old table (excluding the column to be removed)
                database.execSQL("""
            INSERT INTO category_new (id, category_name, category_description, category_color, category_icon)
            SELECT id, category_name, category_description, category_color, category_icon
            FROM category
        """)

                // Step 3: Drop the old table
                database.execSQL("DROP TABLE category")

                // Step 4: Rename the new table to the old table's name
                database.execSQL("ALTER TABLE category_new RENAME TO category")
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Step 1: Create new table with updated schema
                database.execSQL("""
            CREATE TABLE expenses_new (
                expense_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                user_id INTEGER NOT NULL,
                category_id INTEGER NOT NULL,
                expenseName TEXT NOT NULL,
                amount REAL NOT NULL,
                date TEXT NOT NULL,
                startTime TEXT NOT NULL,
                endTime TEXT NOT NULL,
                description TEXT NOT NULL,
                photo TEXT NOT NULL,
                FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                FOREIGN KEY(category_id) REFERENCES category(category_id) ON DELETE CASCADE
            )
        """)

                // Step 2: Copy data from old to new table — you may choose how to handle old "time" data
                database.execSQL("""
            INSERT INTO expenses_new (expense_id, user_id, category_id, expenseName, amount, date, startTime, endTime, description, photo)
            SELECT expense_id, user_id, category_id, expenseName, amount, date,
                   time AS startTime, time AS endTime,  -- assuming same value for both initially
                   description, photo
            FROM expenses
        """)

                // Step 3: Drop old table
                database.execSQL("DROP TABLE expenses")

                // Step 4: Rename new table
                database.execSQL("ALTER TABLE expenses_new RENAME TO expenses")
            }
        }

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Step 1: Create the new table with the updated column name "imageUri"
                database.execSQL("""
            CREATE TABLE expenses_new (
                expense_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                user_id INTEGER NOT NULL,
                category_id INTEGER NOT NULL,
                expenseName TEXT NOT NULL,
                amount REAL NOT NULL,
                date TEXT NOT NULL,
                startTime TEXT NOT NULL,
                endTime TEXT NOT NULL,
                description TEXT NOT NULL,
                imageUri TEXT NOT NULL,
                FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                FOREIGN KEY(category_id) REFERENCES category(category_id) ON DELETE CASCADE
            )
        """)

                // Step 2: Copy data, mapping old "photo" to new "imageUri"
                database.execSQL("""
            INSERT INTO expenses_new (expense_id, user_id, category_id, expenseName, amount, date, startTime, endTime, description, imageUri)
            SELECT expense_id, user_id, category_id, expenseName, amount, date, startTime, endTime, description, photo
            FROM expenses
        """)

                // Step 3: Drop the old table
                database.execSQL("DROP TABLE expenses")

                // Step 4: Rename the new table to the original name
                database.execSQL("ALTER TABLE expenses_new RENAME TO expenses")
            }
        }



        fun getDatabase(context: Context) : MoneyUpDatabase{

            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MoneyUpDatabase::class.java, "moneyup_database")
                    .addMigrations(MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8)
                    .build()
                    .also { Instance = it }
            }
        }
    }

}