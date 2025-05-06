package org.itmo.kotlincollectionmanagerclient.validators

import org.itmo.kotlincollectionmanagerclient.collection.Coordinates
import org.itmo.kotlincollectionmanagerclient.collection.Flat
import org.itmo.kotlincollectionmanagerclient.collection.Furnish
import org.itmo.kotlincollectionmanagerclient.collection.House
import org.itmo.kotlincollectionmanagerclient.utils.AdvancedScanner
import org.itmo.kotlincollectionmanagerclient.validators.interfaces.BasicCommandValidator
import org.springframework.stereotype.Component
import kotlin.text.toBoolean
import kotlin.text.toFloat
import kotlin.text.toInt
import kotlin.text.toLong

@Component
class InsertValidator(private val scanner: AdvancedScanner) : BasicCommandValidator {
    override fun validate(args: List<String>): Any {
        if (args.isEmpty()) return "This command requires at least one argument (flatID)."

        var id = 0L
        try {
            id = args[0].toLong()
        } catch (_: NumberFormatException) {
            return "Invalid number format (flatID)."
        }

        if (args.size == 1) return lifeTimeExecution(id)

        val flatStringifier = args[1].toString()

        if (args.size == 2) return automaticallyExecution(id, flatStringifier)

        return "Invalid count of arguments"
    }

    fun lifeTimeExecution(id: Long): String {
        val newFlat = Flat()
        val newCoordinates = Coordinates()
        val newHouse = House()

        newFlat.setId(id)

        while (newFlat.getName() == null) {
            newFlat.setName(scanner.cycleScan("Enter element name: ") { it }.trim())
        }

        newCoordinates.setX(scanner.cycleScan("Enter coordinate x: ") { it.toLong() })
        newCoordinates.setY(scanner.cycleScan("Enter coordinates y: ") { it.toFloat() })
        newFlat.setCoordinates(newCoordinates)

        while (newFlat.getArea() == null) {
            newFlat.setArea(scanner.cycleScan("Enter flat area: ") { it.toLong() })
        }

        while (newFlat.getNumberOfRooms() == null) {
            newFlat.setNumberOfRooms(scanner.cycleScan("Enter number of rooms: ") { it.toLong() })
        }

        while (newFlat.getPrice() == null) {
            newFlat.setPrice(scanner.cycleScan("Enter price: ") { it.toLong() })
        }

        newFlat.setBalcony(scanner.cycleScanBoolean("Enter balcony available (true/false): "))

        newFlat.setFurnish(scanner.cycleScan("Enter furnish type (DESIGNER, FINE, LITTLE): ") {
            try {
                Furnish.valueOf(it.uppercase().trim())
            } catch (_: IllegalArgumentException) {
                throw IllegalArgumentException("Invalid furnish type. Please try again.")
            }
        })

        newHouse.setName(scanner.cycleScan("Enter new house name: ") { it })
        newHouse.setYear(scanner.cycleScan("Enter the year of building a house: ") { it.toInt() })
        newHouse.setNumberOfFloors(scanner.cycleScan("Enter the number of floors: ") { it.toLong() })
        newFlat.setHouse(newHouse)

        return "[${newFlat.getId()},${newHouse.getName()},${
            newFlat.getCoordinates()?.getX()
        },${
            newFlat.getCoordinates()?.getY()
        },${newFlat.getArea()},${newFlat.getNumberOfRooms()},${newFlat.getPrice()},${newFlat.getBalcony()},${newFlat.getFurnish()},${
            newFlat.getHouse()?.getName()
        },${newFlat.getHouse()?.getYear()},${newFlat.getHouse()?.getNumberOfFloors()}]"
    }

    fun automaticallyExecution(id: Long, data: String): String {
        val args = data.replace("[", "").replace("]", "").split(",")

        try {
            Flat()
                .setId(id)
                .setName(args[0])
                .setCoordinates(Coordinates(args[1].toLong(), args[2].toFloat()))
                .setArea(args[3].toLong())
                .setNumberOfRooms(args[4].toLong())
                .setPrice(args[5].toLong())
                .setBalcony(args[6].toBoolean())
                .setFurnish(Furnish.valueOf(args[7].uppercase().trim()))
                .setHouse(House(args[8], args[9].toInt(), args[10].toLong()))

            if (args.size != 11) return "Invalid arguments."
            val newFlat = data.replaceFirst("[", "[$id,")

            return newFlat
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }
}
