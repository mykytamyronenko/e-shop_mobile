package com.school.projettm.models

import java.util.Date

data class Article(val articleId: Int,
                                 val title: String,
                                 val description: String,
                                 val price: Double,
                                 val category: String,
                                 val state: String,
                                 val userId: Int,
                                 val createdAt: String,
                                 val updatedAt: String,
                                 val status: String,
                                 val mainImageUrl:String,
                                 val quantity: Int)
