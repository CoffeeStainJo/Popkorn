package it.kjaervik.popkorn.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

    private String author;
    private String content;
    private String url;

    public Review() {
    }

    public Review(String url) {
        this.url = url;
    }

    public Review(String author, String url) {
        this.author = author;
        this.url = url;
    }

    public Review(String author, String content, String url) {
        this.author = author;
        this.content = content;
        this.url = url;
    }

    protected Review(Parcel in) {
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public String getAuthor() {
        return author;
    }
    public String getContent() {
        return content;
    }
    public String getUrl() {
        return url;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
