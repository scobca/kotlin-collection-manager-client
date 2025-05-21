package org.itmo.kotlincollectionmanagerclient.validators

import org.itmo.kotlincollectionmanagerclient.collection.Coordinates
import org.itmo.kotlincollectionmanagerclient.collection.Flat
import org.itmo.kotlincollectionmanagerclient.collection.Furnish
import org.itmo.kotlincollectionmanagerclient.collection.House
import org.itmo.kotlincollectionmanagerclient.storages.TokensStorage
import org.itmo.kotlincollectionmanagerclient.utils.AdvancedScanner
import org.itmo.kotlincollectionmanagerclient.utils.TcpConnectionFactory
import org.itmo.kotlincollectionmanagerclient.validators.interfaces.BasicCommandValidator
import org.springframework.stereotype.Component

@Component
class UpdateValidator(
    private val scanner: AdvancedScanner,
    private val tcpConnectionFactory: TcpConnectionFactory
) : BasicCommandValidator {
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
        val oldFlat = tcpConnectionFactory.sendMessage("getElementById $id")

        if (oldFlat.contains("Flat with id $id not found")) {
            return "Flat with id $id not found"
        } else {
            val data = oldFlat.replace("[", "").replace("]", "").split(",")

            val flat = Flat()
            val coordinates = Coordinates()
            val house = House()

            scanner.cycleUpdateScan("Enter new element name (old name: ${data[1]}): ", flat.getName()) { it }
                ?.let { flat.setName(it) }

            scanner.cycleUpdateScan(
                "Enter new coordinate x (old coordinates: ${data[2]}): ",
                flat.getCoordinates()?.getX()
            ) { it.toLong() }
                ?.let { coordinates.setX(it) }

            scanner.cycleUpdateScan(
                "Enter coordinates y (old coordinates: ${data[3]}): ",
                coordinates.getY()
            ) { it.toFloat() }
                ?.let { coordinates.setY(it) }
            flat.setCoordinates(coordinates)

            scanner.cycleUpdateScan(
                "Enter new flat area (old area: ${data[4]}): ",
                flat.getArea()
            ) { it.toLong() }?.let { flat.setArea(it) }

            scanner.cycleUpdateScan(
                "Enter new number of rooms (old number of rooms: ${data[5]}): ",
                flat.getNumberOfRooms()
            ) { it.toLong() }
                ?.let { flat.setNumberOfRooms(it) }

            scanner.cycleUpdateScan(
                "Enter new price (old price: ${data[6]}): ",
                flat.getPrice()
            ) { it.toLong() }
                ?.let { flat.setPrice(it) }

            scanner.cycleUpdateScan(
                "Enter new balcony (old balcony: ${data[7]}): ",
                flat.getBalcony()
            ) { it.toBoolean() }
                ?.let { flat.setBalcony(it) }

            scanner.cycleUpdateScan(
                "Enter new furnish type (DESIGNER | FINE | LITTLE) (old furnish type: ${data[8]}): ",
                flat.getFurnish()
            ) {
                try {
                    Furnish.valueOf(it.uppercase().trim())
                } catch (_: IllegalArgumentException) {
                    throw IllegalArgumentException("Invalid furnish type. Please try again.")
                }
            }?.let { flat.setFurnish(it) }

            scanner.cycleUpdateScan(
                "Enter new house name (old house name: ${data[9]}): ",
                house.getName()
            ) { it }
                ?.let { house.setName(it) }

            scanner.cycleUpdateScan(
                "Enter new year of building a house (old year of building: ${data[10]}): ",
                house.getYear()
            ) { it.toInt() }
                ?.let { house.setYear(it) }

            scanner.cycleUpdateScan(
                "Enter new number of floors (old nu,ber of floors: ${data[11]}): ",
                house.getNumberOfFloors()
            ) { it.toLong() }
                ?.let { house.setNumberOfFloors(it) }
            flat.setHouse(house)

            return "[${flat.getId()},${flat.getName()},${
                flat.getCoordinates()?.getX()
            },${
                flat.getCoordinates()?.getY()
            },${flat.getArea()},${flat.getNumberOfRooms()},${flat.getPrice()},${flat.getBalcony()},${flat.getFurnish()},${
                flat.getHouse()?.getName()
            },${flat.getHouse()?.getYear()},${flat.getHouse()?.getNumberOfFloors()},${TokensStorage.getAccessToken()}]"
        }
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

            val lastIndex = newFlat.lastIndexOf(']')

            if (lastIndex == -1) return newFlat
            val res = newFlat.substring(0, lastIndex) + ",${TokensStorage.getAccessToken()}]"

            return res
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }
}