package org.thewheatfield.etanownative;

// Copyright 2012 Square, Inc.
// from https://gist.github.com/JakeWharton/2856179

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class AspectRatioImageView extends ImageView {

public AspectRatioImageView(Context context)
{
    super(context);
}

public AspectRatioImageView(Context context, AttributeSet attrs)
{
    super(context, attrs);
}

public AspectRatioImageView(Context context, AttributeSet attrs,
        int defStyle)
{
    super(context, attrs, defStyle);
}

@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
{
    Drawable drawable = getDrawable();
    if (drawable != null)
    {
        int width =  MeasureSpec.getSize(widthMeasureSpec);
        int diw = drawable.getIntrinsicWidth();
        if (diw > 0)
        {
            int height = width * drawable.getIntrinsicHeight() / diw;
            setMeasuredDimension(width, height);
        }
        else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    else
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
}
private org.thewheatfield.etanownative.AspectRatioImageView imageView;
public void loadImage2(String imgUrl) {
    // HOLD A REFERENCE TO THE ADAPTER
	this.imageView = this;
    if (imgUrl != null && !imgUrl.equals("")) {
        new ImageLoadTask2().execute(imgUrl);
    }
}	    
// ASYNC TASK TO AVOID CHOKING UP UI THREAD
private class ImageLoadTask2 extends AsyncTask<String, String, Bitmap> {

    @Override
    protected void onPreExecute() {
        Log.i("ImageLoadTask", "Loading image...");
    }

    protected Bitmap doInBackground(String... param) {
        Log.i("ImageLoadTask", "Attempting to load image URL: " + param[0]);
        try {
            Bitmap b = getBitmapFromURL(param[0]);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onProgressUpdate(String... progress) {
        // NO OP
    }

    private Bitmap image;
    protected void onPostExecute(Bitmap ret) {
        if (ret != null) {
            Log.i("ImageLoadTask", "Successfully loaded " + image + " image");
            image = ret;
            if (imageView != null) {
                // WHEN IMAGE IS LOADED NOTIFY THE ADAPTER
            	imageView.setImageBitmap(ret);
            	imageView.notify();
            }
        } else {
            Log.e("ImageLoadTask", "Failed to load " + image + " image");
        }
    }
    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }	 
}	    

}