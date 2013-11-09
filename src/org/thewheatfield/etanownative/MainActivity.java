package org.thewheatfield.etanownative;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.*;
import org.json.*;


public class MainActivity extends Activity {

	Button btnImageLow;
	Button btnImageHigh;
	Button btnFacebook;
	TextView txtLoading;

	ArrayList<ImageThumbnailSingleList > imgItems = new ArrayList<ImageThumbnailSingleList >();
	ArrayList<ImageThumbnailSingleList > imgItemsFeature = new ArrayList<ImageThumbnailSingleList >();
	ImageThumbnailAdapterSingleList imgAdapter;

	
	Boolean imageLow = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_single_list);
		LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
    	ListView list = (ListView) findViewById(R.id.list);
    	View header = mInflater.inflate(R.layout.header, null);
    	list.addHeaderView(header);
    	list.addFooterView(mInflater.inflate(R.layout.footer, null));
		// 
		loadSettings();
		loadHeader(header);
		loadFeed();
	}
	private void loadHeader(View v){
		txtLoading = (TextView) v.findViewById(R.id.txtLoading);
		btnImageLow = (Button) v.findViewById(R.id.btnImageLow);
		btnImageHigh = (Button) v.findViewById(R.id.btnImageHigh);
		
		toggleButtonVisibility();
		btnImageLow.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				buttonClicked(true);
			}
		});
		btnImageHigh.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				buttonClicked(false);
			}
		});
		
		btnFacebook = (Button) v.findViewById(R.id.btnFacebook);
		btnFacebook.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
	        	Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(getText(R.string.facebook_url).toString()) );
	    	    startActivity( browse );                	
				
			}
		});
	
		TextView tv;
		tv = (TextView) v.findViewById(R.id.phoneValue);
		tv.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
            	Intent browse = new Intent( Intent.ACTION_DIAL, Uri.parse("tel:" + getText(R.string.phone_value).toString()) );
        	    startActivity( browse );                					
			}
		});
	}

	private void loadSettings(){
	    SharedPreferences settings = getPreferences(MODE_PRIVATE);
	    imageLow = settings.getBoolean("imageLow", true);
	}
	private void saveSettings(){
	    SharedPreferences settings = getPreferences(MODE_PRIVATE);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean("imageLow", imageLow);
	    editor.commit();
	}
	
	private void toggleButtonVisibility(){
		btnImageLow.setVisibility(imageLow ? View.GONE : View.VISIBLE);
		btnImageHigh.setVisibility(imageLow ? View.VISIBLE : View.GONE);
		txtLoading.setVisibility(View.VISIBLE);
	}
	private void buttonClicked(boolean imageLow){
		this.imageLow = imageLow;
		saveSettings();
		toggleButtonVisibility();
		loadFeed();
	}
	


    private String URL = "http://pipes.yahoo.com/pipes/pipe.run?_id=0b6c19ca3aeae9ecbbd78d5492fcd77c&_render=json";
	private void loadFeed(){
		txtLoading.setText(getResources().getString(R.string.loading_text));
		txtLoading.setVisibility(View.VISIBLE);
    	ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(null);
        imgItems.clear();

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(URL, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {

		    	try {
					JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
					JSONArray items = ((JSONObject)((JSONObject) object.get("value")).getJSONArray("items").get(0)).getJSONArray("entries");
					for(int i = 0; i < items.length(); i++){
						JSONObject item = (JSONObject) items.get(i);
						String link = item.getString("alternate");
						String title = item.getString("title");						 
						String contents = item.getString("content");
						String status = "";
						if(title.indexOf("-") != -1) 
							status = title.substring(0, title.indexOf("-"));
						String image = "";
						//Log.d("test - contents", contents);
						String pattern = "<img.+?src=[\"'](.+?)[\"']";
						Pattern r = Pattern.compile(pattern, Pattern.MULTILINE);
						Matcher m = r.matcher(contents);
						if(m.find()){
							image = m.group(1);
						}
						Log.d("test - image", image);//
						if(!imageLow)
							image = image.replace("_s.jpg", "_n.jpg");
						Log.d("test - image", image);
						contents = contents.replaceAll("<img[^>]+>", "");
						
						ImageThumbnailSingleList obj = new ImageThumbnailSingleList(Util.posted_time(item.getString("published")), image, status, contents, link);
						//if(i == 0)
						{
							//imgFeatureItems.add(obj);
						}
						//else
						{
							imgItems.add(obj);
						}
						//layout.addView(convertView);
					}
					// 
					txtLoading.setVisibility(View.GONE);
			    	ListView list = (ListView) findViewById(R.id.list);


			        imgAdapter = new ImageThumbnailAdapterSingleList(MainActivity.this, MainActivity.this.imgItems);
			        list.setAdapter(imgAdapter);


			        //			        for (Object imgt : MainActivityDoubleList.this.itemsToLoad.keySet()) {
//			        	((AspectRatioImageView) imgt).loadImage2(MainActivityDoubleList.this.itemsToLoad.get(imgt));
//			        }

//			        for (ImageThumbnail imgt : MainActivityDoubleList.this.imgFeatureItems) {
//		                imgt.loadImage(imgFeatureAdapter);
//			        }
			        for (ImageThumbnailSingleList imgt : MainActivity.this.imgItems) {
		                imgt.loadImage(imgAdapter);
			        }		
					
				} catch (JSONException e) {
					txtLoading.setText("ERROR: "  + e.getMessage());
					e.printStackTrace();
				}

		    }
		});		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
