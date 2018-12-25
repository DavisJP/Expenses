package com.davismiyashiro.expenses.view.managetabs

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.davismiyashiro.expenses.Injection
import com.davismiyashiro.expenses.R
import com.davismiyashiro.expenses.view.addtab.AddTabActivity
import com.davismiyashiro.expenses.view.BaseCompatActivity
import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.view.opentab.OpenTabActivity

import java.util.ArrayList

class ChooseTabsActivity : BaseCompatActivity(), ChooseTabsInterfaces.View {

    private lateinit var fabAddTab: FloatingActionButton
    private lateinit var mTabsRecyclerView: RecyclerView

    private lateinit var mTabsView: LinearLayout
    private lateinit var mNoTabsView: View
    private lateinit var mNoTabsIcon: ImageView
    private lateinit var mNoTabsText: TextView

    private lateinit var mTabsAdapter: TabsAdapter
    private var mPresenter: ChooseTabsInterfaces.UserActionsListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.tabs)
        super.onCreate(savedInstanceState)

        mTabsRecyclerView = findViewById(R.id.tabs_recycler_view)
        mTabsRecyclerView.layoutManager = GridLayoutManager(this, 2)
        mTabsAdapter = TabsAdapter(ArrayList(), this)
        mTabsRecyclerView.adapter = mTabsAdapter
        mTabsView = findViewById(R.id.tabs_root)

        mNoTabsView = findViewById(R.id.tabs_root_no_data)
        mNoTabsIcon = findViewById(R.id.tabs_no_data_icon)
        mNoTabsText = findViewById(R.id.tabs_no_data_text_view)

        fabAddTab = findViewById(R.id.fab_manage_tabs)
        fabAddTab.setOnClickListener { mPresenter?.addTab() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()

        mPresenter = ChooseTabsPresenterImpl(this, Injection.provideTabsRepository(applicationContext))

        mPresenter?.loadTabs(false)
    }

    override fun showAddTab() {
        startActivity(AddTabActivity.newIntent(this, null))
    }

    override fun setProgressIndicator(active: Boolean) {}

    override fun showTabs(tabs: MutableList<Tab>) {
        mTabsAdapter.replaceData(tabs)

        mTabsView.visibility = View.VISIBLE
        mNoTabsView.visibility = View.GONE
    }

    override fun showNoTabs() {
        loadShowNoTabsView(getString(R.string.tabs_no_data_text), R.drawable.ic_launcher)
    }

    private fun loadShowNoTabsView(message: String, icon: Int) {
        mTabsView.visibility = View.GONE
        mNoTabsView.visibility = View.VISIBLE

        mNoTabsText.text = message
        mNoTabsIcon.setImageDrawable(ContextCompat.getDrawable(this, icon))
    }

    override fun showTabDetailUi(tab: Tab) {
        startActivity(OpenTabActivity.newInstance(this, tab))
    }

    /**
     * Receives tabs and bind them to the ViewHolder
     */
    private inner class TabsAdapter(tabs: MutableList<Tab>, private val mContext: Context) : RecyclerView.Adapter<TabsAdapter.TabRowHolder>() {

        private var mTabs = mutableListOf<Tab>()

        init {
            setList(tabs)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabRowHolder {
            val layoutInflater = LayoutInflater.from(applicationContext)
            val view = layoutInflater
                    .inflate(R.layout.list_item_tab, parent,
                            false)

            return TabRowHolder(view)
        }

        override fun onBindViewHolder(rowHolder: TabRowHolder, position: Int) {
            val tab = mTabs[position]
            rowHolder.bindTab(tab)
        }

        fun replaceData(tabs: MutableList<Tab>) {
            setList(tabs)
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return mTabs.size
        }

        private fun setList(tabs: MutableList<Tab>) {
            mTabs = tabs
        }

        fun getItem(position: Int): Tab {
            return mTabs[position]
        }

        fun removeItemAtPosition(position: Int) {
            mTabs.removeAt(position)
            notifyItemRemoved(position)
        }

        inner class TabRowHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var mTabTitleTextView: TextView = itemView.findViewById(R.id.list_item_tab_title)

            init {
                itemView.setOnClickListener {
                    val position = adapterPosition
                    val (groupId) = getItem(position)

                    mPresenter?.openTab(groupId)
                }

                itemView.setOnLongClickListener { v ->
                    val alertDialogBuilder = AlertDialog.Builder(mContext)
                    alertDialogBuilder.setTitle("Delete Tab")
                    alertDialogBuilder.setMessage("Are you sure you want to delete this Tab ?")

                    alertDialogBuilder.setPositiveButton("YES") { arg0, _ ->
                        val position = adapterPosition

                        mPresenter?.removeTab(getItem(position).groupId)
                        removeItemAtPosition(position)
                        arg0.dismiss()
                    }

                    alertDialogBuilder.setNegativeButton("NO") { dialog, _ -> dialog.cancel() }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()

                    true
                }
            }

            fun bindTab(tab: Tab) {
                mTabTitleTextView.text = tab.groupName
            }
        }
    }
}
