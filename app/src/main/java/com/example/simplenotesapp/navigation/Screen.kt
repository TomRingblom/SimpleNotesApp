package com.example.simplenotesapp.navigation

enum class Screen(val route: String) {
    Home("Home"),
    Edit("Edit/{id}"),
    Add("Add")
}