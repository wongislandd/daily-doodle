package com.wongislandd.navigation

import com.wongislandd.nexus.navigation.NavigationItem

enum class NavigationItemKey {
    HOME,
    DRAWING_BOARD,
    EXPLORE
}

val supportedNavigationItems = mutableMapOf(
    NavigationItemKey.HOME to NavigationItem(NavigationItemKey.HOME.name, "Home", "home"),
    NavigationItemKey.DRAWING_BOARD to NavigationItem(NavigationItemKey.DRAWING_BOARD.name, "Drawing Board", "drawing-board"),
    NavigationItemKey.EXPLORE to NavigationItem(NavigationItemKey.EXPLORE.name, "Explore", "explore")
)