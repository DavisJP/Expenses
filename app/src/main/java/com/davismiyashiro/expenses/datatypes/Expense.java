package com.davismiyashiro.expenses.datatypes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * Object that encapsulates data for the Expense
 */
public class Expense implements Parcelable {
    private String mId;
    private String mDescription;
    private double mValue = 0;
    private String tabId;

    private void setId(String id) {
        mId = id;
    }

    public Expense(String description, double value, String tabid) {
        mId = UUID.randomUUID().toString();
        mDescription = description;
        mValue = value;
        tabId = tabid;
    }

    protected Expense(Parcel in) {
        mId = in.readString();
        mDescription = in.readString();
        mValue = in.readDouble();
        tabId = in.readString();
    }

    public static Expense retrieveExpense (String id, String description, double value, String tabId) {

        Expense expense = new Expense(description, value, tabId);
        expense.setId(id);
        return expense;
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

    public void setValue(double value) {
        mValue = value;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getDescription() {

        return mDescription;
    }

    public double getValue() {
        return mValue;
    }

    public String getId() {
        return mId;
    }

    public String getTabId() {
        return tabId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expense expense = (Expense) o;

        if (Double.compare(expense.mValue, mValue) != 0) return false;
        if (mId != null ? !mId.equals(expense.mId) : expense.mId != null) return false;
        if (mDescription != null ? !mDescription.equals(expense.mDescription) : expense.mDescription != null)
            return false;
        return tabId != null ? tabId.equals(expense.tabId) : expense.tabId == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (mDescription != null ? mDescription.hashCode() : 0);
        temp = Double.doubleToLongBits(mValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (tabId != null ? tabId.hashCode() : 0);
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mDescription);
        dest.writeDouble(mValue);

        dest.writeString(tabId);
    }
}
