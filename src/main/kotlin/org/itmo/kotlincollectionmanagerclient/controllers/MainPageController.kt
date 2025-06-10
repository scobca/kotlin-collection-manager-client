package org.itmo.kotlincollectionmanagerclient.controllers

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleFloatProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import org.itmo.kotlincollectionmanagerclient.api.dto.FlatDto
import org.itmo.kotlincollectionmanagerclient.controllers.i18n.Localizer
import org.itmo.kotlincollectionmanagerclient.controllers.router.ViewManager
import org.itmo.kotlincollectionmanagerclient.services.CommandsService
import org.itmo.kotlincollectionmanagerclient.storages.TokensStorage.getUsername
import org.itmo.kotlincollectionmanagerclient.storages.TokensStorage.resetUserData
import org.springframework.stereotype.Component
import java.util.Locale
import java.util.ResourceBundle

@Component
class MainPageController(
    private val router: ViewManager,
    private val localizer: Localizer,
    private val commandsService: CommandsService
) {

    @FXML private lateinit var menuLabel: Label
    @FXML private lateinit var collectionLabel: Label
    @FXML private lateinit var tableView: TableView<FlatDto>
    @FXML lateinit var logoutButton: Button
    @FXML lateinit var userLabel: Label
    @FXML lateinit var languageField: ChoiceBox<String>

    @FXML lateinit var idLabel: TableColumn<FlatDto, Long>
    @FXML lateinit var flatNameLabel: TableColumn<FlatDto, String>
    @FXML lateinit var xCordLabel: TableColumn<FlatDto, Long>
    @FXML lateinit var yCordLabel: TableColumn<FlatDto, Float>
    @FXML lateinit var flatAreaLabel: TableColumn<FlatDto, Long>
    @FXML lateinit var numberOfRoomsLabel: TableColumn<FlatDto, Long>
    @FXML lateinit var priceLabel: TableColumn<FlatDto, Long>
    @FXML lateinit var balconyLabel: TableColumn<FlatDto, Boolean>
    @FXML lateinit var furnishLabel: TableColumn<FlatDto, String>
    @FXML lateinit var houseNameLabel: TableColumn<FlatDto, String>
    @FXML lateinit var houseYearLabel: TableColumn<FlatDto, Int>
    @FXML lateinit var numberOfFloorsLabel: TableColumn<FlatDto, Long>

    private lateinit var currentBundle: ResourceBundle

    @FXML
    fun initialize() {
        tableView.columnResizePolicy = TableView.UNCONSTRAINED_RESIZE_POLICY
        userLabel.text = getUsername().toString()

        idLabel.setCellValueFactory { SimpleLongProperty(it.value.id).asObject() }
        flatNameLabel.setCellValueFactory { SimpleStringProperty(it.value.name) }
        xCordLabel.setCellValueFactory { SimpleLongProperty(it.value.coordinates.x).asObject() }
        yCordLabel.setCellValueFactory { SimpleFloatProperty(it.value.coordinates.y).asObject() }
        flatAreaLabel.setCellValueFactory { SimpleLongProperty(it.value.area).asObject() }
        numberOfRoomsLabel.setCellValueFactory { SimpleLongProperty(it.value.numberOfRooms).asObject() }
        priceLabel.setCellValueFactory { SimpleLongProperty(it.value.price).asObject() }
        balconyLabel.setCellValueFactory { SimpleBooleanProperty(it.value.balcony) }
        furnishLabel.setCellValueFactory { SimpleStringProperty(it.value.furnish.toString()) }
        houseNameLabel.setCellValueFactory { SimpleStringProperty(it.value.house.name) }
        houseYearLabel.setCellValueFactory { SimpleIntegerProperty(it.value.house.year).asObject() }
        numberOfFloorsLabel.setCellValueFactory { SimpleLongProperty(it.value.house.numberOfFloors).asObject() }

        try {
            val flats : ObservableList<FlatDto> = FXCollections.observableArrayList(commandsService.getFlats().sortedBy { it.id })
            tableView.items = flats
        } catch (e: Exception) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Error"
            alert.headerText = e.message
            alert.showAndWait()
        }

        languageField.getItems().addAll("ru", "no", "es", "el")
        languageField.value = localizer.getLanguage()

        updateBundle(Locale.forLanguageTag(languageField.value))

        languageField.setOnAction { event ->
            val selected: String? = languageField.value

            localizer.setLanguage(selected.toString())
            localizer.updateLocalization("/fxml/MainPage.fxml")
        }
    }

    private fun updateBundle(locale: Locale) {
        ResourceBundle.clearCache()
        currentBundle = ResourceBundle.getBundle("i18n.Bundle", locale)
        updateTexts()
    }

    fun updateTexts() {
        menuLabel.text = currentBundle.getString("main.menuLabel")
        collectionLabel.text = currentBundle.getString("main.collectionLabel")
        logoutButton.text = currentBundle.getString("main.logoutButton")

        idLabel.text = currentBundle.getString("main.idLabel")
        flatNameLabel.text = currentBundle.getString("main.flatNameLabel")
        xCordLabel.text = currentBundle.getString("main.xCordLabel")
        yCordLabel.text = currentBundle.getString("main.yCordLabel")
        flatAreaLabel.text = currentBundle.getString("main.flatAreaLabel")
        numberOfRoomsLabel.text = currentBundle.getString("main.numberOfRoomsLabel")
        priceLabel.text = currentBundle.getString("main.priceLabel")
        balconyLabel.text = currentBundle.getString("main.balconyLabel")
        furnishLabel.text = currentBundle.getString("main.furnishLabel")
        houseNameLabel.text = currentBundle.getString("main.houseNameLabel")
        houseYearLabel.text = currentBundle.getString("main.houseYearLabel")
        numberOfFloorsLabel.text = currentBundle.getString("main.numberOfFloorsLabel")
    }


    @FXML
    fun logout() {
        resetUserData()
        router.showPage("/fxml/LoginPage.fxml", currentBundle)
    }
}