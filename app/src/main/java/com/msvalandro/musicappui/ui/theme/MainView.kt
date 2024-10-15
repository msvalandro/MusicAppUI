package com.msvalandro.musicappui.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.msvalandro.musicappui.MainViewModel
import com.msvalandro.musicappui.Screen
import com.msvalandro.musicappui.screensInDrawer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val viewModel: MainViewModel = viewModel()

    val controller = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val dialogOpen = remember {
        mutableStateOf(false)
    }

    val currentScreen = remember {
        viewModel.currentScreen.value
    }

    val title = remember {
        mutableStateOf(currentScreen.title)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title.value) },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Menu")
                    }
                }
            )
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(screensInDrawer) { item ->
                    DrawerItem(selected = currentRoute == item.drawerRoute, item = item) {
                        scope.launch { scaffoldState.drawerState.close() }

                        if (item.drawerRoute == Screen.DrawerScreen.AddAccount.route) {
                            dialogOpen.value = true
                        } else {
                            title.value = item.drawerTitle
                            controller.navigate(item.drawerRoute)
                        }
                    }
                }
            }
        }
    ) {
        Navigation(navController = controller, viewModel = viewModel, padding = it)
        AccountDialog(dialogOpen = dialogOpen)
    }
}

@Composable
fun DrawerItem(selected: Boolean, item: Screen.DrawerScreen, onDrawerItemClicked: () -> Unit) {
    val background = if (selected) Color.DarkGray else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(background)
            .clickable {
                onDrawerItemClicked()
            }
    ) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.drawerTitle,
            modifier = Modifier.padding(end = 8.dp, top = 4.dp)
        )
        Text(text = item.drawerTitle, style = MaterialTheme.typography.h5)
    }
}

@Composable
fun Navigation(navController: NavController, viewModel: MainViewModel, padding: PaddingValues) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.DrawerScreen.Account.route,
        modifier = Modifier.padding(padding)) {

        composable(Screen.DrawerScreen.Account.route) {
            AccountView()
        }

        composable(Screen.DrawerScreen.Subscription.route) {
            SubscriptionView()
        }
    }
}
