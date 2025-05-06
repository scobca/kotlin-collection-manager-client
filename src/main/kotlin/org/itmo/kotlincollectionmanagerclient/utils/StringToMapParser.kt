package org.itmo.kotlincollectionmanagerclient.utils

object StringToMapParser {
    fun parseToMap(input: String): Map<String, String> {
        val trimmed = input.trim().removePrefix("{").removeSuffix("}")

        val pairs = trimmed.split(", ")

        val map = mutableMapOf<String, String>()
        for (pair in pairs) {
            val keyValue = pair.split("=", limit = 2)
            if (keyValue.size == 2) {
                val key = keyValue[0].trim()
                val value = keyValue[1].trim()
                map[key] = value
            }
        }
        return map
    }
}