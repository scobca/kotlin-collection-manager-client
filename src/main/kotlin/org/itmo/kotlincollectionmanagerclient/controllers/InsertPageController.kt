package org.itmo.kotlincollectionmanagerclient.controllers

import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.TitledPane
import org.itmo.kotlincollectionmanagerclient.collection.Furnish
import org.itmo.kotlincollectionmanagerclient.controllers.i18n.Localizer
import org.itmo.kotlincollectionmanagerclient.controllers.router.ViewManager
import org.itmo.kotlincollectionmanagerclient.services.CommandsService
import org.itmo.kotlincollectionmanagerclient.storages.TokensStorage
import org.springframework.stereotype.Component
import java.util.Locale
import java.util.ResourceBundle

@Component
class InsertPageController(
    private val localizer: Localizer,
    private val router: ViewManager,
    private val commandsService: CommandsService
) {
    @FXML
    lateinit var titleLabel: TitledPane

    @FXML
    lateinit var languageField: ChoiceBox<String>

    @FXML
    lateinit var backButton: Button

    @FXML
    lateinit var createButton: Button

    @FXML
    lateinit var furnishField: ChoiceBox<Furnish>

    @FXML
    lateinit var numberOfFloorsLabel: Label

    @FXML
    lateinit var houseNameLabel: Label

    @FXML
    lateinit var balconyLabel: Label

    @FXML
    lateinit var numberOfRoomsLabel: Label

    @FXML
    lateinit var xLabel: Label

    @FXML
    lateinit var yLabel: Label

    @FXML
    lateinit var nameLabel: Label

    @FXML
    lateinit var yearLabel: Label

    @FXML
    lateinit var furnishLabel: Label

    @FXML
    lateinit var priceLabel: Label

    @FXML
    lateinit var areaLabel: Label

    @FXML
    private lateinit var idLabel: Label

    @FXML
    lateinit var idField: TextField

    @FXML
    lateinit var nameField: TextField

    @FXML
    lateinit var areaField: TextField

    @FXML
    lateinit var priceField: TextField

    @FXML
    lateinit var numberOfFloorsField: TextField

    @FXML
    lateinit var houseNameField: TextField

    @FXML
    lateinit var balconyField: TextField

    @FXML
    lateinit var numberOfRoomsField: TextField

    @FXML
    lateinit var yField: TextField

    @FXML
    lateinit var xField: TextField

    @FXML
    lateinit var yearField: TextField

    private lateinit var currentBundle: ResourceBundle

    @FXML
    fun initialize() {
        furnishField.getItems().addAll(Furnish.DESIGNER, Furnish.FINE, Furnish.LITTLE)

        languageField.getItems().addAll("ru", "no", "es", "el")
        languageField.value = localizer.getLanguage()

        updateBundle(Locale.forLanguageTag(languageField.value))

        languageField.setOnAction { event ->
            val selected: String? = languageField.value

            localizer.setLanguage(selected.toString())
            localizer.updateLocalization("/fxml/InsertPage.fxml")
        }
    }

    private fun updateBundle(locale: Locale) {
        ResourceBundle.clearCache()
        currentBundle = ResourceBundle.getBundle("i18n.Bundle", locale)
        updateTexts()
    }

    fun updateTexts() {
        idLabel.text = currentBundle.getString("insert.flatId")
        xLabel.text = currentBundle.getString("insert.coordinatesX")
        areaLabel.text = currentBundle.getString("insert.flatArea")
        priceLabel.text = currentBundle.getString("insert.price")
        furnishLabel.text = currentBundle.getString("insert.furnish")
        yearLabel.text = currentBundle.getString("insert.year")
        nameLabel.text = currentBundle.getString("insert.flatName")
        yLabel.text = currentBundle.getString("insert.coordinatesY")
        numberOfRoomsLabel.text = currentBundle.getString("insert.numberOfRooms")
        balconyLabel.text = currentBundle.getString("insert.balcony")
        houseNameLabel.text = currentBundle.getString("insert.houseName")
        numberOfFloorsLabel.text = currentBundle.getString("insert.numberOfFloors")
        createButton.text = currentBundle.getString("insert.create")
        backButton.text = currentBundle.getString("insert.back")
    }

    @FXML
    fun create() {
        val body =
            "[${idField.text},${nameField.text},${xField.text},${yField.text}," +
                    "${areaField.text},${numberOfRoomsField.text},${priceField.text},${balconyField.text}," +
                    "${furnishField.value},${houseNameField.text},${yearField.text},${numberOfFloorsField.text}," +
                    "${TokensStorage.getAccessToken()}]"

        try {
            commandsService.insert(body)
            router.showPage("/fxml/MainPage.fxml", currentBundle)
        } catch (e: Exception) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Error"
            alert.headerText = e.message
            alert.showAndWait()
        }
    }

    @FXML
    fun goBack() {
        router.showPage("/fxml/MainPage.fxml", currentBundle)
    }
}