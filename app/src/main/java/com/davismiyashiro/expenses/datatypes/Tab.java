package com.davismiyashiro.expenses.datatypes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.UUID;

/**
 * Object that encapsulates data for the Tab
 */
public class Tab implements Parcelable {

    private String groupId;
    private String groupName;

    public Tab(String tabName) {
        groupId = UUID.randomUUID().toString().replaceAll("-", "");
        groupName = tabName;
    }

    public Tab(String id, String tabName) {
        groupId = id;
        groupName = tabName;
    }

    //used to inflate the POJO once it has reached its destination activity
    protected Tab(Parcel in) {
        groupId = in.readString();
        groupName = in.readString();
    }

    public static final Creator<Tab> CREATOR = new Creator<Tab>() {
        @Override
        public Tab createFromParcel(Parcel in) {
            return new Tab(in);
        }

        @Override
        public Tab[] newArray(int size) {
            return new Tab[size];
        }
    };

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupId);
        dest.writeString(groupName);
    }

    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof Tab))
            return false;

        Tab tab = (Tab) obj;
        return tab.groupId.equals(groupId) && tab.groupName.equals(groupName);
    }

    @Override
    public int hashCode () {
        Object [] objs = {groupId, groupName};
        return Arrays.hashCode(objs);
    }
}
