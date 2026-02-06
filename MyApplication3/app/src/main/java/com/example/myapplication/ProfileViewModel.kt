package com.example.myapplication

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.io.File

class ProfileViewModel(private val context: Context) : ViewModel() {

    private val db = AppDatabase.getDatabase(context)
    private val dao = db.userDao()

    var userName by mutableStateOf("Michael")
    var profileImagePath by mutableStateOf<String?>(null)

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val user = dao.getUser()
        if (user != null) {
            userName = user.name
            profileImagePath = user.imagePath
        } else {
            val newUser = UserProfile(id = 0, name = "Michael", imagePath = null)
            dao.insertUser(newUser)
        }
    }

    fun updateName(newName: String) {
        userName = newName
        saveToDb()
    }

    fun updateImage(uri: Uri) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, "profile_pic.jpg")
            val outputStream = file.outputStream()

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            profileImagePath = file.absolutePath
            saveToDb()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveToDb() {
        val user = UserProfile(id = 0, name = userName, imagePath = profileImagePath)
        dao.insertUser(user)
    }
}