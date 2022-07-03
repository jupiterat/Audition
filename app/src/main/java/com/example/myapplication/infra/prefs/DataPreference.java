package com.example.myapplication.infra.prefs;

import android.content.Context;

public class DataPreference extends BasePreferences {
    private static final String MENTION_KEY = "mention_key";
    private static final String LINK_KEY = "link_key";

    public DataPreference(Context context) {
        super(context);
    }

    public String getMentionData() {
        return sharedPreferences.getString(MENTION_KEY, null);
    }

    public void setMentionData(String data) {
        putString(MENTION_KEY, data);
    }

    public String getLinkData() {
        return sharedPreferences.getString(LINK_KEY, null);
    }

    public void setLinkData(String data) {
        putString(LINK_KEY, data);
    }
}
