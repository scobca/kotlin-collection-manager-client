package org.itmo.kotlincollectionmanagerclient.controllers

import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import org.itmo.kotlincollectionmanagerclient.api.dto.FlatDto
import org.itmo.kotlincollectionmanagerclient.collection.Furnish
import org.itmo.kotlincollectionmanagerclient.controllers.i18n.Localizer
import org.itmo.kotlincollectionmanagerclient.controllers.router.ViewManager
import org.itmo.kotlincollectionmanagerclient.services.CommandsService
import org.itmo.kotlincollectionmanagerclient.storages.CurrentFlatStorage.clearFlatStorage
import org.itmo.kotlincollectionmanagerclient.storages.CurrentFlatStorage.getFlatId
import org.itmo.kotlincollectionmanagerclient.storages.TokensStorage
import org.itmo.kotlincollectionmanagerclient.storages.TokensStorage.getUsername
import org.itmo.kotlincollectionmanagerclient.validators.checkFieldsTypesValid
import org.springframework.stereotype.Component
import java.util.Locale
import java.util.ResourceBundle

@Component
class FlatPageController(
    private val localizer: Localizer,
    private val commandsService: CommandsService,
    private val router: ViewManager
) {
    @FXML lateinit var backButton: Button
    @FXML lateinit var removeButton: Button
    @FXML lateinit var updateButton: Button

    @FXML lateinit var flatUserLabel: Label
    @FXML lateinit var numberOfFloorsLabel: Label
    @FXML lateinit var houseNameLabel: Label
    @FXML lateinit var balconyLabel: Label
    @FXML lateinit var numberOfRoomsLabel: Label
    @FXML lateinit var yLabel: Label
    @FXML lateinit var nameLabel: Label
    @FXML lateinit var yearLabel: Label
    @FXML lateinit var furnishLabel: Label
    @FXML lateinit var priceLabel: Label
    @FXML lateinit var areaLabel: Label
    @FXML lateinit var xLabel: Label
    @FXML lateinit var idLabel: Label
    
    @FXML lateinit var languageField: ChoiceBox<String>
    @FXML lateinit var furnishField: ChoiceBox<Furnish>
    @FXML lateinit var numberOfFloorsField: TextField
    @FXML lateinit var yField: TextField
    @FXML lateinit var houseNameField: TextField
    @FXML lateinit var balconyField: ChoiceBox<Boolean>
    @FXML lateinit var numberOfRoomsField: TextField
    @FXML lateinit var nameField: TextField
    @FXML lateinit var yearField: TextField
    @FXML lateinit var priceField: TextField
    @FXML lateinit var areaField: TextField
    @FXML lateinit var xField: TextField
    @FXML lateinit var idField: TextField

    private lateinit var currentBundle: ResourceBundle

    @FXML
    fun initialize() {
        languageField.getItems().addAll("ru", "no", "es", "el")
        languageField.value = localizer.getLanguage()

        updateBundle(Locale.forLanguageTag(languageField.value))

        languageField.setOnAction { event ->
            val selected: String? = languageField.value

            localizer.setLanguage(selected.toString())
            localizer.updateLocalization("/fxml/FlatPage.fxml")
        }

        val flats = commandsService.getFlats().filter { flat -> flat.id == getFlatId() }
        fillGaps(flats[0])
        blockFields(flats[0])

        furnishField.getItems().addAll(Furnish.DESIGNER, Furnish.FINE, Furnish.LITTLE)
        balconyField.getItems().addAll(true, false)

        idField.isDisable = true
        updateButton.isDisable = true

        listOf(
            numberOfFloorsField, yField, houseNameField, numberOfRoomsField,
            nameField, yearField, priceField, areaField, xField, idField
        ).forEach { field ->
            field.textProperty().addListener { _, oldValue, newValue ->
                if (oldValue != newValue) {
                    updateButton.isDisable = false
                }
            }
        }

        listOf(furnishField, balconyField).forEach { choiceBox ->
            choiceBox.valueProperty().addListener { _, oldValue, newValue ->
                if (oldValue != newValue) {
                    updateButton.isDisable = false
                }
            }
        }
    }

    fun fillGaps(flat: FlatDto) {
        if (flat.user.email == getUsername()) {
            flatUserLabel.text = "${currentBundle.getString("flat.flatUser")}: ${getUsername()} (${
                currentBundle.getString("flat.youFlatUser")
            })"
        } else {
            flatUserLabel.text = "${currentBundle.getString("flat.flatUser")}: ${flat.user.email}"
        }

        idField.text = flat.id.toString()
        nameField.text = flat.name
        xField.text = flat.coordinates.x.toString()
        yField.text = flat.coordinates.y.toString()
        areaField.text = flat.area.toString()
        numberOfRoomsField.text = flat.numberOfRooms.toString()
        priceField.text = flat.price.toString()
        balconyField.value = flat.balcony
        furnishField.value = flat.furnish
        houseNameField.text = flat.house.name
        yearField.text = flat.house.year.toString()
        numberOfFloorsField.text = flat.house.numberOfFloors.toString()
    }

    fun blockFields(flat: FlatDto) {
        if (flat.user.email != getUsername()) {
            listOf(
                numberOfFloorsField, yField, houseNameField,
                balconyField, numberOfRoomsField, nameField,
                yearField, priceField, areaField,
                xField, idField, furnishField,
                updateButton, removeButton
            ).forEach { field ->
                field.isDisable = true
            }
        }
    }

    private fun updateBundle(locale: Locale) {
        ResourceBundle.clearCache()
        currentBundle = ResourceBundle.getBundle("i18n.Bundle", locale)
        updateTexts()
    }

    fun updateTexts() {
        idLabel.text = currentBundle.getString("flat.flatId")
        xLabel.text = currentBundle.getString("flat.coordinatesX")
        yLabel.text = currentBundle.getString("flat.coordinatesY")
        areaLabel.text = currentBundle.getString("flat.flatArea")
        priceLabel.text = currentBundle.getString("flat.price")
        furnishLabel.text = currentBundle.getString("flat.furnish")
        yearLabel.text = currentBundle.getString("flat.year")
        nameLabel.text = currentBundle.getString("flat.flatName")
        numberOfRoomsLabel.text = currentBundle.getString("flat.numberOfRooms")
        balconyLabel.text = currentBundle.getString("flat.balcony")
        houseNameLabel.text = currentBundle.getString("flat.houseName")
        numberOfFloorsLabel.text = currentBundle.getString("flat.numberOfFloors")
        updateButton.text = currentBundle.getString("flat.update")
        removeButton.text = currentBundle.getString("flat.remove")
        backButton.text = currentBundle.getString("flat.back")
    }

    fun update() {
        if (!checkFieldsTypesValid(
                idField,
                xField,
                yField,
                areaField,
                numberOfRoomsField,
                priceField,
                yearField,
                numberOfFloorsField,
                nameField,
                houseNameField,
                balconyField,
                furnishField
            )) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Ошибка в данных"
            alert.headerText = "Повторите ввод данных в корректном формате!"
            val res = alert.showAndWait()

            if (res == ButtonType.OK) alert.close()
        } else {
            val body =
                "[${idField.text},${nameField.text.replace(" ", "_").trim()},${xField.text},${yField.text}," +
                        "${areaField.text},${numberOfRoomsField.text},${priceField.text},${balconyField.value}," +
                        "${furnishField.value},${
                            houseNameField.text.replace(" ", "_").trim()
                        },${yearField.text},${numberOfFloorsField.text}," +
                        "${TokensStorage.getAccessToken()}]"

            try {
                commandsService.update(body)
                router.showPage("/fxml/MainPage.fxml", currentBundle)
            } catch (e: Exception) {
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Error"
                alert.headerText = e.message
                alert.showAndWait()
            }
        }
    }

    fun remove() {
        val alert = Alert(Alert.AlertType.WARNING)
        alert.title = "Warning"
        alert.headerText = "Вы действительно хотите удалить квартиру?"

        val res = alert.showAndWait()

        if (res.isPresent && res.get() == ButtonType.OK) {
            println(getFlatId())
            try {
                commandsService.remove(getFlatId()!!)
                clearFlatStorage()

                router.showPage("/fxml/MainPage.fxml", currentBundle)
            } catch (e: Exception) {
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Error"
                alert.contentText = e.message
                alert.showAndWait()
            }
        }
    }

    fun replace() {}

    fun goBack() {
        router.showPage("/fxml/MainPage.fxml", currentBundle)
    }
}