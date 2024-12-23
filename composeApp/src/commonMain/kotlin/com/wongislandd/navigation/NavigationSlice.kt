package com.wongislandd.navigation

import com.wongislandd.nexus.navigation.NavigationHelper
import com.wongislandd.nexus.navigation.NavigationItem
import com.wongislandd.nexus.navigation.NavigationItemRegistry
import com.wongislandd.nexus.viewmodel.ViewModelSlice

class NavigationSlice(
    supportedNavigationItems: Set<NavigationItem>,
    navigationItemRegistry: NavigationItemRegistry,
    val navigationHelper: NavigationHelper
) : ViewModelSlice() {

    init {
        navigationItemRegistry.register(*supportedNavigationItems.toTypedArray())
    }
}