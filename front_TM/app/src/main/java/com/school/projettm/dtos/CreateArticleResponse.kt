package com.school.projettm.dtos

import java.io.File
import java.sql.Date

data class CreateArticleResponse(val title: String, val description: String, val price: Double, val category: String, val state: String
,val userId: Int, val createdAt: Date, val updatedAt: Date, val status: String, val quantity: Int, val image: String)
