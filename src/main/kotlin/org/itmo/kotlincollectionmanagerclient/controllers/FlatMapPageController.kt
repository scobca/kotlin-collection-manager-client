package org.itmo.kotlincollectionmanagerclient.controllers

import javafx.fxml.FXML
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.ScatterChart
import javafx.scene.chart.XYChart
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.input.MouseEvent
import org.itmo.kotlincollectionmanagerclient.api.dto.FlatDto
import org.itmo.kotlincollectionmanagerclient.controllers.i18n.Localizer
import org.itmo.kotlincollectionmanagerclient.controllers.router.ViewManager
import org.itmo.kotlincollectionmanagerclient.services.CommandsService
import org.itmo.kotlincollectionmanagerclient.storages.CurrentFlatStorage.setFlatId
import org.springframework.stereotype.Component
import java.util.Locale
import java.util.ResourceBundle

@Component
class FlatMapPageController(
    private val localizer: Localizer,
    private val router: ViewManager,
    private val commandsService: CommandsService,
) {

    @FXML
    lateinit var languageField: ChoiceBox<String>

    @FXML
    lateinit var goToMainPageButton: Button

    @FXML
    private lateinit var scatterChart: ScatterChart<Number, Number>

    @FXML
    private lateinit var xAxis: NumberAxis

    @FXML
    private lateinit var yAxis: NumberAxis

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

        val flats = commandsService.getFlats().sortedBy { it.id }

        adjustAxis(flats)
        plotFlats(flats)
    }

    private fun updateBundle(locale: Locale) {
        ResourceBundle.clearCache()
        currentBundle = ResourceBundle.getBundle("i18n.Bundle", locale)
        updateTexts()
    }

    fun updateTexts() {}

    @FXML
    fun goToMainPage() = router.showPage("/fxml/MainPage.fxml", currentBundle)

    fun adjustAxis(flats: List<FlatDto>) {
        val xValues = flats.map { it.coordinates.x }
        val yValues = flats.map { it.coordinates.y }

        if (xValues.isNotEmpty()) {
            val minX = xValues.minOrNull()?.toDouble() ?: 0.0
            val maxX = xValues.maxOrNull()?.toDouble() ?: 100.0
            val rangeX = maxX - minX
            val paddingX = rangeX * 0.1

            xAxis.lowerBound = minX - paddingX
            xAxis.upperBound = maxX + paddingX
        }

        if (yValues.isNotEmpty()) {
            val minY = yValues.minOrNull()?.toDouble() ?: 0.0
            val maxY = yValues.maxOrNull()?.toDouble() ?: 100.0
            val rangeY = maxY - minY
            val paddingY = rangeY * 0.1

            yAxis.lowerBound = minY - paddingY
            yAxis.upperBound = maxY + paddingY
        }
    }

    fun plotFlats(flats: Collection<FlatDto>) {
        val series = XYChart.Series<Number, Number>()
        series.name = "Квартиры"

        for (flat in flats) {
            val x = flat.coordinates.x.toDouble()
            val y = flat.coordinates.y.toDouble()

            val dataPoint = XYChart.Data<Number, Number>(x, y)
            series.data.add(dataPoint)

            dataPoint.nodeProperty().addListener { _, _, node ->
                if (node != null) {
                    node.userData = flat
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED) { event ->
                        val clickedFlat = node.userData as FlatDto
                        setFlatId(clickedFlat.id)
                        router.showPage("/fxml/FlatPage.fxml", currentBundle)
                    }
                }
            }
        }

        scatterChart.data.clear()
        scatterChart.data.add(series)
    }
}