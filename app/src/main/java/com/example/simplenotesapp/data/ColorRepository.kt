package com.example.simplenotesapp.data



class ColorRepository {
    private val colors = listOf(
        Pair(0xFFFFDADB, "Pink"),
        Pair(0xFFD1EAED, "Turquoise"),
        Pair(0xFFFFD4AA, "Orange"),
        Pair(0xFFFDF3B4, "Yellow")
    )
    fun getColors(): List<Pair<Long, String>> {
        return colors
    }

}