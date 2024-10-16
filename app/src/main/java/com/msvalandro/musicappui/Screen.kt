package com.msvalandro.musicappui

import androidx.annotation.DrawableRes

sealed class Screen(val title: String, val route: String) {
    sealed class BottomScreen(
        val bottomTitle: String,
        val bottomRoute: String,
        @DrawableRes val icon: Int) : Screen(bottomTitle, bottomRoute) {
            object Home: BottomScreen(
                "Home",
                "home",
                R.drawable.ic_music)
            object Library: BottomScreen(
                "Library",
                "library",
                R.drawable.ic_library)
            object Browse: BottomScreen(
                "Browse",
                "browse",
                R.drawable.ic_browse)
        }

    sealed class DrawerScreen(
        val drawerTitle: String,
        val drawerRoute: String,
        @DrawableRes val icon: Int) : Screen(drawerTitle, drawerRoute) {
            object Account: DrawerScreen(
                "Account",
                "account",
                R.drawable.ic_account)
            object Subscription: DrawerScreen(
                "Subscription",
                "subscribe",
                R.drawable.ic_subscribe)
            object AddAccount: DrawerScreen(
                "Add Account",
                "add_account",
                R.drawable.ic_add_account)
        }
}

val screensInBottom = listOf(
    Screen.BottomScreen.Home,
    Screen.BottomScreen.Browse,
    Screen.BottomScreen.Library
)

val screensInDrawer = listOf(
    Screen.DrawerScreen.Account,
    Screen.DrawerScreen.Subscription,
    Screen.DrawerScreen.AddAccount
)
