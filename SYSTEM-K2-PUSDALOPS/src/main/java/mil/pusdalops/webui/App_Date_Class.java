package mil.pusdalops.webui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class App_Date_Class {

	public static void main(String[] args) throws Exception {
		// year
		String yearStr = "2020";
		// datetime
		String datetimeStr = "927.1222";
		// split
		String[] datetime = datetimeStr.split("\\.");
		// convert to list
		List<String> list = Arrays.asList(datetime);
		
		String monthDateStr = null;
		String timeStr = null;
		
		try {
			monthDateStr = list.get(0);
			timeStr = list.get(1);			
		} catch (Exception e) {
			throw new Exception("Tw format: "+datetimeStr+" tidak sesuai.");
		}
		
		String month = monthDateStr.substring(0, 2);
		String day = monthDateStr.substring(2, 4);		
		
		String hour = timeStr.substring(0, 2);
		String mint = timeStr.substring(2, 4);

		LocalDate localdate = null;
		
		try {
			localdate = LocalDate.of(Integer.valueOf(yearStr), Integer.valueOf(month), Integer.valueOf(day));			
		} catch (Exception e) {
			throw new Exception("Tanggal: "+
					yearStr+"-"+
					month+"-"+
					day+"-"+
					" tidak sesuai");
		}
		System.out.println(localdate);
		
		
		LocalTime localtime = LocalTime.of(Integer.valueOf(hour), Integer.valueOf(mint));
		System.out.println(localtime);
		
		LocalDateTime localdatetime = LocalDateTime.of(localdate, localtime);
		System.out.println(localdatetime);
	}

}
