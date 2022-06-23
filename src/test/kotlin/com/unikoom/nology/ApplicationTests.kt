package com.unikoom.nology

import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration

@SpringBootTest
@TestMethodOrder(OrderAnnotation::class)
@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class ApplicationTests {

    @Test
    @Order(1)
    fun contextLoads() {
    }

    @Configuration
    internal class ContextConfiguration {
        @Bean
        fun clientConfiguration(): ClientConfiguration = mockk<ClientConfiguration>()
    }

}
