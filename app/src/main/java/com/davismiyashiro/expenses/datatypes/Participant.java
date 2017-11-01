package com.davismiyashiro.expenses.datatypes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * Object that encapsulates data for the Participant
 */
public class Participant implements Parcelable {

    private String mId;
    private String name;
    private String email;
    private String number;
    private String tabId;

    public Participant(String name, String email, String number, String tabId) {
        this.mId= UUID.randomUUID().toString().replaceAll("-", "");
        this.name = name;
        this.email = email;
        this.number = number;
        this.tabId = tabId;
    }

    public static Participant retrieveParticipant (String id, String name, String email, String number, String tabId) {

        Participant participant = new Participant(name, email, number, tabId);
        participant.setId(id);
        return participant;
    }

    protected Participant(Parcel in) {
        mId = in.readString();
        name = in.readString();
        email = in.readString();
        number = in.readString();
        tabId = in.readString();
    }

    public static final Creator<Participant> CREATOR = new Creator<Participant>() {
        @Override
        public Participant createFromParcel(Parcel in) {
            return new Participant(in);
        }

        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };

    public String getId() {
        return mId;
    }

    private void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTabId() {
        return tabId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(number);
        dest.writeString(tabId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;

        if (mId != null ? !mId.equals(that.mId) : that.mId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;
        return tabId != null ? tabId.equals(that.tabId) : that.tabId == null;

    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (tabId != null ? tabId.hashCode() : 0);
        return result;
    }
}
