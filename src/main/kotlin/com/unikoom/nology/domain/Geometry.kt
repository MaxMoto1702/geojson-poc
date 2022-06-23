package com.unikoom.nology.domain

import com.unikoom.nology.model.GeometryType

data class Geometry(
    val type: GeometryType,
    val coordinates: List<List<List<List<Double>>>>
)
