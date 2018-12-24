package com.davismiyashiro.expenses.injection

import com.davismiyashiro.expenses.view.opentab.ExpenseFragment
import com.davismiyashiro.expenses.view.opentab.ParticipantFragment
import com.davismiyashiro.expenses.view.opentab.ReceiptFragment

import javax.inject.Singleton

import dagger.Component

/**
 * @Author Davis Miyashiro
 */

@Singleton
@Component(modules = [ApplicationModule::class, OpenTabModule::class])
interface ApplicationComponent {

    fun inject(fragment: ParticipantFragment)

    fun inject(fragment: ExpenseFragment)

    fun inject(fragment: ReceiptFragment)
}
