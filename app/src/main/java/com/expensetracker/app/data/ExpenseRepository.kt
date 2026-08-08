package com.expensetracker.app.data

/**
 * Thin wrapper around the two DAOs. The ViewModel talks to this, never to
 * Room directly — that keeps the ViewModel testable and means if we ever
 * swap Room for something else, only this file changes.
 */
class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val budgetDao: BudgetDao
) {
    fun getAllExpenses() = expenseDao.getAllExpenses()
    fun getExpensesBetween(start: Long, end: Long) = expenseDao.getExpensesBetween(start, end)
    fun getTotalBetween(start: Long, end: Long) = expenseDao.getTotalBetween(start, end)
    fun getCategoryTotalsBetween(start: Long, end: Long) = expenseDao.getCategoryTotalsBetween(start, end)

    suspend fun addExpense(expense: Expense) = expenseDao.insert(expense)
    suspend fun updateExpense(expense: Expense) = expenseDao.update(expense)
    suspend fun deleteExpense(expense: Expense) = expenseDao.delete(expense)

    fun getBudget() = budgetDao.getBudget()
    suspend fun saveBudget(budget: Budget) = budgetDao.upsert(budget)
}
