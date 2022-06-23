package com.unikoom.nology.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL

@JsonInclude(NON_NULL)
data class GeometryDto(
    val type: GeometryType,
    val geometry: GeometryDto? = null,
    val properties: Map<String, String>? = null,
    val coordinates: List<List<List<List<Double>>>>? = null,
    val features: List<GeometryDto>? = null,
)
