package mil.pusdalops.webui.siaga;

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

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.pejabat.Pejabat;
import mil.pusdalops.domain.serial.DocumentSerialNumber;
import mil.pusdalops.domain.siaga.PejabatSiaga;
import mil.pusdalops.persistence.pejabat.dao.PejabatDao;
import mil.pusdalops.persistence.pejabat.siaga.dao.PejabatSiagaDao;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.common.SerialNumberGenerator;
import mil.pusdalops.webui.dialogs.DatetimeData;

public class PejabatSiagaDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4224728637183613325L;

	private PejabatDao pejabatDao;
	private SerialNumberGenerator serialNumberGenerator;
	private PejabatSiagaDao pejabatSiagaDao;
	
	private Window pejabatSiagaDialogWin;
	private Textbox kejadianIdTextbox,
		twAwalTahunTextbox, twAwalTanggalJamTextbox, twAwalTimeZoneTextbox,
		twAkhirTahunTextbox, twAkhirTanggalJamTextbox, twAkhirTimeZoneTextbox,
		pangkatTextbox, jabatanTextbox, nrpTextbox;
	private Combobox namaCombobox;
	
	private PejabatSiagaData pejabatSiagaData;
	private PejabatSiaga pejabatSiaga;
	private Kotamaops kotamaops;	
	private LocalDateTime currentLocalDateTime, awalLocalDateTime, akhirLocalDateTime;
	private ZoneId zoneId;
	private boolean twAwalAkhirProper; 
	
	private static final Logger log = Logger.getLogger(PejabatSiagaDialogControl.class);
	
	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.GenericForwardComposer#doAfterCompose(org.zkoss.zk.ui.Component)
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setPejabatSiagaData(
				(PejabatSiagaData) arg.get("pejabatSiagaData"));
	}

	public void onCreate$pejabatSiagaDialogWin(Event event) throws Exception {
		log.info("PejabatSiagaDialogWin Create...");
		setPejabatSiaga(
				getPejabatSiagaData().getPejabatSiaga());
		setKotamaops(
				getPejabatSiagaData().getKotamaops());
		
		// set the timezone for this kotamaops
		int timezoneOrdinal = getKotamaops().getTimeZone().getValue();
		setZoneId(getKotamaops().getTimeZone().toZoneId(timezoneOrdinal));
		
		setCurrentLocalDateTime(getLocalDateTime(getZoneId()));
		
		// load nama pejabat -- from data pejabat
		loadNamaCombobox();
		
		// display data pejabat siaga 
		// -- data pejabat from selected pejabat
		displayDataPejabatSiaga();
	}
	
	private void loadNamaCombobox() throws Exception {
		Comboitem comboitem;		
		List<Pejabat> pejabatList = getPejabatDao().findAllPejabatByKotamaops(true, getKotamaops());
		for (Pejabat pejabat : pejabatList) {
			comboitem = new Comboitem();
			comboitem.setLabel(pejabat.getNama());
			comboitem.setValue(pejabat);
			comboitem.setParent(namaCombobox);
		}
	}

	public void onSelect$namaCombobox(Event event) throws Exception {
		Pejabat selectedPejabat = namaCombobox.getSelectedItem().getValue();
		
		if (selectedPejabat!=null) {
			pangkatTextbox.setValue(selectedPejabat.getPangkat());
			jabatanTextbox.setValue(selectedPejabat.getJabatan()); 
			nrpTextbox.setValue(selectedPejabat.getNrp());
		}
	}
	
	private void displayDataPejabatSiaga() throws Exception {
		if (getPejabatSiaga().getId().compareTo(Long.MIN_VALUE)==0) {
			// new
			
			// create ID
			String documentCode = getKotamaops().getDocumentCode();
			DocumentSerialNumber docSerialNum = createSerialNumber(getCurrentLocalDateTime(), documentCode);

			kejadianIdTextbox.setValue(docSerialNum.getSerialComp());
			kejadianIdTextbox.setAttribute("docSerialNum", docSerialNum);
			
			// set tw awal-akhir
			setTwAwalAkhir(3L);		

		} else {
			// existing -- display data
			kejadianIdTextbox.setValue(getPejabatSiaga().getSerialNumber().getSerialComp());
			kejadianIdTextbox.setAttribute("docSerialNum", getPejabatSiaga().getSerialNumber());
			// twAwal
			twAwalTahunTextbox.setValue(getPejabatSiaga().getTahunAwal());
			twAwalTanggalJamTextbox.setValue(getPejabatSiaga().getTwAwal());
			twAwalTimeZoneTextbox.setValue(getPejabatSiaga().getTwTimezone().toString());
			
			setAwalLocalDateTime(asLocalDateTime(getPejabatSiaga().getTwSiagaAwal()));
			
			// twAkhir
			twAkhirTahunTextbox.setValue(getPejabatSiaga().getTahunAkhir());
			twAkhirTanggalJamTextbox.setValue(getPejabatSiaga().getTwAkhir());
			twAkhirTimeZoneTextbox.setValue(getPejabatSiaga().getTwTimezone().toString());
			
			setAkhirLocalDateTime(asLocalDateTime(getPejabatSiaga().getTwSiagaAkhir()));
			
			// nama pejabat
			PejabatSiaga pejabatSiagaPejabatByProxy = getPejabatSiagaDao().findPejabatSiagaPejabatByProxy(getPejabatSiaga().getId());
			for (Comboitem comboitem : namaCombobox.getItems()) {
				if (pejabatSiagaPejabatByProxy.getPejabat().getNama().compareTo(comboitem.getLabel())==0) {
					namaCombobox.setSelectedItem(comboitem);
					break;
				}
			}
			Pejabat selPejabat = namaCombobox.getSelectedItem().getValue();
			if (selPejabat!=null) {
				pangkatTextbox.setValue(selPejabat.getPangkat());
				jabatanTextbox.setValue(selPejabat.getJabatan()); 
				nrpTextbox.setValue(selPejabat.getNrp());
			}			
		}
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
				"/dialogs/DatetimeWinDialog.zul", pejabatSiagaDialogWin, args);
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
				"/dialogs/DatetimeWinDialog.zul", pejabatSiagaDialogWin, args);
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

	
	public void onClick$saveButton(Event event) throws Exception {
		Comboitem comboitem;
		// get the changes
		PejabatSiaga modPejabatSiaga = getPejabatSiaga();
		if (modPejabatSiaga.getId().compareTo(Long.MIN_VALUE)==0) {
			modPejabatSiaga.setCreatedAt(asDate(getCurrentLocalDateTime()));
			modPejabatSiaga.setEditedAt(asDate(getCurrentLocalDateTime()));
			modPejabatSiaga.setSerialNumber(
					(DocumentSerialNumber)kejadianIdTextbox.getAttribute("docSerialNum"));
		} else {
			modPejabatSiaga.setEditedAt(asDate(getCurrentLocalDateTime()));
		}
		// twAwal
		modPejabatSiaga.setTahunAwal(twAwalTahunTextbox.getValue());
		modPejabatSiaga.setTwAwal(twAwalTanggalJamTextbox.getValue());
		modPejabatSiaga.setTwSiagaAwal(asDate(getAwalLocalDateTime()));
		// twAkhir
		modPejabatSiaga.setTahunAkhir(twAkhirTahunTextbox.getValue());
		modPejabatSiaga.setTwAkhir(twAkhirTanggalJamTextbox.getValue());
		modPejabatSiaga.setTwSiagaAkhir(asDate(getAkhirLocalDateTime()));
		// twTimezone
		modPejabatSiaga.setTwTimezone(getKotamaops().getTimeZone());
		// nama Pejabat
		comboitem = namaCombobox.getSelectedItem();
		modPejabatSiaga.setPejabat(comboitem.getValue());
		
		// send event ON_OK
		Events.sendEvent(Events.ON_OK, pejabatSiagaDialogWin, modPejabatSiaga);
		
		pejabatSiagaDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		
		pejabatSiagaDialogWin.detach();
	}

	private DocumentSerialNumber createSerialNumber(LocalDateTime currentDateTime, String documentCode) throws Exception {
		// get the serial number
		System.out.println(documentCode);
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
	
	public PejabatSiagaData getPejabatSiagaData() {
		return pejabatSiagaData;
	}

	public void setPejabatSiagaData(PejabatSiagaData pejabatSiagaData) {
		this.pejabatSiagaData = pejabatSiagaData;
	}

	public PejabatSiaga getPejabatSiaga() {
		return pejabatSiaga;
	}

	public void setPejabatSiaga(PejabatSiaga pejabatSiaga) {
		this.pejabatSiaga = pejabatSiaga;
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

	public ZoneId getZoneId() {
		return zoneId;
	}

	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	public SerialNumberGenerator getSerialNumberGenerator() {
		return serialNumberGenerator;
	}

	public void setSerialNumberGenerator(SerialNumberGenerator serialNumberGenerator) {
		this.serialNumberGenerator = serialNumberGenerator;
	}

	public PejabatDao getPejabatDao() {
		return pejabatDao;
	}

	public void setPejabatDao(PejabatDao pejabatDao) {
		this.pejabatDao = pejabatDao;
	}

	public PejabatSiagaDao getPejabatSiagaDao() {
		return pejabatSiagaDao;
	}

	public void setPejabatSiagaDao(PejabatSiagaDao pejabatSiagaDao) {
		this.pejabatSiagaDao = pejabatSiagaDao;
	}
}
