package org.thewheatfield.etanownative;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util{
	public static String posted_time(String dateStr){
		// 2013-09-11T06:24:58+01:00
		if(dateStr.charAt(dateStr.length()-3) == ':'){
			dateStr = dateStr.substring(0, dateStr.length()-3) + dateStr.substring(dateStr.length()-2);
		}
		SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
		long diff = 0;
		try {  
		    Date date = format.parse(dateStr);  
		    Date now = new Date();
		    diff = now.getTime() - date.getTime();
		} catch (Exception e) {  
		    e.printStackTrace();  
		}
		diff=diff/1000;
		if(diff<1){
			return "Just now";
		}
		diff=diff/60;
		if(diff<=60){
			return Math.round(diff)+" minutes ago.";
		}
		diff=diff/60;
		if(diff<48){
			return Math.round(diff)+" hours ago.";
		}
		diff=diff/24;
		return Math.round(diff)+" days ago.";
	}
}