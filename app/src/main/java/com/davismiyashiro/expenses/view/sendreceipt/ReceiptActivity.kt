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
package com.davismiyashiro.expenses.view.sendreceipt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ShareCompat
import android.view.View
import android.widget.TextView

import com.davismiyashiro.expenses.Injection
import com.davismiyashiro.expenses.R
import com.davismiyashiro.expenses.view.BaseCompatActivity
import com.davismiyashiro.expenses.datatypes.Tab

class ReceiptActivity : BaseCompatActivity(), ReceiptInterfaces.View {
    private var mTab: Tab? = null
    private var mActionsListener: ReceiptInterfaces.UserActionsListener? = null
    private lateinit var receiptResume: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display)

        mTab = intent.getParcelableExtra(TAB_PARAM)

        receiptResume = findViewById(R.id.multText)
        receiptResume.append("Resume Receipt")

        mActionsListener = ReceiptPresenter(this, Injection.provideTabsRepository(applicationContext))
    }

    fun sendTab(view: View) {

        val mimeType = "text/plain"
        val title = "Share receipt for " + mTab?.groupName

        ShareCompat.IntentBuilder.from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(receiptResume.text.toString())
                .startChooser()
    }

    override fun showReceipt() {
    }

    companion object {

        private val TAB_PARAM = "com.davismiyashiro.expenses.view.receipt.ReceiptActivity"

        fun newInstance(context: Context, tab: Tab): Intent {
            val intent = Intent(context, ReceiptActivity::class.java)
            intent.putExtra(TAB_PARAM, tab)
            return intent
        }
    }
}