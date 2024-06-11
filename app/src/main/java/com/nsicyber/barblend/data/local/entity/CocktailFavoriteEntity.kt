package com.nsicyber.barblend.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nsicyber.barblend.data.model.IngredientModel

@Entity(tableName = "cocktail_favorite")
data class CocktailFavoriteEntity(
    @PrimaryKey val id: String,
    val category: String,
    val title: String,
    val glass: String,
    val image: String,
    val suggestion: String,
    val ingredients: List<IngredientModel>,
    val instructions: String,
    val tags: List<String>,
)
