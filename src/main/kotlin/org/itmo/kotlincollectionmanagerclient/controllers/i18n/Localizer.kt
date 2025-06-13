package org.itmo.kotlincollectionmanagerclient.controllers.i18n

import org.itmo.kotlincollectionmanagerclient.controllers.router.ViewManager
import org.springframework.stereotype.Component
import java.util.Locale
import java.util.ResourceBundle

@Component
class Localizer(private val router: ViewManager) {
    var lang: String = "ru"

    fun setLanguage(lang: String) = apply { this.lang = lang }
    fun getLanguage(): String = lang

    fun getBundle(locale: Locale): ResourceBundle = ResourceBundle.getBundle("i18n.Bundle", locale)

    fun updateLocalization(route: String) {
        val locale = Locale.forLanguageTag(getLanguage())
        val bundle = ResourceBundle.getBundle("i18n.Bundle", locale)

        router.showPage(route, bundle)
    }
}