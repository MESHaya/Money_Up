package data

import androidx.room.Entity
import androidx.room.PrimaryKey

//primary constructor
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val user_id : Long=0,
    var username : String,
    var password :String,
    var email : String,
    var name :String,
    var surname :String,
)
