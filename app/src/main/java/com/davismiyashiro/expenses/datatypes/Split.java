package com.davismiyashiro.expenses.datatypes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * Class that represents each split Expense
 */
public class Split implements Parcelable {


    private String mId;
    private String participantId;
    private String expenseId;
    private int numberParticipants;
    private double valueByParticipant;
    private String mTabId;

    public Split(String participantId, String expenseId, int numberParticipants, double valueByParticipant, String tabId) {
        this.mId = UUID.randomUUID().toString();
        this.participantId = participantId;
        this.expenseId = expenseId;
        this.numberParticipants = numberParticipants;
        this.valueByParticipant = valueByParticipant;
        this.mTabId = tabId;
    }

    public static Split retrieveSplit (String id, String participantId, String expenseId, int numberParticipants, double valueByParticipant, String tabId) {

        Split split = new Split(participantId, expenseId, numberParticipants, valueByParticipant, tabId);
        split.setId(id);
        return split;
    }

    protected Split(Parcel in) {
        mId = in.readString();
        participantId = in.readString();
        expenseId = in.readString();
        numberParticipants = in.readInt();
        valueByParticipant = in.readDouble();
        mTabId = in.readString();
    }

    private void setId(String mId) {
        this.mId = mId;
    }
    public String getId() {
        return mId;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public int getNumberParticipants() {
        return numberParticipants;
    }

    public void setNumberParticipants(int numberParticipants) {
        this.numberParticipants = numberParticipants;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public double getValueByParticipant() {
        return valueByParticipant;
    }

    public void setValueByParticipant(double valueByParticipant) {
        this.valueByParticipant = valueByParticipant;
    }

    public String getTabId() {
        return mTabId;
    }

    public static final Creator<Split> CREATOR = new Creator<Split>() {
        @Override
        public Split createFromParcel(Parcel in) {
            return new Split(in);
        }

        @Override
        public Split[] newArray(int size) {
            return new Split[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(participantId);
        parcel.writeString(expenseId);
        parcel.writeInt(numberParticipants);
        parcel.writeDouble(valueByParticipant);
        parcel.writeString(mTabId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Split split = (Split) o;

        if (numberParticipants != split.numberParticipants) return false;
        if (Double.compare(split.valueByParticipant, valueByParticipant) != 0) return false;
        if (mId != null ? !mId.equals(split.mId) : split.mId != null) return false;
        if (participantId != null ? !participantId.equals(split.participantId) : split.participantId != null)
            return false;
        if (expenseId != null ? !expenseId.equals(split.expenseId) : split.expenseId != null)
            return false;
        return mTabId != null ? mTabId.equals(split.mTabId) : split.mTabId == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (participantId != null ? participantId.hashCode() : 0);
        result = 31 * result + (expenseId != null ? expenseId.hashCode() : 0);
        result = 31 * result + numberParticipants;
        temp = Double.doubleToLongBits(valueByParticipant);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (mTabId != null ? mTabId.hashCode() : 0);
        return result;
    }
}
