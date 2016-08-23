package it.kjaervik.popkorn.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {

    private String key;
    private String name;
    private String type;
    private int size;

    public Trailer() {
    }

    public Trailer(String key) {
        this.key = key;
    }

    public Trailer(String key, String name, String type) {
        this.key = key;
        this.name = name;
        this.type = type;
    }

    public Trailer(String key, String name, String type, int size) {
        this.key = key;
        this.name = name;
        this.type = type;
        this.size = size;
    }

    protected Trailer(Parcel in) {
        key = in.readString();
        name = in.readString();
        type = in.readString();
        size = in.readInt();
    }

    public String getKey() {
        return key;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public int getSize() {
        return size;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setSize(int size) {
        this.size = size;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeInt(size);
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
