package com.davidbarbosa.siliconpowertv.data.local

import java.util.Locale

object LanguageProvider {
    fun tmdbLanguage(): String {
        val l = Locale.getDefault()
        val lang = l.language.ifBlank { "en" }
        val country = l.country.ifBlank {
            when (lang) {
                "es" -> "ES"
                "en" -> "US"
                else -> "US"
            }
        }
        return "$lang-$country"
    }
}
