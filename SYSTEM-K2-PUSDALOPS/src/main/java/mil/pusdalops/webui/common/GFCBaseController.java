package mil.pusdalops.webui.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

public class GFCBaseController extends GenericForwardComposer<Component> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6157099593248143491L;

	// Format : Date
	private final String longDateFormat 	= "dd-MMMM-yyyy";
	private final String shortDateFormat 	= "dd-MM-yyyy";
	private final String emphYearMonth		= "yyyy-MMMM-dd";
	private final String emphYearMonthShort = "yyyyMMdd";
	
	// Format : Decimal and Non-Decimal 
	private final String floatFormatLocal	= "##.##0,00";
	private final String decimalFormatLocal	= "###.###.###,00";
	private final String intFormat			= "##,##0";
	private final String integerFormat		= "###,###,###";

	// private final String floatFormat		= "##,##0.00";
	// private final String decimalFormat	= "###,###,###.00";
	
	// Format : Time
	private final String timeFormat			= "HH:mm:ss";
	
	// Locale
	private final Locale locale				= new Locale("id","ID");
	
	public GFCBaseController() {
		super();
	}
	
	/*
	 * Date functions
	 */
	
	public ZoneId getSystemTimeZone() {

		return ZoneId.of("Asia/Jakarta");
	}	

	/*
	 * current java.time.LocalDate & LocalTime
	 */
	
	public LocalDate getLocalDate() {
		
		return LocalDate.now(ZoneId.of(getSystemTimeZone().toString()));
	}
	
	public LocalDate getLocalDate(ZoneId zoneId) {
		
		return LocalDate.now(zoneId);
	}	

	public String getLocalDateString(String strFormat) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(strFormat, getLocale());
		
		return getLocalDate().format(formatter);
	}
	
	public String getLocalTimeString(String timeFormat) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat, getLocale());
		
		return getLocalTime().format(formatter);
	}	
	
	/*
	 * current java.time.LocalTime
	 */

	public LocalTime getLocalTime() {
		
		return LocalTime.now(ZoneId.of(getSystemTimeZone().toString()));
	}
	
	public LocalTime getLocalTime(ZoneId zoneId) {
		
		return LocalTime.now(zoneId);
	}

	/*
	 * current java.time.LocalDateTime
	 */
	
	public LocalDateTime getLocalDateTime() {

		return LocalDateTime.now(ZoneId.of(getSystemTimeZone().toString()));
	}

	public LocalDateTime getLocalDateTime(ZoneId zoneId) {
		
		return LocalDateTime.now(zoneId);
	}
	
	/*
	 * to java.util.Date
	 */
	
	public Date asDate(LocalDate localDate) {
		
		return java.sql.Date.valueOf(localDate);
	}
	
	public Date asDate(LocalDateTime localDateTime) {
		
		return java.sql.Timestamp.valueOf(localDateTime);
	}
	
	/*
	 * to java.time.LocalDate
	 */
	
	public LocalDate asLocalDate(Date date) {
		
		return new java.sql.Date(date.getTime()).toLocalDate();
	}
	
	public LocalDate asLocalDate(LocalDateTime localDateTime) {

		return localDateTime.toLocalDate();
	}	

	/*
	 * to java.time.LocalDateTime
	 */
	
	public LocalDateTime asLocalDateTime(Date date) {
		
		return new java.sql.Timestamp(
			date.getTime()).toLocalDateTime();	
	}
	
	public LocalDateTime asLocalDateTime(LocalDate localDate, LocalTime localTime) {
		
		return localDate.atTime(localTime);
	}
	
	/*
	 * to String
	 */
		
	public String getLocalDateString(LocalDate localDate, String strFormat) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(strFormat, getLocale());
		
		return localDate.format(formatter);
	}	

	public String getLocalDateTimeString(LocalDateTime localDateTime, String strFormat) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(strFormat, getLocale());
		
		return localDateTime.format(formatter);
	}	

	
	/**
	 * Formatted to dd-MMMM-yyyy
	 * @return String
	 */
	public String getLongDateFormat() {
		return longDateFormat;
	}

	/**
	 * Formatted to dd-MM-yyyy
	 * @return String
	 */
	public String getShortDateFormat() {
		return shortDateFormat;
	}

	/**
	 * Formatted to yyyy-MMMM-dd
	 * @return String
	 */
	public String getEmphYearMonth() {
		return emphYearMonth;
	}	
	
	/**
	 * Formatted to yyyyMMdd
	 * @return
	 */
	public String getEmphYearMonthShort() {
		return emphYearMonthShort;
	}
	
	/**
	 * Locale("id","ID") - Indonesia
	 * @return Locale
	 */
	public Locale getLocale() {
		return locale;
	}	
	
	/**
	 * Formatted to "00,000"
	 * @return String
	 */
	public String getIntFormat() {
		return intFormat;
	}
	
	/**
	 * Formatted to "###,###,###";
	 * @return String
	 */
	public String getIntegerFormat() {
		return integerFormat;
	}

	/**
	 * Formated to HH:mm:ss
	 * @return String
	 */
	public String getTimeFormat() {
		return timeFormat;
	}
	
	/*
	 * DateTime Functions
	 */	
	
	/**
	 * dateToStringDB returns the date to a string value with a fixed
	 * format: yyyy-MM-dd, as required by database
	 * 
	 * @param localDate - Date value
	 * @return - formatted date string
	 */
	public String dateToStringDB(LocalDate localDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", getLocale());
		
		return localDate.format(formatter);
	}
	
	/**
	 * dateToStringDB returns the date to a string value with according
	 * to the formatStr parameter.  This method provides flexibility
	 * to produce different string date: year with month, year only,
	 * month only, etc.
	 * 
	 * @param date - Date value
	 * @param format - the format to produce the date string
	 * @return formatted date string
	 */
	public String dateToStringDB(LocalDate localDate, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, getLocale());

		return localDate.format(formatter);
	}
	
	/**
	 * dateToStringDisplay returns the date with a specified Locale,
	 * to a string value according to the formatStr parameter.  The
	 * format must be in the form of: dd-MMMM-yyyy to produce the
	 * Locale required for the month's spelling.
	 * 
	 * @param date - Date value
	 * @param format - the format to produce the date string
	 * @return formated date string 
	 */
	public String dateToStringDisplay(LocalDate date, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, getLocale());

		return date.format(formatter);	
	}
	
	public LocalDate getFirstdateOfTheMonth(LocalDate currentDate) {
		
		return currentDate.with(TemporalAdjusters.firstDayOfMonth());
	}

	public LocalDate getLastDateOfTheMonth(LocalDate currentDate) {
		
		return currentDate.with(TemporalAdjusters.lastDayOfMonth());
	}
		
	public LocalDate getFirstDateOfTheWeek(LocalDate currentDate) {

		TemporalField fieldID = WeekFields.of(getLocale()).dayOfWeek();

		return currentDate.with(fieldID, 1);
	}
		
	public LocalDate getLastDateOfTheWeek(LocalDate currentDate, int workDay) {

		TemporalField fieldID = WeekFields.of(getLocale()).dayOfWeek();

		return currentDate.with(fieldID, workDay);		
	}
	
	public List<LocalDate> getDatesOfTheWeek(LocalDate currentDate, int workDay) {
		
		List<LocalDate> datesOfTheWeek = new ArrayList<LocalDate>();

		TemporalField fieldID = WeekFields.of(getLocale()).dayOfWeek();
		
		for (int i = 1; i <= workDay; i++) {
			datesOfTheWeek.add(currentDate.with(fieldID, i));
		}
		
		return datesOfTheWeek;
	}
			
	public LocalDate addDate(long day, LocalDate currentDate) {
		
		return currentDate.plusDays(day);
	}

	public LocalDate minusDate(long day, LocalDate currentDate) {
		
		return currentDate.minusDays(day);
	}
	
	public long dayDiff(LocalDate dateDiff01, LocalDate dateDiff02) {
		
		LocalDate tempDate = LocalDate.from(dateDiff01);
		
		return tempDate.until(dateDiff02, ChronoUnit.DAYS);
	}
		
	/*
	 * Decimal and Integer Functions
	 */

	/**
	 * Conver a BigDecimal nominal value to String using:
	 * - RoundingMode.HALF-EVEN - 125.25 -> 125 and 125.75 -> 126
	 * - Locale Format - 125000000 -> 125.000.000,-
	 * 
	 * NOTE: The ',-' is added manually by this function.
	 * 
	 * @param nominal - BigDecimal nominal value to be converted
	 * @return
	 */
	public String toLocalFormat(BigDecimal nominal) {
		BigDecimal nominalRound = nominal.setScale(0, RoundingMode.HALF_EVEN); 

		DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(getLocale());
		
		return decimalFormat.format(nominalRound)+",-";
	}
	
	/**
	 * 
	 * 
	 * @param nominal
	 * @return
	 */
	public String toLocalFormatWithDecimal(BigDecimal nominal) {
		BigDecimal nominalRound = nominal.setScale(2);

		DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(getLocale());
		decimalFormat.applyLocalizedPattern(getDecimalFormatLocal());
		
		return decimalFormat.format(nominalRound);		
	}
	
	/**
	 * 
	 * 
	 * @param bigDecimal
	 * @return
	 */
	public String getFormatedFloatLocal(BigDecimal bigDecimal) {
		DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance(getLocale());
		formatter.applyLocalizedPattern(getFloatFormatLocal());
		
		return formatter.format(bigDecimal);
	}

	/**
	 * 
	 * 
	 * @param floatValue
	 * @return
	 */
	public String getFormatedFloatLocal(float floatValue) {
		DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance(getLocale());
		formatter.applyLocalizedPattern(getFloatFormatLocal());
		
		return formatter.format(floatValue);
	}
	
	/**
	 * 
	 * 
	 * @param integerValue
	 * @return
	 */
	public String getFormatedInteger(int integerValue) {
		DecimalFormat df = new DecimalFormat(integerFormat);
		
		return df.format(integerValue); 
	}
	
	/**
	 * 
	 * 
	 * @param bigDecimal
	 * @return
	 */
	public String getFormatedInteger(BigDecimal bigDecimal) {
		DecimalFormat df = new DecimalFormat(integerFormat);
		
		return df.format(bigDecimal); 
	}
	
	
	/**
	 * An integer string value to be converted to int value
	 * 
	 * @param stringValue
	 * @return int value
	 * @throws Exception - if the stringValue contains non integer char
	 */
	public int getIntValue(String stringValue) throws Exception {
		try {
			
			return Integer.parseInt(stringValue);

		} catch (Exception e) {
			throw new Exception(e);
		}

	}
	
	/*
	 * NOT PRIMITIVE FUNCTIONS - NOT GENERIC ENOUGH - SPECIFIC TO CERTAIN APPS
	 */

	/**
	 * 
	 * 
	 * @param monthCombobox
	 * @param monthListType
	 */
	public void setupMonthList(Combobox monthCombobox, MonthListType monthListType) {
		// create the list of months
		List<DateComboValue> monthList = createMonthComboValues(monthListType);
		
		for (DateComboValue monthComboValue : monthList) {
			Comboitem monthComboitem = new Comboitem();
			
			monthComboitem.setValue(monthComboValue);
			monthComboitem.setLabel(monthComboValue.getDisplayValue());
			monthComboitem.setParent(monthCombobox);
		}
		
		// set the current month
		monthCombobox.setSelectedIndex(0);		
	}
	
	/**
	 * 
	 * 
	 * @param yearCombobox
	 * @param maxYear
	 */
	public void setupYearList(Combobox yearCombobox, int maxYear) {
		// create the list of years
		List<DateComboValue> yearList = createYearComboValues(maxYear);
		
		for (DateComboValue yearComboValue : yearList) {
			Comboitem yearComboitem = new Comboitem();
			
			yearComboitem.setValue(yearComboValue);
			yearComboitem.setLabel(yearComboValue.getDisplayValue());
			yearComboitem.setParent(yearCombobox);
		}
		
		// set the 1st item - current year
		yearCombobox.setSelectedIndex(0);		
	}
	

	/**
	 * 
	 * 
	 * @param monthListType
	 * @return
	 */
	public List<DateComboValue> createMonthComboValues(MonthListType monthListType) {
		List<DateComboValue> dateComboValues = new ArrayList<DateComboValue>();
				
		// setup the month format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", getLocale());		
		
		if (monthListType.compareTo(MonthListType.CURRENT) == 0) { 
			// init localDate to first date of the month
			LocalDate localDate = getLocalDate().with(TemporalAdjusters.firstDayOfMonth());

			// current month
			dateComboValues.add(new DateComboValue(
					localDate.format(formatter), localDate.format(formatter), localDate));
			
			int currMonthValue = localDate.getMonthValue();
			
			// previous months - from the current month
			for (int i = 0; i < currMonthValue-1; i++) {
				localDate = localDate.minus(1, ChronoUnit.MONTHS);
				
				DateComboValue dateComboValue = new DateComboValue(
						localDate.format(formatter), localDate.format(formatter), localDate);
				
				dateComboValues.add(dateComboValue);
			}
		} else {
			// reset to first day of the year
			LocalDate localDate = getLocalDate().with(TemporalAdjusters.firstDayOfYear());			
			
			for (int i = 0; i < 12; i++) {
	
				DateComboValue dateComboValue = new DateComboValue(
						localDate.format(formatter), localDate.format(formatter), localDate);
	
				dateComboValues.add(dateComboValue);
	
				localDate = localDate.plus(1, ChronoUnit.MONTHS);
			}
		}
		
		return dateComboValues;
	}
	
	/**
	 * 
	 * 
	 * @param numberOfYears
	 * @return
	 */
	public List<DateComboValue> createYearComboValues(int numberOfYears) {
		List<DateComboValue> dateComboValues = new ArrayList<DateComboValue>();
		
		// init localDate to first day of the year
		LocalDate localDate = getLocalDate().with(TemporalAdjusters.firstDayOfYear());
		
		// setup the year format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy", getLocale());

		// current year
		dateComboValues.add(new DateComboValue(
				localDate.format(formatter), localDate.format(formatter), localDate));

		// previous years
		for (int i = 0; i < numberOfYears; i++) {
			localDate = localDate.minus(1, ChronoUnit.YEARS);
			
			DateComboValue dateComboValue = new DateComboValue(
					localDate.format(formatter), localDate.format(formatter), localDate);
			
			dateComboValues.add(dateComboValue);
		}
		
		return dateComboValues;
	}
	
	/************************************************************************
	 * 
	 * List Management
	 * 
	 *************************************************************************/
	
	/**
	 * @param initList
	 * @param modifiedList
	 * @return
	 */
	public boolean isItemInTheList(List<? extends Object> initList, List<? extends Object>modifiedList) {
		Collection<Object> collectionInit = new HashSet<Object>(initList);
		Collection<Object> collectionModified = new HashSet<Object>(modifiedList); 
		
		return collectionModified.removeAll(collectionInit);
	}
	
	public List<? extends Object> getItemsNotInTheList(List<? extends Object> initList, List<? extends Object>modifiedList) {
		Collection<Object> collectionInit = new HashSet<Object>(initList);
		Collection<Object> collectionModified = new HashSet<Object>(modifiedList);
				
		collectionModified.removeAll(collectionInit);
		
		return new ArrayList<Object>(collectionModified);
	}

	/*************************************************************************
	 * 
	 * Serial Number Format
	 * 
	 *************************************************************************/
	
	/**
	 * @param digit
	 * @return Sring
	 */
	public String formatTo4DigitWithLeadingZeo(int digit) {		
		String digitStrValue = String.valueOf(digit);
	
		switch (digitStrValue.length()) {
			case 1: return "000"+digit;
			case 2:	return "00"+digit;
			case 3: return "0"+digit;
			case 4: return digitStrValue;
			default:
				return "0000";
		}
	}

	/**
	 * @param digit
	 * @return Sring
	 */
	public String formatTo3DigitWithLeadingZeo(int digit) {		
		String digitStrValue = String.valueOf(digit);
	
		switch (digitStrValue.length()) {
			case 1: return "00"+digit;
			case 2:	return "0"+digit;
			case 3: return digitStrValue;
			default:
				return "0000";
		}
	}

	/**
	 * 
	 * 
	 * @param digit
	 * @return String
	 */
	public String formatTo2DigitWithLeadingZeo(int digit) {
		String digitStrValue = String.valueOf(digit);
		
		switch (digitStrValue.length()) {
			case 1: return "0"+digit;
			case 2: return digitStrValue;
			default:
				return "00";
		}		
	}
	
	/**
	 * 
	 * 
	 * @param accountTypeNo
	 * @param accountGroupNo
	 * @param subAccount01No
	 * @param subAccount02No
	 * @param masterAccountNo
	 * @return
	 */
	public String formatCoaNumber(int accountTypeNo, int accountGroupNo, int subAccount01No, int subAccount02No, int masterAccountNo) {
		String typeNo = String.valueOf(accountTypeNo);
		String groupNo = String.valueOf(accountGroupNo);
		String sub01No = String.valueOf(subAccount01No);
		String sub02No = String.valueOf(subAccount02No);
		String masterNo = String.valueOf(masterAccountNo);
		
		switch (masterNo.length()) {
			case 1: return typeNo+"."+groupNo+sub01No+sub02No+".000"+masterNo;
			case 2: return typeNo+"."+groupNo+sub01No+sub02No+".00"+masterNo;
			case 3: return typeNo+"."+groupNo+sub01No+sub02No+".0"+masterNo;
			case 4: return typeNo+"."+groupNo+sub01No+sub02No+"."+masterNo;
			default:
				return "0.000.0000";
		}
	}

	/**
	 * format to: YYYYMMDDHHMMddd
	 * 
	 * @param code
	 * @param LocalDateTime
	 * @param serialNum
	 * @return String
	 */
	public String formatSerialComp(String code, LocalDateTime localDateTime, int serialNum) {
		String serialComp;
		
		String year 	= getLocalDateTimeString(localDateTime, "YYYY");
		String month	= getLocalDateTimeString(localDateTime, "MM");
		String day		= getLocalDateTimeString(localDateTime, "dd");
		String hour		= getLocalDateTimeString(localDateTime, "HH");
		String minute	= getLocalDateTimeString(localDateTime, "mm");
		
		if (code==null) {
			serialComp = year+
					formatTo2DigitWithLeadingZeo(Integer.valueOf(month))+
					day+
					hour+
					minute+
					formatTo3DigitWithLeadingZeo(serialNum);

		} else {
			serialComp = code+
					year+
					formatTo2DigitWithLeadingZeo(Integer.valueOf(month))+
					day+
					hour+
					minute+
					formatTo3DigitWithLeadingZeo(serialNum);					
		}
		
		return serialComp;
			
	}

	/**
	 * floatFormatLocal	= "##.##0,00";
	 * 
	 * @return
	 */
	public String getFloatFormatLocal() {
		return floatFormatLocal;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public String getDecimalFormatLocal() {
		return decimalFormatLocal;
	}
}
