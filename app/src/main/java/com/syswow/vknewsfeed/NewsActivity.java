package com.syswow.vknewsfeed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsActivity extends Activity {
    ArrayList<Post> posts = new ArrayList<Post>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        getNews();
    }

    //Request to VK
    public void getNews() {
        VKRequest newsRequest = new VKRequest("newsfeed.get", VKParameters.from("filters", "post",
                "count", 10));
        newsRequest.executeWithListener(new VKRequest.VKRequestListener() {

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                getNewsData(response.json);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }
        });
    }

    public void getNewsData(JSONObject jsonObject) {
        Log.d("VKNEWSFEED", jsonObject.toString());

        JSONObject response;
        JSONArray items;
        JSONArray groups;
        JSONArray profiles;

        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);
        LayoutInflater ltInflater = getLayoutInflater();

        try {
            response = jsonObject.getJSONObject("response");
            items = response.getJSONArray("items");
            profiles = response.getJSONArray("profiles");
            groups = response.getJSONArray("groups");

            //Get all news
            for(int itemsCount = 0; itemsCount < items.length(); itemsCount++ ) {
                Post post  = new Post(
                        profiles,
                        groups,
                        items.getJSONObject(itemsCount).getJSONArray("attachments"),
                        items.getJSONObject(itemsCount).getInt("post_id"),
                        items.getJSONObject(itemsCount).getInt("source_id"),
                        items.getJSONObject(itemsCount).getLong("date"),
                        items.getJSONObject(itemsCount).getString("text"));
                posts.add(post);

                View item = ltInflater.inflate(R.layout.post, linLayout, false);
                TextView postName = (TextView) item.findViewById(R.id.news_name);
                TextView postText = (TextView) item.findViewById(R.id.news_post);
                TextView postData = (TextView) item.findViewById(R.id.news_data);
                ImageView postAvatar = (ImageView) item.findViewById(R.id.news_ava);

                ImagesDownloader avatarsDownloader = new ImagesDownloader();
                avatarsDownloader.setImageView(postAvatar);
                avatarsDownloader.execute(post.getAvatar50url());

                if(post.getPostHaveImages() == true) {
                    ImageView postImage = (ImageView) item.findViewById(R.id.post_image);
                    ImagesDownloader imagesDownloader = new ImagesDownloader();
                    imagesDownloader.setImageView(postImage);
                    imagesDownloader.execute(post.getAttachmentsPhoto604url());
                }

                postName.setText(post.getAuthor());
                postText.setText(post.getText());
                postData.setText(post.getDate());

                item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                linLayout.addView(item);
            }

        } catch (JSONException e) {
            Log.d("VKWALL", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            VKSdk.logout();
            startActivity(new Intent(this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
