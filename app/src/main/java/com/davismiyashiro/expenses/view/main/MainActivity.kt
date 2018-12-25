package com.davismiyashiro.expenses.view.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View

import com.davismiyashiro.expenses.BuildConfig
import com.davismiyashiro.expenses.R
import com.davismiyashiro.expenses.view.managetabs.ChooseTabsActivity
import com.davismiyashiro.expenses.view.BaseCompatActivity

import timber.log.Timber

class MainActivity : BaseCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val handler = Handler()

        handler.postDelayed({ this.startApp() }, FINISH_TIMER.toLong())
    }

    fun startApp() {
        val intent = Intent(this, ChooseTabsActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onClick(v: View) {
        startApp()
    }

    companion object {

        val FINISH_TIMER = 1 * 1000
    }
}
