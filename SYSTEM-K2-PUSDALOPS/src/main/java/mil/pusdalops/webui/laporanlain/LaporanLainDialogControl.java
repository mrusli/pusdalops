package mil.pusdalops.webui.laporanlain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.laporanlain.LaporanLain;
import mil.pusdalops.domain.serial.DocumentSerialNumber;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.common.SerialNumberGenerator;
import mil.pusdalops.webui.common.TwConversion;
import mil.pusdalops.webui.dialogs.DatetimeData;

public class LaporanLainDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2260328147479256727L;

	private SerialNumberGenerator serialNumberGenerator;
	private TwConversion twConversion;
	
	private Window laporanLainDialogWin;
	private Textbox kejadianIdTextbox, judulTopikTextbox, twBuatTahunTextbox,
		twBuatTanggalJamTextbox, twBuatTimeZoneTextbox, laporanTextbox;
	
	private LaporanLainData laporanLainData;
	private LaporanLain laporanLain;
	private Kotamaops settingsKotamaops;
	private ZoneId zoneId;
	private LocalDateTime currentLocalDateTime;
	private DocumentSerialNumber documentSerialNumber;

	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.GenericForwardComposer#doAfterCompose(org.zkoss.zk.ui.Component)
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// laporanLainData
		setLaporanLainData((LaporanLainData) arg.get("laporanLainData"));
	}
	
	public void onCreate$laporanLainDialogWin(Event event) throws Exception {
		setLaporanLain(getLaporanLainData().getLaporanLain());
		setSettingsKotamaops(getLaporanLainData().getSettingsKotamaops());
		
		// set the timezone for this kotamaops
		int timezoneOrdinal = getSettingsKotamaops().getTimeZone().ordinal();
		setZoneId(getSettingsKotamaops().getTimeZone().toZoneId(timezoneOrdinal));			
		
		// current datetime -- according to time zone (not just id)
		setCurrentLocalDateTime(getLocalDateTime(getZoneId()));		

		// display
		displayLaporanLainInfo();
	}

	private void displayLaporanLainInfo() throws Exception {
		if (getLaporanLain().getId().compareTo(Long.MIN_VALUE)==0) {
			// NEW LAPORAN

			// get the document code from kotamaops
			String documentCode = getSettingsKotamaops().getDocumentCode();			
			// serial number
			setDocumentSerialNumber(createSerialNumber(getCurrentLocalDateTime(), documentCode));
			// get the tw buat tahun, tanggal, timezone
			// create a new ID : YYYYMMDDHHMMddd
			kejadianIdTextbox.setValue(getDocumentSerialNumber().getSerialComp());
			twBuatTahunTextbox.setValue(getLocalDateTimeString(getCurrentLocalDateTime(), "YYYY"));
			// format to 0518.1313
			twBuatTanggalJamTextbox.setValue(
					getLocalDateTimeString(getCurrentLocalDateTime(), "MM")+
					getLocalDateTimeString(getCurrentLocalDateTime(), "dd")+"."+
					getLocalDateTimeString(getCurrentLocalDateTime(), "HH")+
					getLocalDateTimeString(getCurrentLocalDateTime(), "mm"));
			// timezone
			twBuatTimeZoneTextbox.setValue(getSettingsKotamaops().getTimeZone().toString());
			twBuatTimeZoneTextbox.setAttribute("twBuatTimeZoneInd", getSettingsKotamaops().getTimeZone());
		} else {
			// EXISTING LAPORAN
			
			kejadianIdTextbox.setValue(getLaporanLain().getSerialNumber().getSerialComp());
			setDocumentSerialNumber(getLaporanLain().getSerialNumber());
			judulTopikTextbox.setValue(getLaporanLain().getJudulLaporan());
			// use existing tw buat tahun, tanggal, timezone
			twBuatTahunTextbox.setValue(getLocalDateTimeString(asLocalDateTime(getLaporanLain().getTwPembuatanDateTime()), "yyyy"));
			twBuatTanggalJamTextbox.setValue(
					getLocalDateTimeString(asLocalDateTime(getLaporanLain().getTwPembuatanDateTime()),"MM")+
					getLocalDateTimeString(asLocalDateTime(getLaporanLain().getTwPembuatanDateTime()),"dd")+"."+
					getLocalDateTimeString(asLocalDateTime(getLaporanLain().getTwPembuatanDateTime()), "HH")+
					getLocalDateTimeString(asLocalDateTime(getLaporanLain().getTwPembuatanDateTime()), "mm"));
			twBuatTimeZoneTextbox.setValue(getLaporanLain().getTwPembuatanTimezone().toString());
			twBuatTimeZoneTextbox.setAttribute("twBuatTimeZoneInd", getSettingsKotamaops().getTimeZone());
			laporanTextbox.setValue(getLaporanLain().getIsiLaporan());
		}
	}
	
	public void onClick$twBuatRubahButton(Event event) throws Exception {
		// create the data object
		DatetimeData datetimeData = new DatetimeData();
		// set the dialog title
		datetimeData.setDialogWinTitle("Rubah TW Kejadian");
		
		if (getLaporanLain().getId().compareTo(Long.MIN_VALUE)==0) {
			datetimeData.setTimezoneInd(getSettingsKotamaops().getTimeZone());
			// check if the user actually selects the kotamaops
			TimezoneInd selKotamaopsTimezoneInd = 
					(TimezoneInd) twBuatTimeZoneTextbox.getAttribute("twBuatTimeZoneInd");
			int timezoneIndOrdinal;
			ZoneId zoneId=null;
			if (selKotamaopsTimezoneInd==null) {
				timezoneIndOrdinal = getSettingsKotamaops().getTimeZone().ordinal();
				zoneId = getSettingsKotamaops().getTimeZone().toZoneId(timezoneIndOrdinal);
			} else {
				timezoneIndOrdinal = selKotamaopsTimezoneInd.ordinal();
				zoneId = selKotamaopsTimezoneInd.toZoneId(timezoneIndOrdinal);
			}

			datetimeData.setZoneId(zoneId);
			datetimeData.setLocalDateTime(getCurrentLocalDateTime());			
		} else {
			TimezoneInd timezoneInd = getLaporanLain().getTwPembuatanTimezone();
			int timezoneIndOrdinal = timezoneInd.ordinal();
			ZoneId zoneId = timezoneInd.toZoneId(timezoneIndOrdinal);
			
			datetimeData.setTimezoneInd(timezoneInd);
			datetimeData.setZoneId(zoneId);
			datetimeData.setLocalDateTime(asLocalDateTime(getLaporanLain().getTwPembuatanDateTime()));
		}
		
		Map<String, DatetimeData> args = Collections.singletonMap("datetimeData", datetimeData);
		Window datetimeWin = (Window) Executions.createComponents(
				"/dialogs/DatetimeWinDialog.zul", laporanLainDialogWin, args);
		datetimeWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				DatetimeData datetimeData = (DatetimeData) event.getData();
				// System.out.println(datetimeData.toString());
				
				// year
				twBuatTahunTextbox.setValue(getLocalDateTimeString(datetimeData.getLocalDateTime(), "YYYY"));
				
				// mmdd.HHmm
				twBuatTanggalJamTextbox.setValue(					
						getLocalDateTimeString(datetimeData.getLocalDateTime(), "MM")+
						getLocalDateTimeString(datetimeData.getLocalDateTime(), "dd")+"."+
						getLocalDateTimeString(datetimeData.getLocalDateTime(), "HH")+
						getLocalDateTimeString(datetimeData.getLocalDateTime(), "mm"));
				
				// zona waktu
				twBuatTimeZoneTextbox.setValue(datetimeData.getTimezoneInd().toString());
				// save the object in the attribute
				twBuatTimeZoneTextbox.setAttribute("twBuatTimeZoneInd", datetimeData.getTimezoneInd());
			}
		});
		
		datetimeWin.doModal();		
	}
	
	public void onClick$saveButton(Event event) throws Exception {
		// get the changes
		LaporanLain userModlaporanLain = getLaporanLain();
		if (userModlaporanLain.getId().compareTo(Long.MIN_VALUE)==0) {
			userModlaporanLain.setCreatedAt(asDate(getCurrentLocalDateTime()));
			userModlaporanLain.setEditedAt(asDate(getCurrentLocalDateTime()));
			userModlaporanLain.setSerialNumber(getDocumentSerialNumber());			
		} else {
			userModlaporanLain.setEditedAt(asDate(getCurrentLocalDateTime()));
		}
		
		LocalDateTime twPembuatanLocalDateTime = 
				getTwConversion().convertTwToLocalDateTime(
						twBuatTahunTextbox.getValue(),
						twBuatTanggalJamTextbox.getValue());
		userModlaporanLain.setTwPembuatanDateTime(asDate(twPembuatanLocalDateTime));
		for (TimezoneInd timezoneInd : TimezoneInd.values()) {
			if (timezoneInd.toString().compareTo(twBuatTimeZoneTextbox.getValue())==0) {
				laporanLain.setTwPembuatanTimezone(timezoneInd);				
			}
		}
		userModlaporanLain.setTw(twBuatTanggalJamTextbox.getValue());
		userModlaporanLain.setTahun(twBuatTahunTextbox.getValue());
		userModlaporanLain.setKotamaops(getSettingsKotamaops());
		userModlaporanLain.setJudulLaporan(judulTopikTextbox.getValue());
		userModlaporanLain.setIsiLaporan(laporanTextbox.getValue());
		
		// send event ON_OK
		Events.sendEvent(Events.ON_OK, laporanLainDialogWin, userModlaporanLain);
		
		laporanLainDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		
		laporanLainDialogWin.detach();
	}

	private DocumentSerialNumber createSerialNumber(LocalDateTime currentDateTime, String documentCode) throws Exception {
		
		// get the serial number
		int serNum = getSerialNumberGenerator().getSerialNumber(documentCode, getZoneId(), currentDateTime);

		// create a new object
		DocumentSerialNumber serialNumber = new DocumentSerialNumber();
		serialNumber.setDocumentCode(getSettingsKotamaops().getDocumentCode());
		serialNumber.setCreatedAt(asDate(getCurrentLocalDateTime()));
		serialNumber.setEditedAt(asDate(getCurrentLocalDateTime()));
		serialNumber.setDocumentCode(documentCode);
		serialNumber.setSerialDate(asDate(currentDateTime));
		serialNumber.setSerialNo(serNum);
		serialNumber.setSerialComp(formatSerialComp(documentCode, currentDateTime, serNum));
		
		return serialNumber;
	}
	
	public LaporanLain getLaporanLain() {
		return laporanLain;
	}

	public void setLaporanLain(LaporanLain laporanLain) {
		this.laporanLain = laporanLain;
	}

	public LaporanLainData getLaporanLainData() {
		return laporanLainData;
	}

	public void setLaporanLainData(LaporanLainData laporanLainData) {
		this.laporanLainData = laporanLainData;
	}

	public Kotamaops getSettingsKotamaops() {
		return settingsKotamaops;
	}

	public void setSettingsKotamaops(Kotamaops settingsKotamaops) {
		this.settingsKotamaops = settingsKotamaops;
	}

	public ZoneId getZoneId() {
		return zoneId;
	}

	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	public LocalDateTime getCurrentLocalDateTime() {
		return currentLocalDateTime;
	}

	public void setCurrentLocalDateTime(LocalDateTime currentLocalDateTime) {
		this.currentLocalDateTime = currentLocalDateTime;
	}

	public SerialNumberGenerator getSerialNumberGenerator() {
		return serialNumberGenerator;
	}

	public void setSerialNumberGenerator(SerialNumberGenerator serialNumberGenerator) {
		this.serialNumberGenerator = serialNumberGenerator;
	}

	public DocumentSerialNumber getDocumentSerialNumber() {
		return documentSerialNumber;
	}

	public void setDocumentSerialNumber(DocumentSerialNumber documentSerialNumber) {
		this.documentSerialNumber = documentSerialNumber;
	}

	public TwConversion getTwConversion() {
		return twConversion;
	}

	public void setTwConversion(TwConversion twConversion) {
		this.twConversion = twConversion;
	}
	
}
