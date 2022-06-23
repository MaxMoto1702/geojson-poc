package com.unikoom.nology.controller

import com.unikoom.nology.domain.City
import com.unikoom.nology.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Контроллер для работы с объектами "город".
 */
@RequestMapping("cities", produces = ["application/json"], consumes = ["application/json"])
@RestController
class CityController(
    private val repository: CityRepository,
) {

    /**
     * Возвращает список городов и только общую информацию.
     * Тут по сути правильно реализовать поиск городов по различным критериям.
     */
    @GetMapping
    suspend fun list(): Flow<City> {
        return repository.findAll().asFlow()
    }

    /**
     * Возвращает детализированную информацию по городу без geoJson.
     * Возможно стоит добавить в ответ geoJson.
     */
    @GetMapping("{id}")
    suspend fun get(@PathVariable id: String): City {
        return repository.findById(id)
            .awaitFirstOrNull()
            ?: throw RuntimeException("Not found")
    }

    /**
     * Эндпоинт для создания города без geoJson.
     * Возможно стоит добавить в ответ geoJson.
     */
    @PostMapping
    suspend fun post(@RequestBody city: City): City {
        return repository.save(city)
            .awaitFirstOrNull()
            ?: throw RuntimeException("Cannot save city $city")
    }

    /**
     * Обновление города без geoJson.
     * Позволяет обновить не весь город, а только часть информации города.
     * Возможно стоит добавить в ответ geoJson.
     */
    @PutMapping("{id}")
    suspend fun put(@PathVariable id: String, @RequestBody cityUpdate: City): City {
        val storedCity = repository.findById(id).awaitFirstOrNull()
            ?: throw RuntimeException("Not found")
        return repository
            .save(
                storedCity.copy(
                    name = cityUpdate.name,
                )
            )
            .awaitFirstOrNull()
            ?: throw RuntimeException("Cannot update city $id")
    }
}
