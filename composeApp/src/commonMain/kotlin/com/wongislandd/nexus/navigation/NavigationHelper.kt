package com.wongislandd.nexus.navigation

import androidx.navigation.NavController

class NavigationHelper(private val navigationItemRegistry: NavigationItemRegistry) {

    fun navigate(
        navigationController: NavController,
        navigationKey: String,
        args: Map<String, Any?> = emptyMap()
    ): Boolean {
        val navigationItem = navigationItemRegistry.getNavigationItem(navigationKey)
        if (navigationItem != null) {
            navigationController.navigate(navigationItem.reconstructRoute(args))
            return true
        } else {
            return false
        }
    }
}