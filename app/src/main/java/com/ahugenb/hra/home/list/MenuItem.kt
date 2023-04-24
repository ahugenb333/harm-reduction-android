package com.ahugenb.hra.home.list

data class MenuItem(
    val id: Int,
    val text: String,
    val showDivider: Boolean = true,
    val quickAction: Boolean = false,
    val exportButton: Boolean = false
)