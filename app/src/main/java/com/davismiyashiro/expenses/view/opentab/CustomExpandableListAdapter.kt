package com.davismiyashiro.expenses.view.opentab

/**
 * Class that extends BaseExpandableListAdapter and handles expanding and colliding views
 */

import java.util.ArrayList
import android.content.Context
import android.graphics.Typeface
import android.support.v4.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

import com.davismiyashiro.expenses.R
import com.davismiyashiro.expenses.datatypes.ReceiptItem

class CustomExpandableListAdapter(private val context: Context, private var expandableListTitle: List<String>,
                                  private var expandableListDetail: ArrayMap<String, MutableList<ReceiptItem>>) : BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): ReceiptItem? {
        return this.expandableListDetail[this.expandableListTitle[listPosition]]
                ?.get(expandedListPosition)
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int,
                              isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
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

    fun replaceData(mapReceiptListItems: ArrayMap<String, MutableList<ReceiptItem>>) {
        setExpandableLists(mapReceiptListItems)
        notifyDataSetChanged()
    }

    private fun setExpandableLists(mapReceiptListItems: ArrayMap<String, MutableList<ReceiptItem>>) {
        this.expandableListDetail = mapReceiptListItems
        this.expandableListTitle = ArrayList(expandableListDetail.keys)
    }

    override fun getGroupCount(): Int {
        return this.expandableListTitle.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(listPosition: Int, isExpanded: Boolean,
                              convertView: View?, parent: ViewGroup): View {
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