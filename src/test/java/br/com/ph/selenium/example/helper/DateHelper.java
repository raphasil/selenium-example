package br.com.ph.selenium.example.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateHelper {
	
	private static DateHelper instance;
	
	public synchronized static DateHelper getInstance() {
		if(instance == null) {
			instance = new DateHelper();
		}
		
		return instance;
	}
	
	private DateHelper() {
		
	}

	public String format(String fmt) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		return sdf.format(calendar.getTime());
	}
}
