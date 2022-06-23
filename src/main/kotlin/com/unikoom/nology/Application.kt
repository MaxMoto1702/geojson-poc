package com.unikoom.nology

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.elasticsearch.config.EnableReactiveElasticsearchAuditing
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories

@SpringBootApplication
@EnableReactiveElasticsearchRepositories
//@EnableReactiveElasticsearchAuditing
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
