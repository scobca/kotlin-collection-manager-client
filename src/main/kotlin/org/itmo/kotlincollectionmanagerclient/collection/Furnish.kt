package org.itmo.kotlincollectionmanagerclient.collection

enum class Furnish {
    DESIGNER,
    FINE,
    LITTLE,
    NONE;

    companion object {
        fun validate(type: String): Furnish {
            return try {
                Furnish.valueOf(type.uppercase())
            } catch (_: IllegalArgumentException) {
                NONE
            }
        }
    }
}