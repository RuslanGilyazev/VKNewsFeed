package com.syswow.vknewsfeed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImagesDownloader extends AsyncTask<String, Void, Bitmap> {

    ImageView Image;

    @Override
    protected Bitmap doInBackground(String... params) {
        publishProgress(new Void[]{}); //or null

        String url = "";
        if( params.length > 0 ){
            url = params[0];
        }

        InputStream input = null;
        try {
            URL urlConn = new URL(url);
            input = urlConn.openStream();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return BitmapFactory.decodeStream(input);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        Image.setImageBitmap(result);
    }

    public void setImageView(ImageView Image) {
        this.Image = Image;
    }
}
