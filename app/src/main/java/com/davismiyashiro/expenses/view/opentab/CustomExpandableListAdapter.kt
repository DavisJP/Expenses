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

/**
 * Class that extends BaseExpandableListAdapter and handles expanding and colliding views
 */

import java.util.ArrayList
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

import com.davismiyashiro.expenses.R
import com.davismiyashiro.expenses.datatypes.ReceiptItem

class CustomExpandableListAdapter(
    private val context: Context,
    private var expandableListTitle: List<String>,
    private var expandableListDetail: MutableMap<String, MutableList<ReceiptItem>>
) : BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): ReceiptItem? {
        return this.expandableListDetail[this.expandableListTitle[listPosition]]
                ?.get(expandedListPosition)
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val (_, _, expenseDesc, value) = getChild(listPosition, expandedListPosition) as ReceiptItem
        if (convertView == null) {
            val layoutInflater = this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }
        val expandedListTextView = convertView!!
                .findViewById<View>(R.id.expandedListItem) as TextView
        expandedListTextView.text = expenseDesc

        val expandedListValue = convertView
                .findViewById<View>(R.id.expandedListItemValue) as TextView
        expandedListValue.text = value.toString()

        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.expandableListDetail[this.expandableListTitle[listPosition]]
                ?.size ?: 0
    }

    override fun getGroup(listPosition: Int): Any {
        return this.expandableListTitle[listPosition]
    }

    fun replaceData(mapReceiptListItems: MutableMap<String, MutableList<ReceiptItem>>) {
        setExpandableLists(mapReceiptListItems)
        notifyDataSetChanged()
    }

    private fun setExpandableLists(mapReceiptListItems: MutableMap<String, MutableList<ReceiptItem>>) {
        this.expandableListDetail = mapReceiptListItems
        this.expandableListTitle = ArrayList(expandableListDetail.keys)
    }

    override fun getGroupCount(): Int {
        return this.expandableListTitle.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val titleId = getGroup(listPosition) as String

        val items = expandableListDetail[titleId]
        val listTitle = items?.get(0)?.participantName
        var totalValue = 0.0
        for (item in items.orEmpty()) {
            totalValue += item.value
        }

        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_group, null)
        }
        val listTitleTextView = convertView!!
                .findViewById<View>(R.id.listTitle) as TextView
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle

        val listTotalValue = convertView
                .findViewById<View>(R.id.listValue) as TextView
        listTotalValue.setTypeface(null, Typeface.BOLD)
        listTotalValue.text = totalValue.toString()

        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}