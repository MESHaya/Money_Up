package data.UserTable

import kotlinx.coroutines.flow.Flow

/**
 * This is a repo that provides insert,update,delete etc from the DB
 */

interface UsersRepository {
    fun getAllUsersStream() : Flow<List<User>>

    fun getUserStream(user_id:Int) :Flow<User?>

    suspend fun insertUser(user: User)

    suspend fun deleteUser(user: User)

    suspend fun updateUser(user: User)
}