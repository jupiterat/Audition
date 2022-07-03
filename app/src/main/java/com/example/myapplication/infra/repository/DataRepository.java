package com.example.myapplication.infra.repository;

import android.content.Context;

import com.example.myapplication.infra.prefs.DataPreference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import io.reactivex.Observable;

public class DataRepository {
    DataPreference preference;

    public DataRepository(Context context) {
        preference = new DataPreference(context);
    }

    public void setMentionData(String data) {
        preference.setMentionData(data);
    }

    public String getMentionData() {
        return preference.getMentionData();
    }

    public String getLinkData() {
        return preference.getLinkData();
    }

    public void setLinkData(String data) {
        preference.setLinkData(data);
    }

    public Observable<String> getHtml(String url) {
        return Observable.create(emitter -> {
            try {
                String result = makeRequest(url);
                if (result != null) {
                    emitter.onNext(result);
                } else {
                    emitter.onNext("");
                }
                emitter.onComplete();
            } catch (Exception exception) {
                emitter.onError(exception);
            }
        });
    }

    private String makeRequest(String original) {
        URL url;
        try {
            url = new URL(original);
            HttpURLConnection urlConnection;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder html = new StringBuilder();
                    String tmp;
                    while ((tmp = br.readLine()) != null) {
                        html.append(" ").append(tmp);
                    }
                    br.close();
                    html = new StringBuilder(html.toString().replaceAll("\\s+", " "));

                    return html.toString();
                } catch (UnknownHostException e) {
                    System.out.println(" Exception: " + e.getMessage());
                    return null;
                }
            } catch (Exception e) {
                System.out.println(" IOException: " + e.getMessage());
                return null;
            }
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException: " + e.getMessage());
            return null;
        }
    }
}
