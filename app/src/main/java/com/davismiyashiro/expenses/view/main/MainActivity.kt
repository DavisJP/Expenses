/*
 * MIT License
 *
 * Copyright (c) 2019 Davis Miyashiro
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

    private fun startApp() {
        val intent = Intent(this, ChooseTabsActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onClick(v: View) {
        startApp()
    }

    companion object {

        const val FINISH_TIMER = 1 * 1000
    }
}
