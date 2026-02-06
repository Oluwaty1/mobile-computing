package com.example.myapplication

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

// 1. The Entity (The Table)
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 0,
    val name: String,
    val imagePath: String?
)

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 0")
    fun getUser(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserProfile)

    @Update
    fun updateUser(user: UserProfile)
}

// 3. The Database Class
@Database(entities = [UserProfile::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .allowMainThreadQueries()
                    .build().also { INSTANCE = it }
            }
        }
    }
}