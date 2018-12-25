package com.davismiyashiro.expenses.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

import com.davismiyashiro.expenses.R

import timber.log.Timber

abstract class BaseCompatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate() event")
        super.onCreate(savedInstanceState)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    public override fun onStart() {
        super.onStart()
        Timber.d("onStart() event")
    }

    public override fun onRestart() {
        super.onRestart()
        Timber.d("onRestart() event")
    }

    public override fun onResume() {
        super.onResume()
        Timber.d("onResume() event")
    }

    public override fun onPause() {
        super.onPause()
        Timber.d("onPause() event")
    }

    public override fun onStop() {
        super.onStop()
        Timber.d("onStop() event")
    }

    public override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy() event")
    }
}
