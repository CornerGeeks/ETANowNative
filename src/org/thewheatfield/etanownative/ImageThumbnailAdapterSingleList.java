package org.thewheatfield.etanownative;

import java.util.List;


import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageThumbnailAdapterSingleList extends BaseAdapter {
	private Context context;
    private LayoutInflater mInflater;
    private List<?> items;
 
    public ImageThumbnailAdapterSingleList(Context context, List<?> items) {
    	this.context = context;
        mInflater = LayoutInflater.from(context);
        this.items = items;
    }

    public int getCount() {
        return items.size();
    }
 
    public ImageThumbnailSingleList getItem(int position) {
        return (ImageThumbnailSingleList) items.get(position);
    }
 
    public long getItemId(int position) {
    	return position;
    }
 
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ImageThumbnailSingleList s = null;
        s = (ImageThumbnailSingleList) items.get(position);
        Log.d("test", s.toString());
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.details = (TextView) convertView.findViewById(R.id.txtDetails);
            holder.status = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.date = (TextView) convertView.findViewById(R.id.txtPostedDate);
            holder.image = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.details.setText(Html.fromHtml(s.getDetails() == null ? "" : s.getDetails()));
        if(s.getStatus() != ""){
        	holder.status.setText(s.getStatus());
            if(s.getStatus().toLowerCase().contains("restock"))
            	holder.status.setBackgroundColor(this.context.getResources().getColor(R.color.restock));
            else
            	holder.status.setBackgroundColor(this.context.getResources().getColor(R.color.instock));
        	holder.status.setVisibility(View.VISIBLE);
        }
        else
        	holder.status.setVisibility(View.GONE);
        
        // holder.status.setAlpha((float) 1);
        holder.date.setText(Html.fromHtml("<a href='"+ s.getLink() +"'>"+s.getDate()+"</a>"));
        holder.date.setMovementMethod(LinkMovementMethod.getInstance());

        if (s.getImage() != null) {
            holder.image.setImageBitmap(s.getImage());
       } else {
            holder.image.setImageResource(R.drawable.ic_launcher);
        }
        holder.image.setVisibility(s.getImgUrl() == "" ? View.GONE : View.VISIBLE);
        return convertView;
    }
 
    class ViewHolder {
        TextView date; 
        TextView status; 
        TextView details; 
        ImageView image;
    } 
}