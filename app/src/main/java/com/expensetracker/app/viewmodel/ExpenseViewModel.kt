package com.expensetracker.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.data.AppDatabase
import com.expensetracker.app.data.Budget
import com.expensetracker.app.data.CategoryTotal
import com.expensetracker.app.data.Expense
import com.expensetracker.app.data.ExpenseCategory
import com.expensetracker.app.data.ExpenseRepository
import com.expensetracker.app.util.BudgetStatus
import com.expensetracker.app.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * AndroidViewModel (not plain ViewModel) because it needs an Application
 * context to build the Room database and to resolve file paths for photos.
 *
 * Every list here is exposed as a StateFlow that auto-updates the UI
 * whenever the underlying table changes — the UI never has to manually
 * "refresh".
 */
class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExpenseRepository

    init {
        val db = AppDatabase.getInstance(application)
        repository = ExpenseRepository(db.expenseDao(), db.budgetDao())
    }

    private fun <T> Flow<T>.toState(initial: T): StateFlow<T> =
        stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), initial)

    val allExpenses: StateFlow<List<Expense>> =
        repository.getAllExpenses().toState(emptyList())

    val budget: StateFlow<Budget> =
        repository.getBudget().map { it ?: Budget() }.toState(Budget())

    val weeklyTotal: StateFlow<Double> =
        repository.getTotalBetween(DateUtils.startOfWeekMillis(), DateUtils.endOfWeekMillis())
            .toState(0.0)

    val monthlyTotal: StateFlow<Double> =
        repository.getTotalBetween(DateUtils.startOfMonthMillis(), DateUtils.endOfMonthMillis())
            .toState(0.0)

    val weeklyCategoryTotals: StateFlow<List<CategoryTotal>> =
        repository.getCategoryTotalsBetween(DateUtils.startOfWeekMillis(), DateUtils.endOfWeekMillis())
            .toState(emptyList())

    val monthlyCategoryTotals: StateFlow<List<CategoryTotal>> =
        repository.getCategoryTotalsBetween(DateUtils.startOfMonthMillis(), DateUtils.endOfMonthMillis())
            .toState(emptyList())

    fun weeklyStatus(spent: Double, b: Budget) = BudgetStatus(
        spent = spent,
        limit = b.weeklyLimit,
        warningThresholdPercent = b.warningThresholdPercent,
        overdraftAllowance = b.overdraftAllowance
    )

    fun monthlyStatus(spent: Double, b: Budget) = BudgetStatus(
        spent = spent,
        limit = b.monthlyLimit,
        warningThresholdPercent = b.warningThresholdPercent,
        overdraftAllowance = b.overdraftAllowance
    )

    fun addExpense(amount: Double, category: ExpenseCategory, note: String, photoPath: String?) {
        viewModelScope.launch {
            repository.addExpense(
                Expense(
                    amount = amount,
                    category = category,
                    note = note,
                    dateMillis = DateUtils.nowMillis(),
                    photoPath = photoPath
                )
            )
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch { repository.deleteExpense(expense) }
    }

    fun saveBudget(
        weeklyLimit: Double,
        monthlyLimit: Double,
        warningThresholdPercent: Int,
        overdraftAllowance: Double
    ) {
        viewModelScope.launch {
            repository.saveBudget(
                Budget(
                    weeklyLimit = weeklyLimit,
                    monthlyLimit = monthlyLimit,
                    warningThresholdPercent = warningThresholdPercent,
                    overdraftAllowance = overdraftAllowance
                )
            )
        }
    }
}
