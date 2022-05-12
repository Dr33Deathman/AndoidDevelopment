package kz.kamran.todoapplication.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoryListResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("categories")
    val categoryList: List<CategoryDto>
)
