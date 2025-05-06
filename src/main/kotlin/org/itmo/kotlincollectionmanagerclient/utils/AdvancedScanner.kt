package org.itmo.kotlincollectionmanagerclient.utils

import org.springframework.stereotype.Component
import java.util.Scanner

@Component
class AdvancedScanner {
    val scanner = Scanner(System.`in`)

    fun <T> cycleScan(message: String, converter: (String) -> T): T {
        var value: T? = null
        while (value == null) {
            try {
                print(message)

                val newValue = scanner.nextLine().trim()
                value = converter(newValue)
            } catch (_: NumberFormatException) {
                println("Invalid input. Please try again.")
            } catch (e: Exception) {
                println("An error occurred: ${e.message}")
            }
        }

        return value
    }

    fun <T> cycleUpdateScan(message: String, oldValue: T, converter: (String) -> T): T {
        var value: T? = null
        while (value == null) {
            try {
                print(message)

                val newValue = scanner.nextLine().trim()
                value = if (newValue == "") {
                    oldValue
                } else {
                    converter(newValue)
                }
            } catch (_: NumberFormatException) {
                println("Invalid input. Please try again.")
            } catch (e: Exception) {
                println("An error occurred: ${e.message}")
            }
        }

        return value
    }

    fun cycleScanBoolean(message: String): Boolean {
        var value: Boolean? = null
        while (value == null) {
            try {
                print(message)

                val input = scanner.nextLine().trim().lowercase()
                when (input) {
                    "true" -> value = true
                    "false" -> value = false
                    else -> println("Invalid input. Please enter 'true' or 'false'.")
                }
            } catch (e: Exception) {
                println("An error occurred: ${e.message}")
            }
        }
        return value
    }
}