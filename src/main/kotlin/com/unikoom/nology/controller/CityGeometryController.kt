package com.unikoom.nology.controller

import com.unikoom.nology.domain.City
import com.unikoom.nology.domain.Geometry
import com.unikoom.nology.model.GeometryDto
import com.unikoom.nology.model.GeometryType
import com.unikoom.nology.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Контроллер для работы с геометрией города (geoJson).
 *
 * Структура геометрии:
 *  - FeatureCollection
 *     - Feature - это эквивалент одного города
 *        - Polygon или MultiPolygon - это сама геометрия города
 *     - Feature
 *        - Polygon или MultiPolygon
 *     - ...
 */
@RequestMapping("cities", produces = ["application/geo+json"], consumes = ["application/geo+json"])
@RestController
class CityGeometryController(
    private val repository: CityRepository,
) {

    /**
     * Возвращает геометрию (geoJson) по нескольким городам.
     * Тут по сути правильно реализовать поиск городов по различным критериям.
     */
    @GetMapping
    suspend fun list(): GeometryDto {
        return repository
            .findAll()
            .asFlow()
            .toList()
            .toGeoJson()
    }

    /**
     * Возвращает геометрию (geoJson) одного города.
     */
    @GetMapping("{id}")
    suspend fun get(@PathVariable id: String): GeometryDto {
        return repository.findById(id)
            .awaitFirstOrNull()
            ?.toGeoJson()
            ?: throw RuntimeException("Not found")
    }

    /**
     * Создает города из геометрии (geoJson).
     * В геометрии есть поле properties,
     * которое позволяет передать словарь свойств,
     * в которые можно занести информацию по городу.
     * Данный энпоит позволяет загрузить не только один
     * город, но и несколько сразу.
     * Один город = одна feature.
     */
    @PostMapping
    suspend fun post(@RequestBody geoJson: GeometryDto): GeometryDto {
        val features = geoJson.features
        if (features == null || features.isEmpty()) throw RuntimeException("Features is empty")
        val cities = features.mapIndexed { idx, feature ->
            val properties = feature.properties!!
            val geometry = feature.geometry!!
            City(
                id = properties["id"] ?: UUID.randomUUID().toString(),
                name = properties["name"] ?: throw RuntimeException("Feature $idx has not name property"),
                geometry = Geometry(
                    type = geometry.type,
                    coordinates = geometry.coordinates!!
                ),
            )
        }
        val storedCities = repository.saveAll(cities).asFlow().toList();
        return storedCities.toGeoJson()
    }

    /**
     * Обновляет геометрию города.
     * Так же позволяет обновить часть свойств города, через поле properties.
     */
    @PutMapping("{id}")
    suspend fun put(@PathVariable id: String, @RequestBody geoJson: GeometryDto): GeometryDto {
        val storedCity = repository.findById(id).awaitFirstOrNull()
            ?: throw RuntimeException("Not found")
        if (geoJson.features == null || geoJson.features.isEmpty()) return storedCity.toGeoJson()
        val feature = geoJson.features.first()
        val geometry = feature.geometry!!
        return repository
            .save(
                storedCity.copy(
                    geometry = Geometry(
                        type = geometry.type,
                        coordinates = geometry.coordinates!!
                    ),
                )
            )
            .awaitFirstOrNull()
            ?.toGeoJson()
            ?: throw RuntimeException("Cannot update city $id")
    }
}

private fun City.toGeoJson(): GeometryDto {
    return GeometryDto(
        type = GeometryType.FeatureCollection,
        features = this.geometry?.let {
            listOf(
                GeometryDto(
                    type = GeometryType.Feature,
                    properties = mapOf(
                        "id" to this.id,
                        "name" to this.name,
                    ),
                    geometry = GeometryDto(
                        type = this.geometry.type,
                        coordinates = this.geometry.coordinates
                    )
                )
            )
        } ?: listOf()
    )
}

private fun List<City>.toGeoJson(): GeometryDto {
    return GeometryDto(
        type = GeometryType.FeatureCollection,
        features = this.filter { it.geometry != null }.map {
            GeometryDto(
                type = GeometryType.Feature,
                properties = mapOf(
                    "id" to it.id,
                    "name" to it.name,
                ),
                geometry = GeometryDto(
                    type = it.geometry!!.type,
                    coordinates = it.geometry.coordinates
                )
            )
        }
    )
}
