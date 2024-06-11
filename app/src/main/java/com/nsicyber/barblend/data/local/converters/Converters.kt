package com.nsicyber.barblend.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nsicyber.barblend.data.model.IngredientModel

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromIngredientList(ingredients: List<IngredientModel>): String {
        val gson = Gson()
        return gson.toJson(ingredients)
    }

    @TypeConverter
    fun toIngredientList(ingredientsString: String): List<IngredientModel> {
        val listType = object : TypeToken<List<IngredientModel>>() {}.type
        return Gson().fromJson(ingredientsString, listType)
    }
}
