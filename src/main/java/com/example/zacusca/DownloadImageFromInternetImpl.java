package com.example.zacusca;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

public class DownloadImageFromInternetImpl extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    public DownloadImageFromInternetImpl(ImageView imageView) {
        this.imageView=imageView;
        //Toast.makeText(imageView.getContext(), "Please wait, it may take a few minute...",Toast.LENGTH_SHORT).show();
    }
    protected Bitmap doInBackground(String... urls) {
        String imageURL=urls[0];
        Bitmap bimage=null;
        try {
            InputStream in=new java.net.URL(imageURL).openStream();
            bimage=BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error Message", e.getMessage());
            String eroare=e.getMessage();
            Toast.makeText(imageView.getContext(), "error",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return bimage;
    }
    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }

}