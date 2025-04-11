package data

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room
import data.UserTable.User
import data.UserTable.UserDao

@Database(entities = [User::class] , version = 1, exportSchema = false)

abstract class MoneyUpDatabase: RoomDatabase(){

    abstract fun userDao(): UserDao

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