package com.school.projettm.dtos

import java.io.File
import java.math.BigDecimal

data class CreateArticleCommand(
    val title: String,
    val description: String,
    val price: BigDecimal,
    val category: String,
    val state: String,
    val userId: Int,
    val createdAt: String,
    val updatedAt: String,
    val status: String,
    val quantity: Int,
    val image: File)
