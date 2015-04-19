package com.gdin.netcentermanm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String date2String(Date date){
		return dateFormat.format(date);
	}
    public static String date2String(Date date,String format){
        return new SimpleDateFormat(format).format(date);
    }


}
