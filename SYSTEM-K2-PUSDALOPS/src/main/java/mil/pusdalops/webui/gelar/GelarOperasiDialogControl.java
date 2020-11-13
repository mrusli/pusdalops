package mil.pusdalops.webui.gelar;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.gelarops.GelarOperasi;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.domain.serial.DocumentSerialNumber;
import mil.pusdalops.persistence.gelaroperasi.dao.GelarOperasiDao;
import mil.pusdalops.persistence.kotamaops.dao.KotamaopsDao;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.common.SerialNumberGenerator;
import mil.pusdalops.webui.dialogs.DatetimeData;

public class GelarOperasiDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -877810375280969448L;
	
	private SerialNumberGenerator serialNumberGenerator;
	private KotamaopsDao kotamaopsDao;
	private GelarOperasiDao gelarOperasiDao;
	
	private Window gelarOperasiDialogWin;
	private Combobox kotamaopsCombobox, disposisiCombobox;
	private Textbox kejadianIdTextbox, twAwalTahunTextbox, twAwalTanggalJamTextbox, 
		twAwalTimeZoneTextbox, twAkhirTahunTextbox, twAkhirTanggalJamTextbox,
		twAkhirTimeZoneTextbox, satuanTextbox, brigadeTextbox, batalyonTextbox, kvTextbox,
		komandanTextbox, misiTextbox, jumlahTextbox;
	
	private GelarOperasiData gelarOperasiData;
	private Kotamaops kotamaops;
	private LocalDateTime currentLocalDateTime, awalLocalDateTime, akhirLocalDateTime;
	private ZoneId zoneId;
	private GelarOperasi gelarOperasi;
	private boolean twAwalAkhirProper;
	
	private static final Logger log = Logger.getLogger(GelarOperasiDialogControl.class);
	
	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.GenericForwardComposer#doAfterCompose(org.zkoss.zk.ui.Component)
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setGelarOperasiData(
				(GelarOperasiData) arg.get("gelarOperasiData"));
	}

	public void onCreate$gelarOperasiDialogWin(Event event) throws Exception {
		log.info("Creating GelarOperasiDialogControl...");
		
		setKotamaops(
				getGelarOperasiData().getKotamaops());
		setGelarOperasi(
				getGelarOperasiData().getGelarOperasi());
		
		// set the timezone for this kotamaops
		int timezoneOrdinal = getKotamaops().getTimeZone().getValue();
		setZoneId(getKotamaops().getTimeZone().toZoneId(timezoneOrdinal));					

		// set current datetime
		setCurrentLocalDateTime(getLocalDateTime(getZoneId()));
		
		// load kotamaops combobox
		loadKotamaopsCombobox();
		
		// load disposisi combobox
		loadDisposisiCombobox();
		
		// set tw awal dan akhir
		setTwAwalAkhir(10L);		
		
		// display
		displayGelarOperasiData();
	}

	private void loadKotamaopsCombobox() throws Exception {
		Comboitem comboitem;

		if (getKotamaops().getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0) {
			// pusdalops -- load other kotamaops under Pusdalops
			Kotamaops kotamaopsKotamaopsByProxy = 
					getKotamaopsDao().findKotamaopsKotamaopsByProxy(getKotamaops().getId());
			List<Kotamaops> kotamaopsList = kotamaopsKotamaopsByProxy.getKotamaops();
			
			for (Kotamaops kotamaops : kotamaopsList) {
				comboitem = new Comboitem();
				comboitem.setLabel(kotamaops.getKotamaopsName());
				comboitem.setValue(kotamaops);
				comboitem.setParent(kotamaopsCombobox);
			}
			
			kotamaopsCombobox.setSelectedIndex(0);
		} else {
			// kotamaops ONLY -- no other selections
			comboitem = new Comboitem();
			comboitem.setLabel(getKotamaops().getKotamaopsName());
			comboitem.setValue(getKotamaops());
			comboitem.setParent(kotamaopsCombobox);
			
			kotamaopsCombobox.setSelectedItem(comboitem);
			kotamaopsCombobox.setDisabled(true);
		}
		
	}

	private void loadDisposisiCombobox() {
		Comboitem comboitem;
		
		comboitem = new Comboitem();
		comboitem.setLabel("Pasukan");
		comboitem.setValue(null);
		comboitem.setParent(disposisiCombobox);
		
		disposisiCombobox.setSelectedItem(comboitem);
		disposisiCombobox.setDisabled(true);
	}

	private void setTwAwalAkhir(long minDays) {
		twAwalTahunTextbox.setValue(getCurrentLocalDateTime().minusDays(minDays).format(DateTimeFormatter.ofPattern("YYYY", getLocale())));
		twAwalTanggalJamTextbox.setValue(
				getCurrentLocalDateTime().minusDays(minDays).format(DateTimeFormatter.ofPattern("MM", getLocale()))+
				getCurrentLocalDateTime().minusDays(minDays).format(DateTimeFormatter.ofPattern("dd", getLocale()))+".0000");
		twAwalTimeZoneTextbox.setValue(getKotamaops().getTimeZone().toString());

		// set awal
		setAwalLocalDateTime(getCurrentLocalDateTime().minusDays(minDays));
		
		// akhir
		twAkhirTahunTextbox.setValue(getLocalDateTimeString(getCurrentLocalDateTime(), "YYYY"));
		twAkhirTanggalJamTextbox.setValue(
				getLocalDateTimeString(getCurrentLocalDateTime(), "MM")+
				getLocalDateTimeString(getCurrentLocalDateTime(), "dd")+".0000");
		twAkhirTimeZoneTextbox.setValue(getKotamaops().getTimeZone().toString());
		
		// set akhir
		setAkhirLocalDateTime(getCurrentLocalDateTime());
		
		setTwAwalAkhirProper(true);				
		
	}
	
	public void onClick$twAwalRubahButton(Event event) throws Exception {
		// create the data object
		DatetimeData datetimeData = new DatetimeData();
		// set the object
		datetimeData.setDialogWinTitle("Rubah TW Awal");
		datetimeData.setTimezoneInd(getKotamaops().getTimeZone());
		datetimeData.setZoneId(getZoneId());
		datetimeData.setLocalDateTime(getAwalLocalDateTime());
				
		// pass to the dialog window
		Map<String, DatetimeData> args = Collections.singletonMap("datetimeData", datetimeData);
		Window datetimeWin = (Window) Executions.createComponents(
				"/dialogs/DatetimeWinDialog.zul", gelarOperasiDialogWin, args);
		datetimeWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				DatetimeData datetimeData = (DatetimeData) event.getData();
				
				log.info("Change twAwal to: "+datetimeData.getLocalDateTime());
				setAwalLocalDateTime(datetimeData.getLocalDateTime());

				// LocalDate twAwalDate = datetimeData.getLocalDateTime().toLocalDate();
				// LocalTime twNoTime = LocalTime.of(0, 0, 0);
				
				twAwalTahunTextbox.setValue(getLocalDateTimeString(datetimeData.getLocalDateTime(), "YYYY"));
				twAwalTanggalJamTextbox.setValue(
						getLocalDateTimeString(datetimeData.getLocalDateTime(), "MM")+
						getLocalDateTimeString(datetimeData.getLocalDateTime(), "dd"));
				twAwalTanggalJamTextbox.setAttribute(
						"twAwalLocalDateTime", datetimeData.getLocalDateTime());
				
				
				if (getAwalLocalDateTime().isAfter(getAkhirLocalDateTime())) {
					setTwAwalAkhirProper(false);
					throw new Exception("TW Awal TIDAK melewati TW Akhir");
				} else {
					setTwAwalAkhirProper(true);
				}
					
				
			}
		});
		
		datetimeWin.doModal();
		
	}
	
	public void onClick$twAkhirRubahButton(Event event) throws Exception {
		// create the data object
		DatetimeData datetimeData = new DatetimeData();
		// set the object
		datetimeData.setDialogWinTitle("Rubah TW Akhir");
		datetimeData.setTimezoneInd(getKotamaops().getTimeZone());
		datetimeData.setZoneId(getZoneId());
		datetimeData.setLocalDateTime(getAkhirLocalDateTime());

		// pass to the dialog window
		Map<String, DatetimeData> args = Collections.singletonMap("datetimeData", datetimeData);
		Window datetimeWin = (Window) Executions.createComponents(
				"/dialogs/DatetimeWinDialog.zul", gelarOperasiDialogWin, args);
		datetimeWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				DatetimeData datetimeData = (DatetimeData) event.getData();
				
				log.info(datetimeData.getLocalDateTime());
				setAkhirLocalDateTime(datetimeData.getLocalDateTime());
				
				// LocalTime twNoTime = LocalTime.of(0, 0, 0);
				
				twAkhirTahunTextbox.setValue(getLocalDateTimeString(datetimeData.getLocalDateTime(), "YYYY"));
				twAkhirTanggalJamTextbox.setValue(
						getLocalDateTimeString(datetimeData.getLocalDateTime(), "MM")+
						getLocalDateTimeString(datetimeData.getLocalDateTime(), "dd"));
				twAkhirTanggalJamTextbox.setAttribute(
						"twAkhirLocalDateTime", datetimeData.getLocalDateTime());
				
				if (getAkhirLocalDateTime().isBefore(getAwalLocalDateTime())) {
					setTwAwalAkhirProper(false);
					throw new Exception("TW Akhir TIDAK mengawali TW Awal");
				} else {
					setTwAwalAkhirProper(true);
				}
			}
		});
		
		datetimeWin.doModal();		
	}	

	private void displayGelarOperasiData() throws Exception {
		if (getGelarOperasi().getId().compareTo(Long.MIN_VALUE)==0) {
			// new -- create
			
			String documentCode = getKotamaops().getDocumentCode();
			// create a new serial number
			DocumentSerialNumber docSerialNum = createSerialNumber(getCurrentLocalDateTime(), documentCode);
			
			kejadianIdTextbox.setValue(docSerialNum.getSerialComp());
			kejadianIdTextbox.setAttribute("docSerialNum", docSerialNum);
			
		} else {
			Comboitem comboitem;
			// existing -- edit
			kejadianIdTextbox.setValue(getGelarOperasi().getSerialNumber().getSerialComp());
			kejadianIdTextbox.setAttribute("docSerialNum", getGelarOperasi().getSerialNumber());
			// tw awal
			twAwalTahunTextbox.setValue(getGelarOperasi().getTahunAwalOps());
			twAwalTanggalJamTextbox.setValue(getGelarOperasi().getTwAwal());
			twAwalTimeZoneTextbox.setValue(getGelarOperasi().getTwTimezone().toString());
			// tw akhir
			twAkhirTahunTextbox.setValue(getGelarOperasi().getTahunAkhirOps());
			twAkhirTanggalJamTextbox.setValue(getGelarOperasi().getTwAkhir());
			twAkhirTimeZoneTextbox.setValue(getGelarOperasi().getTwTimezone().toString());
			// disposisi
			comboitem = disposisiCombobox.getSelectedItem();
			comboitem.setLabel(getGelarOperasi().getDisposisi());
			// kotamaops
			GelarOperasi gelarOperasiKotamaopsByProxy = 
					getGelarOperasiDao().findGelarOperasiKotamaopsByProxy(getGelarOperasi().getId());
			for (Comboitem kotamaopsItem : kotamaopsCombobox.getItems()) {
				if (gelarOperasiKotamaopsByProxy.getKotamaops().getKotamaopsName().compareTo(kotamaopsItem.getLabel())==0) {
					kotamaopsCombobox.setSelectedItem(kotamaopsItem);
					break;
				}
			}
			satuanTextbox.setValue(getGelarOperasi().getSatuan());
			brigadeTextbox.setValue(getGelarOperasi().getBrigade());
			batalyonTextbox.setValue(getGelarOperasi().getBatalyon());
			kvTextbox.setValue(getGelarOperasi().getKv());
			komandanTextbox.setValue(getGelarOperasi().getKomandan());
			misiTextbox.setValue(getGelarOperasi().getMisi());
			jumlahTextbox.setValue(getGelarOperasi().getJumlahPersonil());
			
		}
	}

	public void onClick$saveButton(Event event) throws Exception {
		Comboitem comboitem;
		// get the mod data
		GelarOperasi modGelOperasi = getGelarOperasi();
		if (modGelOperasi.getId().compareTo(Long.MIN_VALUE)==0) {
			modGelOperasi.setCreatedAt(asDate(getCurrentLocalDateTime()));
			modGelOperasi.setEditedAt(asDate(getCurrentLocalDateTime()));
			// docSerialNum
			modGelOperasi.setSerialNumber(
					(DocumentSerialNumber) kejadianIdTextbox.getAttribute("docSerialNum"));
		} else {
			modGelOperasi.setEditedAt(asDate(getCurrentLocalDateTime()));
		}
		// twAwal
		modGelOperasi.setTwAwalOps(asDate(getAwalLocalDateTime()));
		modGelOperasi.setTahunAwalOps(twAwalTahunTextbox.getValue());
		modGelOperasi.setTwAwal(twAwalTanggalJamTextbox.getValue());
		// twAkhir
		modGelOperasi.setTwAkhirOps(asDate(getAkhirLocalDateTime()));
		modGelOperasi.setTahunAkhirOps(twAkhirTahunTextbox.getValue());
		modGelOperasi.setTwAkhir(twAkhirTanggalJamTextbox.getValue());
		// twTimezone -- from kotamaops
		modGelOperasi.setTwTimezone(getKotamaops().getTimeZone());
		// disposisi
		comboitem = disposisiCombobox.getSelectedItem();
		modGelOperasi.setDisposisi(comboitem.getLabel());
		
		modGelOperasi.setSatuan(satuanTextbox.getValue());
		modGelOperasi.setBrigade(brigadeTextbox.getValue());
		modGelOperasi.setBatalyon(batalyonTextbox.getValue());
		
		// kotamaops
		comboitem = kotamaopsCombobox.getSelectedItem();
		modGelOperasi.setKotamaops(comboitem.getValue());
		
		// disposisi:pasukan
		modGelOperasi.setKv(kvTextbox.getValue());
		modGelOperasi.setKomandan(komandanTextbox.getValue());
		modGelOperasi.setMisi(misiTextbox.getValue());
		modGelOperasi.setJumlahPersonil(jumlahTextbox.getValue());
		
		// send event
		Events.sendEvent(Events.ON_OK, gelarOperasiDialogWin, modGelOperasi);
		
		gelarOperasiDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		
		gelarOperasiDialogWin.detach();
	}

	private DocumentSerialNumber createSerialNumber(LocalDateTime currentDateTime, String documentCode) throws Exception {
		// get the serial number
		int serNum = getSerialNumberGenerator().getSerialNumber(documentCode, getZoneId(), currentDateTime);

		// create a new object
		DocumentSerialNumber serialNumber = new DocumentSerialNumber();
		serialNumber.setDocumentCode(documentCode);
		serialNumber.setCreatedAt(asDate(getCurrentLocalDateTime()));
		serialNumber.setEditedAt(asDate(getCurrentLocalDateTime()));
		serialNumber.setSerialDate(asDate(currentDateTime));
		serialNumber.setSerialNo(serNum);
		serialNumber.setSerialComp(formatSerialComp(documentCode, currentDateTime, serNum));
		
		return serialNumber;
	}	
	
	public GelarOperasiData getGelarOperasiData() {
		return gelarOperasiData;
	}

	public void setGelarOperasiData(GelarOperasiData gelarOperasiData) {
		this.gelarOperasiData = gelarOperasiData;
	}

	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}

	public LocalDateTime getCurrentLocalDateTime() {
		return currentLocalDateTime;
	}

	public void setCurrentLocalDateTime(LocalDateTime currentLocalDateTime) {
		this.currentLocalDateTime = currentLocalDateTime;
	}

	public ZoneId getZoneId() {
		return zoneId;
	}

	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	public KotamaopsDao getKotamaopsDao() {
		return kotamaopsDao;
	}

	public void setKotamaopsDao(KotamaopsDao kotamaopsDao) {
		this.kotamaopsDao = kotamaopsDao;
	}

	public SerialNumberGenerator getSerialNumberGenerator() {
		return serialNumberGenerator;
	}

	public void setSerialNumberGenerator(SerialNumberGenerator serialNumberGenerator) {
		this.serialNumberGenerator = serialNumberGenerator;
	}

	public GelarOperasi getGelarOperasi() {
		return gelarOperasi;
	}

	public void setGelarOperasi(GelarOperasi gelarOperasi) {
		this.gelarOperasi = gelarOperasi;
	}

	public LocalDateTime getAwalLocalDateTime() {
		return awalLocalDateTime;
	}

	public void setAwalLocalDateTime(LocalDateTime awalLocalDateTime) {
		this.awalLocalDateTime = awalLocalDateTime;
	}

	public LocalDateTime getAkhirLocalDateTime() {
		return akhirLocalDateTime;
	}

	public void setAkhirLocalDateTime(LocalDateTime akhirLocalDateTime) {
		this.akhirLocalDateTime = akhirLocalDateTime;
	}

	public boolean isTwAwalAkhirProper() {
		return twAwalAkhirProper;
	}

	public void setTwAwalAkhirProper(boolean twAwalAkhirProper) {
		this.twAwalAkhirProper = twAwalAkhirProper;
	}

	public GelarOperasiDao getGelarOperasiDao() {
		return gelarOperasiDao;
	}

	public void setGelarOperasiDao(GelarOperasiDao gelarOperasiDao) {
		this.gelarOperasiDao = gelarOperasiDao;
	}
	
}
