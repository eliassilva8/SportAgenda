package com.eliassilva.sportagenda;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Elias on 12/07/2018.
 */
public class Event implements Parcelable {
    public String sports;
    public String date;
    public String time;
    public String local;
    public String participants;

    public Event() {
    }

    public Event(String sports, String date, String time, String local, String participants) {
        this.sports = sports;
        this.date = date;
        this.time = time;
        this.local = local;
        this.participants = participants;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Event(Parcel in) {
        sports = in.readString();
        date = in.readString();
        time = in.readString();
        local = in.readString();
        participants = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sports);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(local);
        dest.writeString(participants);
    }
}
