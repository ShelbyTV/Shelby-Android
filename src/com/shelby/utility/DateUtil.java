package com.shelby.utility;

import android.text.format.DateFormat;

public final class DateUtil {
	
	public static String formatDateForStream(String epochTime, Boolean shorttext){
			String retStr = null;
		    double ti = Math.round(System.currentTimeMillis()/1000) - Double.valueOf(epochTime);
		    
		    if (ti < 60) {
		        retStr= "just now";
		    } else if (ti < 3600) {
		        long diff = Math.round(ti / 60);
				if (diff == 1) { 
					if (shorttext) retStr = ""+diff +" min ago";
					else retStr =  ""+diff+" minute"; 
				} else {
					if (shorttext) retStr = ""+diff+" min ago";
					else retStr =  ""+diff+" minutes"; 
				}
		    } else if (ti < 86400) {
		    	long diff = Math.round(ti/60/60);
				if (diff == 1) { 
					if (shorttext) retStr = ""+diff+" hr ago";
					else retStr = ""+diff+" hour";
				} else {
					if (shorttext) retStr = ""+diff+" hrs ago";
					else retStr = ""+diff+" hours";
				}
		    } else if (ti < 2629743) {
		        long diff = Math.round(ti / 60 / 60 / 24);
				if (diff == 1) { 
					if (shorttext) retStr = ""+diff+" day ago";
					else retStr = ""+diff+" day";
				} else {
					if (shorttext) retStr = ""+diff+" days ago";
					else retStr = ""+diff+" days";
				}
		    } else {
		    	long diff = Math.round(Double.valueOf(epochTime))*1000;
		    	if (shorttext) {
		    		retStr = DateFormat.format("MMM dd", diff).toString();
		    	} else {
		    		retStr = DateFormat.format("MMM dd yyyy", diff).toString();
		    	}
		    }   
		    return retStr;
	}
	
	public static String formatDateForStreamHtml(String epochTime, Boolean shorttext){
		String retStr = null;
	    double ti = Math.round(System.currentTimeMillis()/1000) - Double.valueOf(epochTime);
	    
	    if (ti < 60) {
	        retStr= "<b>just now</b>";
	    } else if (ti < 3600) {
	        long diff = Math.round(ti / 60);
			if (diff == 1) { 
				if (shorttext) retStr = "<b>"+diff +"</b> min";
				else retStr =  "<b>"+diff+"</b> minute"; 
			} else {
				if (shorttext) retStr = "<b>"+diff+"</b> min";
				else retStr =  "<b>"+diff+"</b> minutes"; 
			}
	    } else if (ti < 86400) {
	    	long diff = Math.round(ti/60/60);
			if (diff == 1) { 
				if (shorttext) retStr = "<b>"+diff+"</b> hr";
				else retStr = "<b>"+diff+"</b> hour";
			} else {
				if (shorttext) retStr = "<b>"+diff+"</b> hrs";
				else retStr = "<b>"+diff+"</b> hours";
			}
	    } else if (ti < 2629743) {
	        long diff = Math.round(ti / 60 / 60 / 24);
			if (diff == 1) { 
				if (shorttext) retStr = "<b>"+diff+"</b> day";
				else retStr = "<b>"+diff+"</b> day";
			} else {
				if (shorttext) retStr = "<b>"+diff+"</b> days";
				else retStr = "<b>"+diff+"</b> days";
			}
	    } else {
	    	long diff = Math.round(Double.valueOf(epochTime))*1000;
	    	if (shorttext) {
	    		retStr = DateFormat.format("MMM dd", diff).toString();
	    	} else {
	    		retStr = DateFormat.format("MMM dd yyyy", diff).toString();
	    	}
	    }   
	    return retStr;
}

}
