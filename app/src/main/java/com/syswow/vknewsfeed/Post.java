package com.syswow.vknewsfeed;

import android.text.format.DateFormat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;

public class Post {
    private int ID, authorID, ownerID;
    private long longDate;
    private String text, date, author, avatar50url;
    JSONArray profiles;

    Post(JSONArray profiles, int ID, int authorID, int ownerID, long date, String text) {
        this.ID = ID;
        this.authorID = authorID;
        this.ownerID = ownerID;
        this.longDate = date;
        this.text = text;
        this.profiles = profiles;

        authorInformation();

        Log.d("VKWALL", text);
    }

    //Find author of post
    private void authorInformation() {
        try {
            for (int profilesCount = 0; profilesCount < profiles.length(); profilesCount++) {
                if (authorID == profiles.getJSONObject(profilesCount).getInt("id")) {
                    author = profiles.getJSONObject(profilesCount).getString("first_name") + " " +
                            profiles.getJSONObject(profilesCount).getString("last_name");
                    avatar50url = profiles.getJSONObject(profilesCount).getString("photo_50");
                }
            }
        }   catch (JSONException e) {
            Log.d("VKWALL", e.toString());
            e.printStackTrace();
        }

    }

    public String getAvatar50url() {
        return avatar50url;
    }

    public int getID() {
        return ID;
    }

    public int getAuthorID() {
        return authorID;
    }

    public String getAuthor() {
        return author;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public String getDate() {
        date = DateFormat
                .format("dd.MM.yy kk:mm",
                        new Date(longDate * 1000)
                ).toString();

        return date;
    }

    public String getText() {
        return text;
    }
}
