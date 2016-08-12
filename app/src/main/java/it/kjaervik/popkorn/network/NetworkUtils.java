package it.kjaervik.popkorn.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NetworkUtils {
    public static StringBuffer readResponseFromInputStream(InputStream inputStream, BufferedReader bufferedReader) throws IOException {
        StringBuffer buffer = new StringBuffer();

        if (inputStream == null) return null;

        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String inputLine;

        while ((inputLine = bufferedReader.readLine()) != null)
            buffer.append(inputLine).append("\n");

        if (buffer.length() == 0) // no point in parsing
            return null;

        return buffer;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
