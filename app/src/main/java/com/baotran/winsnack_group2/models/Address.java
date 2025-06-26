package com.baotran.winsnack_group2.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    private String name;
    private String details;
    private boolean selected;

    public Address(String name, String details, boolean selected) {
        this.name = name;
        this.details = details;
        this.selected = selected;
    }

    protected Address(Parcel in) {
        name = in.readString();
        details = in.readString();
        selected = in.readByte() != 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(details);
        dest.writeByte((byte) (selected ? 1 : 0));
    }

    @Override
    public String toString() {
        return name + " | " + details;
    }
}
