package com.alef.weatherapp

import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.fasterxml.jackson.databind.ObjectMapper


@RestController
class ApiController {
    @GetMapping("/forecast")
    fun forecast(@RequestParam q: String, @RequestParam appid: String): Any {
        val allowedCities = listOf("Abu Dhabi", "Dubai", "Sharjah", "Al Ain", "Ajman", "Ras Al Khaimah", "Fujairah", "Umm Al Quwain")

        if (allowedCities.none { it.equals(q, ignoreCase = true) }) {
            val errorResponse = mapOf("cod" to "404", "message" to "City not found")
            return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
        }

        val resource = ClassPathResource("weather.json")
        val jsonContent = resource.inputStream.bufferedReader().use { it.readText() }
        val objectMapper = ObjectMapper()
        val jsonMap: MutableMap<String, Any> = (objectMapper.readValue(jsonContent, Map::class.java) as Map<String, Any>).toMutableMap()
        val city: MutableMap<String, Any> = jsonMap["city"] as MutableMap<String, Any>
        city["name"] = q
        return jsonMap
    }
}
