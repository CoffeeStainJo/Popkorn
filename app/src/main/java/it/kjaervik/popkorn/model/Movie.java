package it.kjaervik.popkorn.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import it.kjaervik.popkorn.util.TheMovieDB;

public class Movie implements Parcelable {

    private String Id;
    private String title;
    private String posterImageUri; // /inVq3FRqcYIRl2la8iZikYYxFNR.jpg"
    private String synopsis;
    private double userRating;
    private String releaseDate;

    private Movie(Parcel in) {
        Id = in.readString();
        title = in.readString();
        posterImageUri = in.readString();
        synopsis = in.readString();
        userRating = in.readDouble();
        releaseDate = in.readString();
    }

    // Constructor to convert a JSON object into a Java class instance
    // Decodes movie json into movie model object
    public Movie(JSONObject jsonMovie) {

        try{

            this.Id = jsonMovie.getString(TheMovieDB.MOVIE_ID);
            this.title = jsonMovie.getString(TheMovieDB.MOVIE_TITLE);
            this.posterImageUri = jsonMovie.getString(TheMovieDB.MOVIE_POSTER_PATH).replaceAll("/", "");
            this.synopsis = jsonMovie.getString(TheMovieDB.MOVIE_SYNOPSIS);
            this.userRating = Double.parseDouble(jsonMovie.getString(TheMovieDB.MOVIE_USER_RATING));
            this.releaseDate = (jsonMovie.getString(TheMovieDB.MOVIE_RELEASE_DATE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<Movie> getMoviesFromJsonArray(JSONArray jsonObjects) {

        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < jsonObjects.length(); i++) {

            try {
                movies.add(new Movie(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movies;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPosterImageUri() {
        return posterImageUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(title);
        dest.writeString(posterImageUri);
        dest.writeString(synopsis);
        dest.writeDouble(userRating);
        dest.writeString(releaseDate);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "Id='" + Id + '\'' +
                ", title='" + title + '\'' +
                ", posterImageUri='" + posterImageUri + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", userRating=" + userRating +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterImageUri(String posterImageUri) {
        this.posterImageUri = posterImageUri;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
