package org.itmo.kotlincollectionmanagerclient.validators

import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import org.itmo.kotlincollectionmanagerclient.collection.Furnish

fun checkFieldsTypesValid(
    idField: TextField,
    xField: TextField,
    yField: TextField,
    areaField: TextField,
    numberOfRoomsField: TextField,
    priceField: TextField,
    yearField: TextField,
    numberOfFloorsField: TextField,
    nameField: TextField,
    houseNameField: TextField,
    balconyField: ChoiceBox<Boolean>,
    furnishField: ChoiceBox<Furnish>,
): Boolean {
    var mistakes = 0
    val fieldsToCheck = listOf(
        idField to { text: String -> text.toLongOrNull() },
        xField to { text: String -> text.toLongOrNull() },
        yField to { text: String -> text.toFloatOrNull() },
        areaField to { text: String -> text.toLongOrNull() },
        numberOfRoomsField to { text: String -> text.toLongOrNull() },
        priceField to { text: String -> text.toLongOrNull() },
        yearField to { text: String -> text.toLongOrNull() },
        numberOfFloorsField to { text: String -> text.toLongOrNull() },
    )

    fieldsToCheck.forEach { (field, parseFunc) ->
        if (parseFunc(field.text) == null) {
            field.clear()
            field.style = "-fx-border-color: red;"
            mistakes++
        } else {
            field.style = ""
        }
    }

    val textFieldsToCheck = listOf(nameField, houseNameField)
    textFieldsToCheck.forEach { field ->
        if (field.text.isBlank()) {
            field.style = "-fx-border-color: red;"
            mistakes++
        } else {
            field.style = ""
        }
    }

    val choiceBoxesToCheck = listOf(balconyField, furnishField)
    choiceBoxesToCheck.forEach { choiceBox ->
        if (choiceBox.value == null) {
            choiceBox.style = "-fx-border-color: red;"
            mistakes++
        } else {
            choiceBox.style = ""
        }
    }

    return mistakes == 0
}