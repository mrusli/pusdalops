package mil.pusdalops.webui.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class TwConversion extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5797318394267332062L;

	public LocalDateTime convertTwToLocalDateTime(String thn, String tw) throws Exception {
		List<String> list = dateTimeSplit(isCharSufficient(tw));
		
		// get the monthdate and time as string values
		String monthDateStr = null;
		String timeStr = null;
		try {
			monthDateStr = list.get(0);
			timeStr = list.get(1);
		} catch (Exception e) {
			throw new Exception("Tw format :"+tw+" tidak sesuai.");
		}
		
		String month, day, hour, mint;
		
		try {
			// split the month, day, hour and minutes
			month = monthDateStr.substring(0, 2);
			day = monthDateStr.substring(2, 4);
			
			hour = timeStr.substring(0, 2);
			mint = timeStr.substring(2, 4);			
		} catch (Exception e) {
			throw new Exception("Tw format tidak sesuai.");
		}
		
		LocalDate localdate = constructLocalDate(thn, month, day);
		
		LocalTime localtime = constructLocalTime(hour, mint);
		
		return LocalDateTime.of(localdate, localtime);
	}
	
	private String isCharSufficient(String tw) throws Exception {
		// is tw 9 char long?
		if (tw.length()<9) {
			// append '0' in front
			tw = '0'+tw;
		} else {
			// do nothing
		}
		
		return tw;
	}
	
	private List<String> dateTimeSplit(String tw) throws Exception {
		String[] datetime;
		
		try {
			// split
			datetime = tw.split("\\.");			
		} catch (Exception e) {
			throw new Exception("Tw pemisahan antara tanggal dan jam tidak sesuai.");
		}
		
		List<String> list = Arrays.asList(datetime);
		
		return list;
	}
	
	private LocalTime constructLocalTime(String hour, String minute) throws Exception {
		// construct a localTime object
		LocalTime localtime = null;
		try {
			localtime = LocalTime.of(Integer.valueOf(hour), Integer.valueOf(minute));
		} catch (Exception e) {
			throw new Exception("Jam: "+
					hour+":"+
					minute+
					" tidak sesuai.");
		}
		
		return localtime;
	}
	
	private LocalDate constructLocalDate(String thn, String month, String day) throws Exception {
		// construct a localDate object
		LocalDate localdate = null;
		try {
			localdate = LocalDate.of(Integer.valueOf(thn), Integer.valueOf(month), Integer.valueOf(day));
		} catch (Exception e) {
			throw new Exception("Tanggal: "+
					thn+"-"+
					month+"-"+
					day+"-"+
					" tidak sesuai.");
		}
		
		
		return localdate;
	}
}
