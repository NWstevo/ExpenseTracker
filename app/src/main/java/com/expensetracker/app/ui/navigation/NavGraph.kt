package com.expensetracker.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.expensetracker.app.ui.LocalCurrency
import com.expensetracker.app.ui.components.CurrencySelector
import com.expensetracker.app.ui.screens.AddExpenseScreen
import com.expensetracker.app.ui.screens.BudgetSettingsScreen
import com.expensetracker.app.ui.screens.DashboardScreen
import com.expensetracker.app.ui.screens.ExpenseListScreen
import com.expensetracker.app.viewmodel.ExpenseViewModel

private sealed class Destination(val route: String, val label: String) {
    object Dashboard : Destination("dashboard", "Dashboard")
    object History : Destination("history", "History")
    object Budget : Destination("budget", "Budget")
    object AddExpense : Destination("add_expense", "Add Expense")
}

private val bottomBarDestinations = listOf(Destination.Dashboard, Destination.History, Destination.Budget)

@Composable
fun ExpenseTrackerNavGraph(viewModel: ExpenseViewModel) {
    val navController = rememberNavController()
    val currency by viewModel.currency.collectAsState()

    CompositionLocalProvider(LocalCurrency provides currency) {
        Scaffold(
            topBar = {
                Surface {
                    CurrencySelector(
                        currency = currency,
                        onSelect = { viewModel.setCurrency(it) }
                    )
                }
            },
            bottomBar = {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry?.destination

                NavigationBar {
                    bottomBarDestinations.forEach { destination ->
                        val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = when (destination) {
                                        Destination.Dashboard -> Icons.Filled.Home
                                        Destination.History -> Icons.Filled.List
                                        Destination.Budget -> Icons.Filled.AccountBalanceWallet
                                        else -> Icons.Filled.Home
                                    },
                                    contentDescription = destination.label
                                )
                            },
                            label = { Text(destination.label) }
                        )
                    }
                }
            },
            floatingActionButton = {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route
                if (currentRoute != Destination.AddExpense.route) {
                    FloatingActionButton(onClick = { navController.navigate(Destination.AddExpense.route) }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add expense")
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Destination.Dashboard.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Destination.Dashboard.route) { DashboardScreen(viewModel) }
                composable(Destination.History.route) { ExpenseListScreen(viewModel) }
                composable(Destination.Budget.route) { BudgetSettingsScreen(viewModel) }
                composable(Destination.AddExpense.route) {
                    AddExpenseScreen(viewModel, onSaved = { navController.popBackStack() })
                }
            }
        }
    }
}
