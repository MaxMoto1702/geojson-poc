package com.unikoom.nology.domain

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "cities-poc")
data class City(
    @Id val id: String,
    val name: String,
    val geometry: Geometry?,
)
