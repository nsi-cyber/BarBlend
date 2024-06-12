package com.nsicyber.barblend.data

import com.nsicyber.barblend.data.local.entity.CocktailEntity
import com.nsicyber.barblend.data.local.entity.CocktailFavoriteEntity
import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.data.model.IngredientModel
import com.nsicyber.barblend.data.remote.model.CocktailResponse

fun CocktailResponse.toModel(): CocktailModel {
    return CocktailModel(
        id = idDrink.orEmpty(),
        category = strCategory.orEmpty(),
        title = strDrink.orEmpty(),
        glass = strGlass.orEmpty(),
        image = strDrinkThumb.orEmpty(),
        suggestion = "",
        ingredients = extractIngredients(this),
        instructions = strInstructions.orEmpty(),
        tags = strTags?.split(",").orEmpty(),
    )
}

fun CocktailResponse?.toLocal(): CocktailEntity {
    return CocktailEntity(
        id = this?.idDrink.orEmpty(),
        category = this?.strCategory.orEmpty(),
        title = this?.strDrink.orEmpty(),
        glass = this?.strGlass.orEmpty(),
        image = this?.strDrinkThumb.orEmpty(),
        suggestion = "",
        ingredients = extractIngredients(this),
        instructions = this?.strInstructions.orEmpty(),
        tags = this?.strTags?.split(",").orEmpty(),
    )
}

fun CocktailEntity?.toModel(): CocktailModel {
    return CocktailModel(
        id = this?.id.orEmpty(),
        category = this?.category.orEmpty(),
        title = this?.title.orEmpty(),
        glass = this?.glass.orEmpty(),
        image = this?.image.orEmpty(),
        suggestion = this?.suggestion.orEmpty(),
        ingredients = this?.ingredients.orEmpty(),
        instructions = this?.instructions.orEmpty(),
        tags = this?.tags.orEmpty(),
    )
}

fun CocktailFavoriteEntity.toModel(): CocktailModel {
    return CocktailModel(
        id = id,
        category = category,
        title = title,
        glass = glass,
        image = image,
        suggestion = suggestion,
        ingredients = ingredients,
        instructions = instructions,
        tags = tags,
    )
}

fun CocktailModel.toLocal(): CocktailEntity {
    return CocktailEntity(
        id = id,
        category = category.orEmpty(),
        title = title.orEmpty(),
        glass = glass.orEmpty(),
        image = image.orEmpty(),
        suggestion = suggestion.orEmpty(),
        ingredients = ingredients.orEmpty(),
        instructions = instructions.orEmpty(),
        tags = tags.orEmpty(),
    )
}

fun CocktailModel.toFavLocal(): CocktailFavoriteEntity {
    return CocktailFavoriteEntity(
        id = id,
        category = category.orEmpty(),
        title = title.orEmpty(),
        glass = glass.orEmpty(),
        image = image.orEmpty(),
        suggestion = suggestion.orEmpty(),
        ingredients = ingredients.orEmpty(),
        instructions = instructions.orEmpty(),
        tags = tags.orEmpty(),
    )
}

fun extractIngredients(data: CocktailResponse?): List<IngredientModel> {
    data?.let {
        val ingredients = mutableListOf<IngredientModel>()
        for (i in 1..15) {
            val ingredient =
                data::class.members.find { it.name == "strIngredient$i" }?.call(data) as? String
            val measure =
                data::class.members.find { it.name == "strMeasure$i" }?.call(data) as? String
            if (!ingredient.isNullOrEmpty()) {
                ingredients.add(
                    IngredientModel(
                        ingredient = ingredient,
                        measure = measure.orEmpty(),
                    ),
                )
            }
        }
        return ingredients
    } ?: run { return emptyList<IngredientModel>() }
}
