package com.syswow.vknewsfeed;

import android.text.format.DateFormat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Post {
    private final String TYPE_PHOTO = "photo";
    private int ID, authorID, ownerID;
    private long longDate;
    private String text, date, author, avatar50url, avatar100url, type, attachmentsPhoto130url,
            attachmentsPhoto604url;
    JSONArray profiles, groups, attachments;
    private boolean postHaveImages = false;

    Post(JSONArray profiles, JSONArray groups, JSONArray attachments,
         int ID, int authorID, long date, String text) {
        this.ID = ID;
        this.authorID = authorID;
        this.longDate = date;
        this.text = text;
        this.profiles = profiles;
        this.groups = groups;
        this.attachments = attachments;

        Log.d("VKNEWSFEED", attachments.toString());

        if(authorID > 0) {
            authorInformation(); //Post from friend
        }
        else {
            groupInformation(); //Post from group
        }

        postHaveAchments();

        Log.d("VKWALL", text);
    }

    private void postHaveAchments() {
        JSONObject photo;
        try {
            if(attachments != null) {
                Log.d("VKNEWSFEED", attachments.toString());
                for (int attachmentsCount = 0; attachmentsCount < attachments.length();
                        attachmentsCount++) {
                    if(attachments.getJSONObject(attachmentsCount).getString("type").equals(TYPE_PHOTO)) {
                        postHaveImages = true;
                        photo = attachments.getJSONObject(attachmentsCount).getJSONObject("photo");
                        attachmentsPhoto130url = photo.getString("photo_130");
                        attachmentsPhoto604url = photo.getString("photo_604");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            postHaveImages = false;
            Log.d("VKNEWSFEED", e.toString());
        }
    }

    public boolean getPostHaveImages() {
        return postHaveImages;
    }

    public String getAttachmentsPhoto130url() {
        return  attachmentsPhoto130url;
    }

    public String getAttachmentsPhoto604url() {
        return  attachmentsPhoto604url;
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
