package com.nsicyber.barblend.data

import com.nsicyber.barblend.data.local.entity.CocktailFavoriteLocal
import com.nsicyber.barblend.data.local.entity.CocktailLocal
import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.data.model.IngredientModel
import com.nsicyber.barblend.data.remote.model.CocktailRemote

fun CocktailRemote.toModel(): CocktailModel {
    return CocktailModel(
        id = idDrink!!,
        category = strCategory,
        title = strDrink,
        glass = strGlass,
        image = strDrinkThumb,
        suggestion = null,
        ingredients = extractIngredients(this),
        instructions = strInstructions,
        tags = strTags?.split(",")
    )
}

fun CocktailRemote.toLocal(): CocktailLocal {
    return CocktailLocal(
        id = idDrink.orEmpty(),
        category = strCategory.orEmpty(),
        title = strDrink.orEmpty(),
        glass = strGlass.orEmpty(),
        image = strDrinkThumb.orEmpty(),
        suggestion = "",
        ingredients = extractIngredients(this),
        instructions = strInstructions.orEmpty(),
        tags = strTags?.split(",").orEmpty()
    )
}

fun CocktailLocal.toModel(): CocktailModel {
    return CocktailModel(
        id = id,
        category = category,
        title = title,
        glass = glass,
        image = image,
        suggestion = suggestion,
        ingredients = ingredients,
        instructions = instructions,
        tags = tags
    )
}

fun CocktailFavoriteLocal.toModel(): CocktailModel {
    return CocktailModel(
        id = id,
        category = category,
        title = title,
        glass = glass,
        image = image,
        suggestion = suggestion,
        ingredients = ingredients,
        instructions = instructions,
        tags = tags
    )
}

fun CocktailModel.toFavLocal(): CocktailFavoriteLocal {
    return CocktailFavoriteLocal(
        id = id,
        category = category.orEmpty(),
        title = title.orEmpty(),
        glass = glass.orEmpty(),
        image = image.orEmpty(),
        suggestion = suggestion.orEmpty(),
        ingredients = ingredients.orEmpty(),
        instructions = instructions.orEmpty(),
        tags = tags.orEmpty()
    )
}

fun extractIngredients(data: CocktailRemote): List<IngredientModel> {
    val temp = arrayListOf<IngredientModel>()
    data.strIngredient1?.let {
        temp.add(IngredientModel(ingredient = it, measure = data.strMeasure1.orEmpty()))
    }
    data.strIngredient2?.let {
        temp.add(IngredientModel(ingredient = it, measure = data.strMeasure2.orEmpty()))
    }
    data.strIngredient3?.let {
        temp.add(IngredientModel(ingredient = it, measure = data.strMeasure3.orEmpty()))
    }
    data.strIngredient4?.let {
        temp.add(IngredientModel(ingredient = it, measure = data.strMeasure4.orEmpty()))
    }
    data.strIngredient5?.let {
        temp.add(IngredientModel(ingredient = it, measure = data.strMeasure5.orEmpty()))
    }
    data.strIngredient6?.let {
        temp.add(IngredientModel(ingredient = it, measure = data.strMeasure6.orEmpty()))
    }
    data.strIngredient7?.let {
        temp.add(IngredientModel(ingredient = it, measure = data.strMeasure7.orEmpty()))
    }
    data.strIngredient8?.let {
        temp.add(IngredientModel(ingredient = it, measure = data.strMeasure8.orEmpty()))
    }
    data.strIngredient9?.let {
        temp.add(IngredientModel(ingredient = it, measure = data.strMeasure9.orEmpty()))
    }
    data.strIngredient10?.let {
        temp.add(IngredientModel(ingredient = it, measure = data.strMeasure10.orEmpty()))
    }
    data.strIngredient11?.let {
        temp.add(IngredientModel(ingredient = it, measure = data.strMeasure11.orEmpty()))
    }
    data.strIngredient12?.let {
        temp.add(IngredientModel(ingredient = it, measure = data.strMeasure12.orEmpty()))
    }
    data.strIngredient13?.let {
        temp.add(IngredientModel(ingredient = it, measure = data.strMeasure13.orEmpty()))
    }
    data.strIngredient14?.let {
        temp.add(IngredientModel(ingredient = it, measure = data.strMeasure14.orEmpty()))
    }
    data.strIngredient15?.let {
        temp.add(IngredientModel(ingredient = it, measure = data.strMeasure15.orEmpty()))
    }
    return temp.toList()
}
