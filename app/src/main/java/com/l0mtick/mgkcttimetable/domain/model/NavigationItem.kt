package com.l0mtick.mgkcttimetable.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val id: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)