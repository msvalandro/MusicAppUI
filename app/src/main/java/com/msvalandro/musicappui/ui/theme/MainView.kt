package com.msvalandro.musicappui.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.primarySurface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.msvalandro.musicappui.MainViewModel
import com.msvalandro.musicappui.R
import com.msvalandro.musicappui.Screen
import com.msvalandro.musicappui.screensInBottom
import com.msvalandro.musicappui.screensInDrawer
import com.msvalandro.musicappui.ui.theme.BrowseScreen
import com.msvalandro.musicappui.ui.theme.HomeView
import com.msvalandro.musicappui.ui.theme.LibraryScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainView() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val isSheetFullScreen by remember {
        mutableStateOf(false)
    }
    val modifier = if (isSheetFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val roundedCornerRadius = if (isSheetFullScreen) 0.dp else 12.dp

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

    val bottomBar: @Composable () -> Unit = {
        if (currentScreen is Screen.DrawerScreen || currentScreen == Screen.BottomScreen.Home) {
            BottomNavigation(modifier = Modifier.wrapContentSize()) {
                screensInBottom.forEach {
                    val isSelected = currentRoute == it.bottomRoute
                    val tint = if (isSelected) Color.White else Color.Black

                    BottomNavigationItem(
                        selected = currentRoute == it.bottomRoute,
                        onClick = { controller.navigate(it.bottomRoute) },
                        icon = {
                            Icon(
                                painter = painterResource(id = it.icon),
                                contentDescription = it.bottomTitle,
                                tint = tint
                            )
                        },
                        label = { Text(text = it.bottomTitle, color = tint) },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Black
                    )
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = roundedCornerRadius, topEnd = roundedCornerRadius),
        sheetContent = {
            MoreBottomSheet(modifier = modifier)
        }
    ) {
        Scaffold(
            bottomBar = bottomBar,
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
fun MoreBottomSheet(modifier: Modifier) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .background(MaterialTheme.colors.primarySurface)) {
        Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = modifier.padding(16.dp)) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = "Settings"
                )
                Text(text = "Settings", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun Navigation(navController: NavController, viewModel: MainViewModel, padding: PaddingValues) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.DrawerScreen.Account.route,
        modifier = Modifier.padding(padding)) {

        composable(Screen.BottomScreen.Home.bottomRoute) {
            HomeView()
        }

        composable(Screen.BottomScreen.Browse.bottomRoute) {
            BrowseScreen()
        }

        composable(Screen.BottomScreen.Library.bottomRoute) {
            LibraryScreen()
        }

        composable(Screen.DrawerScreen.Account.route) {
            AccountView()
        }

        composable(Screen.DrawerScreen.Subscription.route) {
            SubscriptionView()
        }
    }
}
