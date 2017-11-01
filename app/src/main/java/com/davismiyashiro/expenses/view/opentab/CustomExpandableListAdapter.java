package com.davismiyashiro.expenses.view.opentab;

/**
 * Class that extends BaseExpandableListAdapter and handles expanding and colliding views
 */

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.davismiyashiro.expenses.R;
import com.davismiyashiro.expenses.datatypes.ReceiptItem;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private ArrayMap<String, List<ReceiptItem>> expandableListDetail;

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       ArrayMap<String, List<ReceiptItem>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ReceiptItem item = (ReceiptItem) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(item.getExpenseDesc());

        TextView expandedListValue = (TextView) convertView
                .findViewById(R.id.expandedListItemValue);
        expandedListValue.setText(String.valueOf(item.getValue()));

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    public void replaceData (ArrayMap<String, List<ReceiptItem>> mapReceiptListItems) {
        setExpandableLists(mapReceiptListItems);
        notifyDataSetChanged();
    }

    private void setExpandableLists (ArrayMap<String, List<ReceiptItem>> mapReceiptListItems) {
        this.expandableListDetail = mapReceiptListItems;
        this.expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String titleId = (String) getGroup(listPosition);

        List<ReceiptItem> items = expandableListDetail.get(titleId);
        String listTitle = items.get(0).getParticipantName();
        double totalValue = 0;
        for (ReceiptItem item : items) {
            totalValue += item.getValue();
        }

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

        TextView listTotalValue = (TextView) convertView
                .findViewById(R.id.listValue);
        listTotalValue.setTypeface(null, Typeface.BOLD);
        listTotalValue.setText(String.valueOf(totalValue));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}