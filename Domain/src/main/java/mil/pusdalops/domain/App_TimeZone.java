package mil.pusdalops.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;

public class App_TimeZone {

	public static void main(String[] args) {
		System.out.println("HelloWorld!!!");
		
		ZoneId zoneId;
		LocalDate localDate;
		LocalTime localTime;
		
		zoneId = ZoneId.of("Asia/Jakarta");
		System.out.println(zoneId.getId());
		System.out.println(zoneId.getDisplayName(TextStyle.FULL, new Locale("id","ID")));
		System.out.println(zoneId.getRules());
		localDate = LocalDate.now(ZoneId.of(zoneId.toString()));
		localTime = LocalTime.now(ZoneId.of(zoneId.toString()));
		System.out.println(localDate+" "+localTime);
		
		zoneId = ZoneId.of("Asia/Ujung_Pandang");
		System.out.println(zoneId.getId());
		System.out.println(zoneId.getDisplayName(TextStyle.FULL, new Locale("id","ID")));
		System.out.println(zoneId.getRules());
		localDate = LocalDate.now(ZoneId.of(zoneId.toString()));
		localTime = LocalTime.now(ZoneId.of(zoneId.toString()));
		System.out.println(localDate+" "+localTime);
		
		zoneId = ZoneId.of("Asia/Jayapura");
		System.out.println(zoneId.getId());
		System.out.println(zoneId.getDisplayName(TextStyle.FULL, new Locale("id","ID")));
		System.out.println(zoneId.getRules());
		localDate = LocalDate.now(ZoneId.of(zoneId.toString()));
		localTime = LocalTime.now(ZoneId.of(zoneId.toString()));
		System.out.println(localDate+" "+localTime);

	}

}
