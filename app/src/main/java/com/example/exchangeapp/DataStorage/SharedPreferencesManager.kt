package com.example.exchangeapp.DataStorage

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesManager(context: SharedPreferences) {

    private val sharedPreferences: SharedPreferences = context
    private val gson = Gson()

    fun saveSortedButtonInfo(buttonInfo: List<Pair<String, Int>>) {
        val jsonString = gson.toJson(buttonInfo)
        sharedPreferences.edit().putString("sortedButtonInfo", jsonString).apply()
    }

    fun loadSortedButtonInfo(): List<Pair<String, Int>> {
        val jsonString = sharedPreferences.getString("sortedButtonInfo", null)
        return if (jsonString != null) {
            try {
                val type = object : TypeToken<List<Pair<String, Number>>>() {}.type
                val data = gson.fromJson<List<Pair<String, Number>>>(jsonString, type) ?: emptyList()
                // Convert each value to Int explicitly
                data.map { it.first to it.second.toInt() }
            } catch (e: Exception) {
                emptyList() // Fallback to empty list if deserialization fails
            }
        } else {
            emptyList() // Default empty list if nothing is stored
        }
    }

    fun hasStoredData(): Boolean {
        return sharedPreferences.contains("sortedButtonInfo")
    }


}