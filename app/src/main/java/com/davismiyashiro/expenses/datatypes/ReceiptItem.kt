package com.davismiyashiro.expenses.datatypes

/**
 *
 * Created by Davis Miyashiro.
 */
data class ReceiptItem(val participantId: String,
                       val participantName: String,
                       val expenseDesc: String,
                       val value: Double)
