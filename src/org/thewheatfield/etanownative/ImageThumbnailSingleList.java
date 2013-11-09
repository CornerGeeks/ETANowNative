package org.thewheatfield.etanownative;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class ImageThumbnailSingleList {
		 
	    private String imgUrl;
	    private String status;
	    private String date;
	    private String details;
	    private String link;
	 
	    private Bitmap image;
	 
	    private ImageThumbnailAdapterSingleList  sta;
	 
	    public ImageThumbnailSingleList (String date, String imgUrl, String status, String details, String link) {
			this.date = date;
			this.status = status;
			this.details = details;
			this.imgUrl = imgUrl;
			this.link = link;
			// TO BE LOADED LATER - OR CAN SET TO A DEFAULT IMAGE
			this.image = null;
	    }
	 
	    public String getImgUrl() {
	        return imgUrl;
	    }
	    public String getDate() {
	        return date;
	    }
	    public String getStatus() {
	        return status;
	    }
	    public String getDetails() {
	        return details;
	    }
	    public String getLink() {
	        return link;
	    }
	 
	    public void setImgUrl(String imgUrl) {
	        this.imgUrl = imgUrl;
	    }
	    public void setLink(String link) {
	        this.link = link;
	    }
	 
		public Bitmap getImage() {
			return image;
	    }
	 
	    public ImageThumbnailAdapterSingleList  getAdapter() {
	        return sta;
	    }
	 
	    public void setAdapter(ImageThumbnailAdapterSingleList  sta) {
	        this.sta = sta;
	    }
	 
	    public void loadImage(ImageThumbnailAdapterSingleList  sta) {
	        // HOLD A REFERENCE TO THE ADAPTER
	        this.sta = sta;
	        if (imgUrl != null && !imgUrl.equals("")) {
	            new ImageLoadTaskSingleList().execute(imgUrl);
	        }
	    }

	 
	    // ASYNC TASK TO AVOID CHOKING UP UI THREAD
	    private class ImageLoadTaskSingleList  extends AsyncTask<String, String, Bitmap> {
	 
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
	 
	        protected void onPostExecute(Bitmap ret) {
	            if (ret != null) {
	                Log.i("ImageLoadTask", "Successfully loaded " + image + " image");
	                image = ret;
	                if (sta != null) {
	                    // WHEN IMAGE IS LOADED NOTIFY THE ADAPTER
	                    sta.notifyDataSetChanged();
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