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
package com.davismiyashiro.expenses.view.opentab

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.NavUtils
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup

import com.davismiyashiro.expenses.R
import com.davismiyashiro.expenses.datatypes.ReceiptItem
import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.view.BaseCompatActivity
import com.davismiyashiro.expenses.view.addexpense.ExpenseActivity
import com.davismiyashiro.expenses.view.addtab.AddTabActivity
import com.davismiyashiro.expenses.view.addparticipant.ParticipantActivity
import com.davismiyashiro.expenses.view.opentab.ReceiptFragment.OnReceiptFragmentInteractionListener
import com.davismiyashiro.expenses.view.sendreceipt.ReceiptActivity

import timber.log.Timber

class OpenTabActivity : BaseCompatActivity(),
        OnReceiptFragmentInteractionListener {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * [FragmentPagerAdapter] derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter

    internal lateinit var partFragment: ParticipantFragment
    internal lateinit var expFragment: ExpenseFragment
    internal lateinit var receiptFragment: ReceiptFragment

    /**
     * The [ViewPager] that will host the section contents.
     */
    private lateinit var mViewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var fabPartFrag: FloatingActionButton
    private lateinit var mTab: Tab

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_open_tab)
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")

        mTab = intent.getParcelableExtra(TAB_REQUEST)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container)
        mViewPager.adapter = mSectionsPagerAdapter

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                Timber.d("onPageSelected:$position")
                when (position) {
                    0 -> fabPartFrag.setImageResource(R.drawable.ic_person_add_white_24dp)
                    1 -> fabPartFrag.setImageResource(R.drawable.ic_note_add_white_24dp)
                    2 -> fabPartFrag.setImageResource(R.drawable.ic_share_white_24dp)

                    else -> fabPartFrag.hide()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        tabLayout = findViewById(R.id.tabs)
        tabLayout.setupWithViewPager(mViewPager)

        fabPartFrag = findViewById(R.id.fab_part_frag)
        fabPartFrag.setOnClickListener { v ->
            val pagerPosition = mViewPager.currentItem
            var addData = Intent()
            when (pagerPosition) {
                0 -> addData = ParticipantActivity.newInstance(baseContext, mTab, null)
                1 -> {
                    addData = ExpenseActivity.newIntent(baseContext, mTab, null)
                    fabPartFrag.hide()
                }
                2 -> addData = ReceiptActivity.newInstance(baseContext, mTab)
                else -> {
                }
            }
            startActivity(addData)
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")

        supportActionBar?.title = mTab.groupName

        fabPartFrag.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_open_tab, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
            R.id.action_settings -> {
                startActivityForResult(AddTabActivity.newIntent(this, mTab), UPDATE_RESULT)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.d("onActivityResult")
        // Check which request we're responding to
        if (requestCode == UPDATE_RESULT) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                // Do something with the contact here (bigger example below)
                mTab = data?.getParcelableExtra(AddTabActivity.EDIT_TAB) as Tab
            }
        }
    }

    override fun onBackPressed() {
        if (mViewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            mViewPager.currentItem = mViewPager.currentItem - 1
        }
    }

    override fun onReceiptFragmentInteraction(receiptItem: ReceiptItem) {}

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val tabTitles = arrayOf("PARTICIPANTS", "EXPENSES", "RECEIPT")
        private var baseId: Long = 0

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val createdFragment = super.instantiateItem(container, position) as Fragment

            when (position) {
                0 -> partFragment = createdFragment as ParticipantFragment
                1 -> expFragment = createdFragment as ExpenseFragment
                else -> receiptFragment = createdFragment as ReceiptFragment
            }

            return createdFragment
        }

        // getItem is called to instantiate the fragment for the given page.
        override fun getItem(position: Int): Fragment {
            Timber.d("getItem.position: $position")
            val fragment = Fragment()

            if (position == 0) {
                partFragment = ParticipantFragment.newInstance(mTab)
                return partFragment
            }

            if (position == 1) {
                expFragment = ExpenseFragment.newInstance(mTab)
                return expFragment
            }

            if (position == 2) {
                receiptFragment = ReceiptFragment.newInstance(mTab)
                return receiptFragment
            }

            return fragment
        }

        override fun getCount(): Int {
            return tabTitles.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            // Generate title based on item position
            return tabTitles[position]
        }

        // this is called when notifyDataSetChanged() is called
        override fun getItemPosition(`object`: Any): Int {
            // refresh all fragments when data set changed
            return PagerAdapter.POSITION_NONE
        }

        override fun getItemId(position: Int): Long {
            // give an ID different from position when position has been changed
            return baseId + position
        }

        /**
         * Notify that the position of a fragment has been changed.
         * Create a new ID for each position to force recreation of the fragment
         * @param n number of items which have been changed
         */
        fun notifyChangeInPosition(n: Int) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += (count + n).toLong()
        }
    }

    companion object {

        val TAB_REQUEST = "com.davismiyashiro.expenses.view.opentab.OpenTabActivity"
        val UPDATE_RESULT = 1

        fun newInstance(context: Context, tab: Tab): Intent {
            val intent = Intent(context, OpenTabActivity::class.java)
            intent.putExtra(TAB_REQUEST, tab)
            return intent
        }
    }
}
