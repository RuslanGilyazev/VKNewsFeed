package com.syswow.vknewsfeed;

import android.text.format.DateFormat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;

public class Post {
    private int ID, authorID, ownerID;
    private long longDate;
    private String text, date, author, avatar50url, avatar100url;
    JSONArray profiles, groups;

    Post(JSONArray profiles, JSONArray groups, int ID, int authorID, long date, String text) {
        this.ID = ID;
        this.authorID = authorID;
        this.longDate = date;
        this.text = text;
        this.profiles = profiles;
        this.groups = groups;

        if(authorID > 0) {
            authorInformation(); //Post from friend
        }
        else {
            groupInformation(); //Post from group
        }

        Log.d("VKWALL", text);
    }


    private void groupInformation() {
        authorID = authorID * -1;
        try {
            for (int profilesCount = 0; profilesCount < profiles.length(); profilesCount++) {
                if (authorID == groups.getJSONObject(profilesCount).getInt("id")) {
                    author = groups.getJSONObject(profilesCount).getString("name");
                    avatar50url = groups.getJSONObject(profilesCount).getString("photo_50");
                    avatar100url = groups.getJSONObject(profilesCount).getString("photo_100");
                }
            }
        }   catch (JSONException e) {
            Log.d("VKWALL", e.toString());
            e.printStackTrace();
        }
    }

    private void authorInformation() {
        try {
            for (int profilesCount = 0; profilesCount < profiles.length(); profilesCount++) {
                if (authorID == profiles.getJSONObject(profilesCount).getInt("id")) {
                    author = profiles.getJSONObject(profilesCount).getString("first_name") + " " +
                            profiles.getJSONObject(profilesCount).getString("last_name");
                    avatar50url = profiles.getJSONObject(profilesCount).getString("photo_50");
                    avatar100url = profiles.getJSONObject(profilesCount).getString("photo_100");
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

    public String getAvatar100url() {
        return avatar100url;
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
