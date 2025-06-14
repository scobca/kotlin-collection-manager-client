package org.itmo.kotlincollectionmanagerclient.controllers

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleFloatProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableRow
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.stage.Modality
import javafx.stage.Stage
import org.itmo.kotlincollectionmanagerclient.api.dto.FlatDto
import org.itmo.kotlincollectionmanagerclient.controllers.i18n.Localizer
import org.itmo.kotlincollectionmanagerclient.controllers.router.ViewManager
import org.itmo.kotlincollectionmanagerclient.services.CommandsService
import org.itmo.kotlincollectionmanagerclient.storages.CurrentFlatStorage.setFlatId
import org.itmo.kotlincollectionmanagerclient.storages.FlatsStorage.setFlatsCollection
import org.itmo.kotlincollectionmanagerclient.storages.TokensStorage.getUsername
import org.itmo.kotlincollectionmanagerclient.storages.TokensStorage.resetUserData
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Component
import java.util.Locale
import java.util.ResourceBundle

@Component
class MainPageController(
    private val router: ViewManager,
    private val localizer: Localizer,
    private val commandsService: CommandsService,
    private val applicationContext: ConfigurableApplicationContext,
) {
    @FXML
    lateinit var filterParam: TextField

    @FXML
    lateinit var filterLabel: Label

    @FXML
    private lateinit var menuLabel: Label

    @FXML
    private lateinit var collectionLabel: Label

    @FXML
    private lateinit var tableView: TableView<FlatDto>

    @FXML
    lateinit var logoutButton: Button

    @FXML
    lateinit var userLabel: Label

    @FXML
    lateinit var languageField: ChoiceBox<String>

    @FXML
    lateinit var idLabel: TableColumn<FlatDto, Long>

    @FXML
    lateinit var flatNameLabel: TableColumn<FlatDto, String>

    @FXML
    lateinit var xCordLabel: TableColumn<FlatDto, Long>

    @FXML
    lateinit var yCordLabel: TableColumn<FlatDto, Float>

    @FXML
    lateinit var flatAreaLabel: TableColumn<FlatDto, Long>

    @FXML
    lateinit var numberOfRoomsLabel: TableColumn<FlatDto, Long>

    @FXML
    lateinit var priceLabel: TableColumn<FlatDto, Long>

    @FXML
    lateinit var balconyLabel: TableColumn<FlatDto, Boolean>

    @FXML
    lateinit var furnishLabel: TableColumn<FlatDto, String>

    @FXML
    lateinit var houseNameLabel: TableColumn<FlatDto, String>

    @FXML
    lateinit var houseYearLabel: TableColumn<FlatDto, Int>

    @FXML
    lateinit var numberOfFloorsLabel: TableColumn<FlatDto, Long>

    @FXML
    lateinit var removeIfLowerKeyButton: Button

    @FXML
    lateinit var getAveragePriceButton: Button

    @FXML
    lateinit var clearButton: Button

    @FXML
    lateinit var removeAllByBalconyButton: Button

    @FXML
    lateinit var insertButton: Button

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
            val flats: ObservableList<FlatDto> =
                FXCollections.observableArrayList(commandsService.getFlats().sortedBy { it.id })

            setFlatsCollection(flats)
            tableView.items = flats
        } catch (e: Exception) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Error"
            alert.headerText = e.message
            alert.showAndWait()
        }

        tableView.setRowFactory {
            object : TableRow<FlatDto>() {
                override fun updateItem(item: FlatDto?, empty: Boolean) {
                    super.updateItem(item, empty)
                    style = if (item == null || empty) {
                        ""
                    } else {
                        if (item.user.email == getUsername()) {
                            "-fx-background-color: lightgreen;"
                        } else {
                            ""
                        }
                    }
                }
            }
        }

        tableView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            if (newValue != null) {
                setFlatId(newValue.id)
                router.showPage("/fxml/FlatPage.fxml", currentBundle)
            }
        }

        filterParam.textProperty().addListener { _, _, _ -> filterContainsName() }

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

        insertButton.text = currentBundle.getString("main.insertButton")
        removeAllByBalconyButton.text = currentBundle.getString("main.removeAllByBalconyButton")
        clearButton.text = currentBundle.getString("main.clearButton")
        getAveragePriceButton.text = currentBundle.getString("main.getAveragePriceButton")
        removeIfLowerKeyButton.text = currentBundle.getString("main.removeIfLowerKeyButton")
    }


    @FXML
    fun logout() {
        resetUserData()
        router.showPage("/fxml/LoginPage.fxml", currentBundle)
    }

    @FXML
    fun insert() {
        router.showPage("/fxml/InsertPage.fxml", currentBundle)
    }

    @FXML
    fun removeAllByBalcony() {
        val loader = FXMLLoader(javaClass.getResource("/fxml/RemoveAllByBalcony.fxml"))
        loader.setControllerFactory { clazz ->
            applicationContext.getBean(clazz)
        }
        val root = loader.load<Parent>()

        val popupStage = Stage()
        popupStage.scene = Scene(root)
        popupStage.title = "Remove All By Balcony"
        popupStage.initModality(Modality.APPLICATION_MODAL)
        popupStage.isResizable = false

        val controller = loader.getController<RemoveByBalconyController>()
        controller.setStage(popupStage)

        popupStage.show()
    }

    @FXML
    fun removeIfLowerKey() {
        val loader = FXMLLoader(javaClass.getResource("/fxml/RemoveIfLowerPopup.fxml"))
        loader.setControllerFactory { clazz ->
            applicationContext.getBean(clazz)
        }
        val root = loader.load<Parent>()

        val popupStage = Stage()
        popupStage.scene = Scene(root)
        popupStage.title = "Remove If Lower"
        popupStage.initModality(Modality.APPLICATION_MODAL)
        popupStage.isResizable = false

        val controller = loader.getController<RemoveIfLowerPopupController>()
        controller.setStage(popupStage)

        popupStage.show()
    }

    @FXML
    fun clear() {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Warning"
        alert.headerText = "Вы действительно хотите удалить все свои объекты коллекции?"
        val res = alert.showAndWait()

        if (res.isPresent && res.get() == ButtonType.OK) {
            commandsService.clear()
            router.showPage("/fxml/MainPage.fxml", currentBundle)
        }
    }

    @FXML
    fun getAveragePrice() {
        val price = commandsService.getAveragePrice()

        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "Average Price"
        alert.contentText = price
        alert.showAndWait()
    }

    @FXML
    fun filterContainsName() {
        if (!filterParam.text.isEmpty()) {
            val flats: ObservableList<FlatDto> =
                FXCollections.observableArrayList(
                    commandsService.getFlats()
                        .filter { it.name.contains(filterParam.text.replace(" ", "_"), ignoreCase = true) }
                        .sortedBy { it.id })

            tableView.items = flats
        } else {
            val flats: ObservableList<FlatDto> =
                FXCollections.observableArrayList(commandsService.getFlats().sortedBy { it.id })

            tableView.items = flats
        }
    }
}