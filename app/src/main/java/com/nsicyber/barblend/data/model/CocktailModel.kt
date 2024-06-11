package com.nsicyber.barblend.data.model

data class CocktailModel(
    val id: String,
    val category: String,
    val title: String,
    val glass: String,
    val image: String,
    val suggestion: String,
    val ingredients: List<IngredientModel>,
    val instructions: String,
    val tags: List<String>,
    val timestamp: Long = System.currentTimeMillis(),
)
