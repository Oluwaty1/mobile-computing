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

@Entity(tableName = "vocabulary_table")
data class VocabularyItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val phrase: String,
    val audioFilePath: String? = null
)

@Dao
interface VocabularyDao {
    @Query("SELECT * FROM vocabulary_table")
    fun getAllVocabulary(): List<VocabularyItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(items: List<VocabularyItem>)

    @Insert
    fun insertVocabulary(item: VocabularyItem)
    @Update
    fun updateVocabulary(item: VocabularyItem)
    @Query("UPDATE vocabulary_table SET audioFilePath = null")
    fun clearAllAudioPaths()
}

@Database(entities = [VocabularyItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vocabularyDao(): VocabularyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "finnish_vocab_database"
                )
                    .allowMainThreadQueries()
                    .build().also { INSTANCE = it }
            }
        }
    }
}