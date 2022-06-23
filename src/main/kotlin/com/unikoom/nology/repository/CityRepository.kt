package com.unikoom.nology.repository

import com.unikoom.nology.domain.City
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository

interface CityRepository : ReactiveElasticsearchRepository<City, String> {
}
