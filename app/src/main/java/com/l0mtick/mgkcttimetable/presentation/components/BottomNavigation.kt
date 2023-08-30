package com.l0mtick.mgkcttimetable.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    )
@Composable
fun BottomNavigation(navController: NavController, currentScreenId: Int) {
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(currentScreenId - 1)
    }
    val navItems = listOf(
        NavigationItem(
            title = "Group",
            selectedIcon = Icons.Filled.List,
            unselectedIcon = Icons.Outlined.List,
        ),
        NavigationItem(
            title = "Teacher",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
        )
    )
    NavigationBar {
        navItems.forEachIndexed() { index, item ->
            NavigationBarItem(
                selected = index == selectedItemIndex,
                onClick = {
                    selectedItemIndex = index
                    when(currentScreenId) {
                        1 -> {
                            if (selectedItemIndex == 1)
                                navController.navigate("teacher")
                            selectedItemIndex -= 1
                        }
                        2 -> {
                            if (selectedItemIndex == 0)
                                navController.popBackStack()
                        }
                    }
                },
                label = {
                    Text(text = item.title)
                },
                icon = {
                    Icon(
                        imageVector = if (selectedItemIndex == index) {
                            item.selectedIcon
                        } else {
                            item.unselectedIcon
                        },
                        contentDescription = item.title
                    )
                })
        }
    }
}