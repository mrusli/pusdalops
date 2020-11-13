package mil.pusdalops.webui.laporanrutin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kejadian.KejadianJenis;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.persistence.kejadian.jenis.dao.KejadianJenisDao;
import mil.pusdalops.persistence.kotamaops.dao.KotamaopsDao;
import mil.pusdalops.persistence.laporanrutin.dao.LaporanRutinDao;
import mil.pusdalops.persistence.settings.dao.SettingsDao;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.common.PrintUtil;
import mil.pusdalops.webui.dialogs.DatetimeData;

public class LaporanRutinInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5292407419977582805L;

	private SettingsDao settingsDao;
	private KotamaopsDao kotamaopsDao;
	private KejadianJenisDao kejadianJenisDao;
	private LaporanRutinDao laporanRutinDao;
	
	private Window laporanRutinInfoWin;
	private Label formTitleLabel, title01, title02, title03;
	private Vbox printVbox;
	private Textbox twAwalTahunTextbox, twAwalTanggalJamTextbox, twAwalTimeZoneTextbox,
		twAkhirTahunTextbox, twAkhirTanggalJamTextbox, twAkhirTimeZoneTextbox;
	private Combobox kotamaopsCombobox, jenisKejadianCombobox;
	private Grid kejadianGrid;
	private Button printButton;
	
	private Settings settings;
	private Kotamaops kotamaops;
	private LocalDateTime currentLocalDateTime, awalLocalDateTime, akhirLocalDateTime;
	private boolean twAwalAkhirProper;
	private ZoneId zoneId;
	private List<Kejadian> kejadianList;
	
	private final long SETTINGS_DEFAULT_ID = 1L;
	private static final Logger log = Logger.getLogger(LaporanRutinInfoControl.class);
	
	public void onCreate$laporanRutinInfoWin(Event event) throws Exception {
		log.info("Creating LaporanRutinControl...");
		setSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		setKotamaops(
				getSettings().getSelectedKotamaops());
		formTitleLabel.setValue("Laporan | Laporan Rutin - Kotamaops : "+
				getKotamaops().getKotamaopsName());

		// set the timezone for this kotamaops
		int timezoneOrdinal = getKotamaops().getTimeZone().ordinal();
		setZoneId(getKotamaops().getTimeZone().toZoneId(timezoneOrdinal));			
		
		setCurrentLocalDateTime(getLocalDateTime(getZoneId()));
		
		setupTwAwalAkhir();
		
		// load kotamaops
		loadKotamaopsCombobox();
		
		// load jenis kejadian
		loadKejadianJenis();
	}

	private void setupTwAwalAkhir() {
		// awal - date minus 90 days
		LocalDate twAwalDate = minusDate(90L, asLocalDate(getCurrentLocalDateTime()));
		LocalTime twNoTime = LocalTime.of(0, 0, 0);
		
		twAwalTahunTextbox.setValue(
				getLocalDateTimeString(asLocalDateTime(twAwalDate, twNoTime), "YYYY"));
		twAwalTanggalJamTextbox.setValue(
				getLocalDateTimeString(asLocalDateTime(twAwalDate, twNoTime), "MM")+
				getLocalDateTimeString(asLocalDateTime(twAwalDate, twNoTime), "dd")+".0000");
		twAwalTanggalJamTextbox.setAttribute(
				"twAwalLocalDateTime", asLocalDateTime(twAwalDate, twNoTime));
		
		twAwalTimeZoneTextbox.setValue(getKotamaops().getTimeZone().toString());

		// set awal
		setAwalLocalDateTime(asLocalDateTime(twAwalDate, twNoTime));
		
		// akhir
		twAkhirTahunTextbox.setValue(getLocalDateTimeString(getCurrentLocalDateTime(), "YYYY"));
		twAkhirTanggalJamTextbox.setValue(
				getLocalDateTimeString(getCurrentLocalDateTime(), "MM")+
				getLocalDateTimeString(getCurrentLocalDateTime(), "dd")+".0000");
		twAkhirTanggalJamTextbox.setAttribute(
				"twAkhirLocalDateTime", asLocalDateTime(getCurrentLocalDateTime().toLocalDate(), twNoTime));

		twAkhirTimeZoneTextbox.setValue(getKotamaops().getTimeZone().toString());
		
		// set akhir
		setAkhirLocalDateTime(asLocalDateTime(getCurrentLocalDateTime().toLocalDate(), twNoTime));
		
		setTwAwalAkhirProper(true);
	}

	private void loadKotamaopsCombobox() throws Exception {
		if (getKotamaops().getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0) {
			// populate the combobox with all the kotamaops, added to PUSDALOPS
			Kotamaops kotamaopsByProxy = 
					getKotamaopsDao().findKotamaopsKotamaopsByProxy(getKotamaops().getId());
			List<Kotamaops> kotamaopsList = kotamaopsByProxy.getKotamaops();

			Comboitem comboitem;
			// semua
			comboitem = new Comboitem();
			comboitem.setLabel("--semua--");
			comboitem.setValue(null);
			comboitem.setParent(kotamaopsCombobox);
			// kotamaops
			for (Kotamaops kotamaops : kotamaopsList) {
				comboitem = new Comboitem();
				comboitem.setLabel(kotamaops.getKotamaopsName());
				comboitem.setValue(kotamaops);
				comboitem.setParent(kotamaopsCombobox);
			}
			kotamaopsCombobox.setSelectedIndex(0);
			
			
		} else {
			// no more kotamaops selection
			Comboitem comboitem = new Comboitem();
			comboitem.setLabel(getKotamaops().getKotamaopsName());
			comboitem.setValue(getKotamaops());
			comboitem.setParent(kotamaopsCombobox);
			// set selected
			kotamaopsCombobox.setSelectedIndex(0);
			kotamaopsCombobox.setDisabled(true);			
		}
	}

	private void loadKejadianJenis() throws Exception {
		List<KejadianJenis> kejadianJenisList = getKejadianJenisDao().findAllKejadianJenis(true);
		
		Comboitem comboitem;
		comboitem = new Comboitem();
		comboitem.setLabel("--semua--");
		comboitem.setValue(null);
		comboitem.setParent(jenisKejadianCombobox);
		
		for (KejadianJenis kejadianJenis : kejadianJenisList) {
			comboitem = new Comboitem();
			comboitem.setLabel(kejadianJenis.getNamaJenis());
			comboitem.setValue(kejadianJenis);
			comboitem.setParent(jenisKejadianCombobox);
		}
		
		jenisKejadianCombobox.setSelectedIndex(0);
	}
	
	
	public void onClick$twAwalRubahButton(Event event) throws Exception {
		// create the data object
		DatetimeData datetimeData = new DatetimeData();
		// set the object
		datetimeData.setDialogWinTitle("Rubah TW Awal");
		datetimeData.setTimezoneInd(getKotamaops().getTimeZone());
		datetimeData.setZoneId(getZoneId());
		datetimeData.setLocalDateTime(
				(LocalDateTime) twAwalTanggalJamTextbox.getAttribute("twAwalLocalDateTime"));

		// pass to the dialog window
		Map<String, DatetimeData> args = Collections.singletonMap("datetimeData", datetimeData);
		Window datetimeWin = (Window) Executions.createComponents(
				"/dialogs/DatetimeWinDialog.zul", laporanRutinInfoWin, args);
		datetimeWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				DatetimeData datetimeData = (DatetimeData) event.getData();
				
				log.info(datetimeData.getLocalDateTime());
				setAwalLocalDateTime(datetimeData.getLocalDateTime());
				
				twAwalTahunTextbox.setValue(getLocalDateTimeString(datetimeData.getLocalDateTime(), "YYYY"));
				twAwalTanggalJamTextbox.setValue(
						getLocalDateTimeString(datetimeData.getLocalDateTime(), "MM")+
						getLocalDateTimeString(datetimeData.getLocalDateTime(), "dd")+".0000");
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
		datetimeData.setLocalDateTime(
				(LocalDateTime) twAkhirTanggalJamTextbox.getAttribute("twAkhirLocalDateTime"));

		// pass to the dialog window
		Map<String, DatetimeData> args = Collections.singletonMap("datetimeData", datetimeData);
		Window datetimeWin = (Window) Executions.createComponents(
				"/dialogs/DatetimeWinDialog.zul", laporanRutinInfoWin, args);
		datetimeWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				DatetimeData datetimeData = (DatetimeData) event.getData();
				
				log.info(datetimeData.getLocalDateTime());
				setAkhirLocalDateTime(datetimeData.getLocalDateTime());
				
				twAkhirTahunTextbox.setValue(getLocalDateTimeString(datetimeData.getLocalDateTime(), "YYYY"));
				twAkhirTanggalJamTextbox.setValue(
						getLocalDateTimeString(datetimeData.getLocalDateTime(), "MM")+
						getLocalDateTimeString(datetimeData.getLocalDateTime(), "dd")+".0000");
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
	
	public void onClick$executeButton(Event event) throws Exception {
		// is twAwalAkhirProper? -- if yes, get twAwal and twAkhir
		if (!isTwAwalAkhirProper()) {
			throw new Exception("TW Awal dan Akhir BELUM sesuai");
		}		

		title01.setValue("LAPORAN RUTIN KEJADIAN MENONJOL");
		
		Kotamaops selKotamaops;
		KejadianJenis selKejadianJenis;
		if ((kotamaopsCombobox.getSelectedItem().getValue()==null) && (jenisKejadianCombobox.getSelectedItem().getValue()==null)) {
			// select all kejadian without kotamaops and jenisKejadian
			setKejadianList(
					getLaporanRutinDao().findAllKejadian(asDate(getAwalLocalDateTime()), asDate(getAkhirLocalDateTime())));
			
			title02.setValue("KOTAMAOPS : SEMUA - JENIS KEJADIAN : SEMUA");
			
		} else if ((kotamaopsCombobox.getSelectedItem().getValue()!=null) && (jenisKejadianCombobox.getSelectedItem().getValue()==null)) {
			// select kejadian with selected kotamaops
			selKotamaops = kotamaopsCombobox.getSelectedItem().getValue();
			setKejadianList(
					getLaporanRutinDao().findAllKejadianByKotamaops(selKotamaops, asDate(getAwalLocalDateTime()), asDate(getAkhirLocalDateTime())));
			title02.setValue("KOTAMAOPS : "+selKotamaops.getKotamaopsName()+" - JENIS KEJADIAN : SEMUA");

		} else if ((kotamaopsCombobox.getSelectedItem().getValue()==null) && (jenisKejadianCombobox.getSelectedItem().getValue()!=null)) {
			// select kejadian with selected jenisKejadian
			selKejadianJenis = jenisKejadianCombobox.getSelectedItem().getValue();
			setKejadianList(
					getLaporanRutinDao().findAllKejadianByKejadianJenis(selKejadianJenis, asDate(getAwalLocalDateTime()), asDate(getAkhirLocalDateTime())));
			title02.setValue("KOTAMAOPS : SEMUA - JENIS KEJADIAN : "+selKejadianJenis.getNamaJenis().toUpperCase());
			
		} else {
			// select kejadian with selected kotamaops and selected jenisKejadian
			setKejadianList(
					getLaporanRutinDao().findAllKejadianByKotamaopsByKejadianJenis(
							kotamaopsCombobox.getSelectedItem().getValue(), jenisKejadianCombobox.getSelectedItem().getValue(),
							asDate(getAwalLocalDateTime()), asDate(getAkhirLocalDateTime())));
			selKotamaops = kotamaopsCombobox.getSelectedItem().getValue();
			selKejadianJenis = jenisKejadianCombobox.getSelectedItem().getValue();
			title02.setValue("KOTAMAOPS : "+selKotamaops.getKotamaopsName()+" - JENIS KEJADIAN : "+selKejadianJenis.getNamaJenis().toUpperCase());
		}		

		title03.setValue("PERIODE : TW "+getLocalDateTimeString(getAwalLocalDateTime(), "YYYY")+"/"+
			getLocalDateTimeString(getAwalLocalDateTime(),"MM")+
			getLocalDateTimeString(getAwalLocalDateTime(),"dd")+(".0000")+
			" S/D "+
			getLocalDateTimeString(getAkhirLocalDateTime(), "YYYY")+"/"+
			getLocalDateTimeString(getAkhirLocalDateTime(),"MM")+
			getLocalDateTimeString(getAkhirLocalDateTime(),"dd")+".0000");
		
		kejadianGrid.setModel(new ListModelList<Kejadian>(getKejadianList()));
		kejadianGrid.setRowRenderer(getKejadianRowRenderer());
		kejadianGrid.setEmptyMessage("TIDAK ADA DATA");
		
		printButton.setVisible(!getKejadianList().isEmpty());
	}
	
	private RowRenderer<Kejadian> getKejadianRowRenderer() {
		
		return new RowRenderer<Kejadian>() {
			
			// int pageNo = 1;
			// int rowNo = 0;			
			
			@Override
			public void render(Row row, Kejadian kejadian, int index) throws Exception {				
				Vlayout vlayout = new Vlayout();
				Hlayout hlayout = new Hlayout();

				Label titleLabel;
				
				// row number
				titleLabel = new Label("No.");
				titleLabel.setStyle("font-weight:bold;");
				titleLabel.setParent(hlayout);
				
				Label rowNumLabel = new Label(String.valueOf(index+1)+".");
				rowNumLabel.setParent(hlayout);
				
				// TW
				titleLabel = new Label("TW: ");
				titleLabel.setStyle("font-weight:bold;");
				titleLabel.setParent(hlayout);
				String twString = 
						getLocalDateTimeString(asLocalDateTime(kejadian.getTwKejadianDateTime()), "YYYY")+"/"+
						getLocalDateTimeString(asLocalDateTime(kejadian.getTwKejadianDateTime()), "MM")+
						getLocalDateTimeString(asLocalDateTime(kejadian.getTwKejadianDateTime()), "dd")+"."+
						getLocalDateTimeString(asLocalDateTime(kejadian.getTwKejadianDateTime()), "HH")+
						getLocalDateTimeString(asLocalDateTime(kejadian.getTwKejadianDateTime()), "mm");
				Label twLabel = new Label(twString);
				twLabel.setParent(hlayout);
			
				// Lokasi
				titleLabel = new Label("Kotamaops: ");
				titleLabel.setStyle("font-weight:bold;");
				titleLabel.setParent(hlayout);
				
				Kejadian kejadianKotamaopsByProxy = getLaporanRutinDao().findKejadianKotamaopsByProxy(kejadian.getId());
				Label kotamaopsLabel = new Label(kejadianKotamaopsByProxy.getKotamaops().getKotamaopsName());
				kotamaopsLabel.setParent(hlayout);
				
				titleLabel = new Label("Propinsi: ");
				titleLabel.setStyle("font-weight:bold;");
				titleLabel.setParent(hlayout);				
				
				Kejadian kejadianPropinsiByProxy = getLaporanRutinDao().findKejadianPropinsiByProxy(kejadian.getId());
				Label propinsiLabel = new Label(kejadianPropinsiByProxy.getPropinsi().getNamaPropinsi());
				propinsiLabel.setParent(hlayout);

				titleLabel = new Label("Kabupaten/Kotamadya: ");
				titleLabel.setStyle("font-weight:bold;");
				titleLabel.setParent(hlayout);				

				Kejadian kejadianKabupatenKotByProxy = getLaporanRutinDao().findKejadianKabupatenKotByProxy(kejadian.getId());
				Label kabupatenKot = new Label(kejadianKabupatenKotByProxy.getKabupatenKotamadya().getNamaKabupaten());
				kabupatenKot.setParent(hlayout);
				
				hlayout.setParent(vlayout);
				
				// kronologis
				Label kronologisTitleLabel = new Label("Kronologis: ");
				kronologisTitleLabel.setStyle("font-weight: bold;");
				kronologisTitleLabel.setParent(vlayout);
				
				Label kronologisLabel = new Label(kejadian.getKronologis());
				kronologisLabel.setParent(vlayout);
				row.appendChild(vlayout);

/*				if (rowNo<12) {
					rowNo++;
				} else {
					rowNo = 0;
					Label pageNoLabel = new Label(String.valueOf(pageNo));
					pageNoLabel.setParent(vlayout);
					vlayout.setStyle("page-break-after: always;");
					log.info("Page No: "+pageNo);
					pageNo++;
				}
*/
			}
		};
	}

	public void onClick$printButton(Event event) throws Exception {

		PrintUtil.print(printVbox);
	}
	
	public void onClick$resetButton(Event event) throws Exception {
		title01.setValue("");
		title02.setValue("");
		title03.setValue("");
		
		kejadianGrid.setModel(new ListModelList<Kejadian>());
		kejadianGrid.setRowRenderer(getKejadianRowRenderer());
		// kejadianGrid.setEmptyMessage("TIDAK ADA DATA");
		
		printButton.setVisible(false);
	}
	
	public SettingsDao getSettingsDao() {
		return settingsDao;
	}

	public void setSettingsDao(SettingsDao settingsDao) {
		this.settingsDao = settingsDao;
	}

	public KotamaopsDao getKotamaopsDao() {
		return kotamaopsDao;
	}

	public void setKotamaopsDao(KotamaopsDao kotamaopsDao) {
		this.kotamaopsDao = kotamaopsDao;
	}

	public KejadianJenisDao getKejadianJenisDao() {
		return kejadianJenisDao;
	}

	public void setKejadianJenisDao(KejadianJenisDao kejadianJenisDao) {
		this.kejadianJenisDao = kejadianJenisDao;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
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

	public LaporanRutinDao getLaporanRutinDao() {
		return laporanRutinDao;
	}

	public void setLaporanRutinDao(LaporanRutinDao laporanRutinDao) {
		this.laporanRutinDao = laporanRutinDao;
	}

	public List<Kejadian> getKejadianList() {
		return kejadianList;
	}

	public void setKejadianList(List<Kejadian> kejadianList) {
		this.kejadianList = kejadianList;
	}
}
