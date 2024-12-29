package com.wongislandd.navigation

import com.wongislandd.nexus.navigation.NavigationItem

enum class NavigationItemKey {
    HOME,
    DRAWING_BOARD,
    EXPLORE,
    PROMPT_GENERATION
}

val PROMPT_ARG = "prompt"

val supportedNavigationItems = mutableMapOf(
    NavigationItemKey.HOME to NavigationItem(NavigationItemKey.HOME.name, "Home", "home"),
    NavigationItemKey.DRAWING_BOARD to NavigationItem(
        NavigationItemKey.DRAWING_BOARD.name,
        "Drawing Board",
        "drawing-board",
        supportedArgs = listOf(PROMPT_ARG)
    ),
    NavigationItemKey.EXPLORE to NavigationItem(
        NavigationItemKey.EXPLORE.name,
        "Explore",
        "explore"
    ),
    NavigationItemKey.PROMPT_GENERATION to NavigationItem(
        NavigationItemKey.PROMPT_GENERATION.name,
        "Prompt Generation",
        "prompt-generation"
    )
)