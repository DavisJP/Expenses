package com.davismiyashiro.expenses.datatypes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

import java.util.UUID

/**
 * Object that encapsulates data for the Expense
 */
@Parcelize
data class Expense(var description: String, var value: Double, var tabId: String, var id: String) : Parcelable {
    constructor(description: String, value: Double, tabId: String) : this (description, value, tabId, UUID.randomUUID().toString())

    companion object {
        fun retrieveExpense(id: String, description: String, value: Double, tabId: String): Expense {

            val expense = Expense(description, value, tabId)
            expense.id = id
            return expense
        }
    }
}
