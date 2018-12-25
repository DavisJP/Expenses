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