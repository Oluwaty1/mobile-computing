package com.example.myapplication

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel(context: Context) : ViewModel() {

    private val db = AppDatabase.getDatabase(context)
    private val dao = db.vocabularyDao()

    val vocabularyList = mutableStateListOf<VocabularyItem>()

    init {
        dao.clearAllAudioPaths()
        loadData()
    }

    private fun loadData() {
        var items = dao.getAllVocabulary()

        if (items.isEmpty()) {
            dao.insertAll(FinnishData.defaultPhrases)
            items = dao.getAllVocabulary()
        }

        vocabularyList.clear()
        vocabularyList.addAll(items)
    }

    fun updateItem(item: VocabularyItem) {
        dao.updateVocabulary(item)
        loadData()
    }
    fun addNewPhrase(newPhrase: String) {
        if (newPhrase.isNotBlank()) {
            dao.insertVocabulary(VocabularyItem(phrase = newPhrase.trim()))
            loadData()
        }
    }
}