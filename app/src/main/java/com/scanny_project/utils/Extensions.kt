package com.scanny_project.utils

import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.setupNavigation(actions: Map<Int, () -> Unit>) {
        setOnItemSelectedListener { item ->
            actions[item.itemId]?.invoke() ?: return@setOnItemSelectedListener false
            true
        }
}

