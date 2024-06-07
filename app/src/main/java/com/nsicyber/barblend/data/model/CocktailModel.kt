package com.nsicyber.barblend.data.model


data class CocktailModel(
val id: String,
val category: String?=null,
val title: String?=null,
val glass: String?=null,
val image: String?=null,
val suggestion: String?=null,
val ingredients: List<IngredientModel?>?=null,
val instructions: String?=null,
val tags: List<String>?=null,
val timestamp: Long = System.currentTimeMillis()
)
