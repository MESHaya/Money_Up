package data.UserTable

import kotlinx.coroutines.flow.Flow

/**
 *  implementation of UsersRepository that interacts with a local Room database.
 */
class OfflineUsersRepository(private val userDao: UserDao) : UsersRepository {

    // Returns a Flow that emits all users from the database.
    override fun getAllUsersStream(): Flow<List<User>> = userDao.getAllUsers()

    // Returns a Flow that emits a single user by ID (or null if not found).
    override fun getUserStream(user_id: Int): Flow<User?> = userDao.getUser(user_id)

    // Inserts a user into the database (suspend = must run in coroutine).
    override suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    // Deletes a user from the database.
    override suspend fun deleteUser(user: User) {
        userDao.delete(user)
    }

    // Updates a user in the database.
    override suspend fun updateUser(user: User) {
        userDao.update(user)
    }
}
