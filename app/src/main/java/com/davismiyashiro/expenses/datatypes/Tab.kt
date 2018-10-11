package com.davismiyashiro.expenses.datatypes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 *
 * Created by Davis Miyashiro.
 */
@Parcelize
data class Tab (var groupId: String, var groupName:String) : Parcelable {
    constructor(groupName: String) : this (UUID.randomUUID().toString().replace("-".toRegex(), ""), groupName)
}