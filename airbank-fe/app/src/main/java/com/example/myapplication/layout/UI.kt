package com.example.myapplication.layout


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.navigate.AppNavigation
import com.example.myapplication.screens.BottomNavItem
import com.example.myapplication.workers.NotificationWorker


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun AppMainContent(navController: NavHostController) {
    AppNavigation(navController)
}
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MyUI() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var title by remember {mutableStateOf("")}
    var nowon by remember { mutableStateOf("") }
    val isLoggedin = true
    LaunchedEffect(navController.currentDestination){
        navController.addOnDestinationChangedListener { _, destination, _ ->
            title = BottomNavItem.fromRoute(destination.route.toString()).title
            Log.d("navigateRoute",destination.toString())
            nowon = destination.route.toString()
        }
    }
    Scaffold(
        topBar = {
            if(nowon!="First"){
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.pretendardbold)),
                            fontSize = 20.sp,
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(BottomNavItem.Main.screenRoute){
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) {saveState = true}
                                }
                            }
                        }
                    ){
                    Image(
                        painterResource(id = R.drawable.airbank),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .fillMaxWidth() // Set the width to fill the available space
                            .aspectRatio(1.5f) // Set the desired aspect ratio (width / height)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (navController.currentDestination?.route != BottomNavItem.Notification.screenRoute) {
                            navController.navigate(BottomNavItem.Notification.screenRoute) {
                                navController.graph.startDestinationRoute?.let {
                                    popUpTo(it) { saveState = true }
                                }
                            }
                        } else {
                            navController.navigate(BottomNavItem.Main.screenRoute) {
                                navController.graph.startDestinationRoute?.let {
                                    popUpTo(it) { saveState = true }
                                }
                            }
                        }
                    }) {
                        Box(
                            contentAlignment = Alignment.TopEnd
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = "notification"
                            )

                            if (NotificationWorker.updateAlarm) { // updateAlarm 값 확인
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Color.Red, CircleShape)
                                        .align(Alignment.TopEnd)
                                )
                            }
                        }
                    }
                }
            )
        }},
        bottomBar = {
            if (nowon!="First"){
            NavigationBar(
                containerColor = Color(0xFFF4F4F4),
                content = {
                    val items = listOf(
                        BottomNavItem.Main,
                        BottomNavItem.Wallet,
                        BottomNavItem.Loan,
                        BottomNavItem.Savings,
                        BottomNavItem.MyPage
                    )
                    items.forEach {item ->
                        NavigationBarItem(
                            icon = {Icon(painter = painterResource(id = item.icon), contentDescription = item.title)},
//                            label = { Text(item.title) },
                            selected = currentRoute == item.screenRoute,
                            alwaysShowLabel = false,
                            onClick = {
                                navController.navigate(item.screenRoute){
                                    navController.graph.startDestinationRoute?.let {
                                        popUpTo(it) {saveState = true}
                                    }
                                }
                            },
//                            colors =  NavigationBarItemColors.iconColor
                        )
                    }
                }
            )
        }}
    ) {
        Row(
            modifier = Modifier
                .padding(it)
                .background(Color(0xFFf3f3f3))
            , horizontalArrangement = Arrangement.Center
            , verticalAlignment = Alignment.CenterVertically
        ) {
            AppMainContent(navController = navController)
        }
    }
}





