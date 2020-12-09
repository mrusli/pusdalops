package mil.pusdalops.webui.laporanrutin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.persistence.kejadian.dao.KejadianDao;
import mil.pusdalops.persistence.kejadian.jenis.dao.KejadianJenisDao;
import mil.pusdalops.persistence.kotamaops.dao.KotamaopsDao;
import mil.pusdalops.persistence.laporanrutin.dao.LaporanRutinDao;
import mil.pusdalops.persistence.settings.dao.SettingsDao;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.common.PrintUtil;
import mil.pusdalops.webui.dialogs.DatetimeData;

public class LaporanRutinInfoControl02 extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5010802710014380723L;

	private SettingsDao settingsDao;
	private KotamaopsDao kotamaopsDao;
	private KejadianJenisDao kejadianJenisDao; 
	private LaporanRutinDao laporanRutinDao;
	private KejadianDao kejadianDao;
	
	public Window laporanRutinInfoWin02;
	public Label formTitleLabel, title01, title02, title03, title04;
	public Textbox twAwalTahunTextbox, twAwalTanggalJamTextbox, twAwalTimeZoneTextbox,
		twAkhirTahunTextbox, twAkhirTanggalJamTextbox, twAkhirTimeZoneTextbox;
	public Column idUrtCol, idNoCol, twCol, uraiCol, jenKejCol;
	public Grid kejadianGrid;
	public Button printButton, resetButton;
	public Vbox printVbox;
	
	private Settings settings;
	private Kotamaops kotamaops;
	private ZoneId zoneId;
	private boolean twAwalAkhirProper;
	private LocalDateTime currentLocalDateTime, awalLocalDateTime, akhirLocalDateTime;
	
	public static final Logger log = Logger.getLogger(LaporanRutinInfoControl02.class);
	private final long SETTINGS_DEFAULT_ID = 1L;
	
	public void onCreate$laporanRutinInfoWin02(Event event) throws Exception {
		log.info("Creating Laporan Rutin...");
		setSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		setKotamaops(
				getSettings().getSelectedKotamaops());
		formTitleLabel.setValue("Laporan | Laporan Rutin - Kotamaops : "+
				getKotamaops().getKotamaopsName());
		
		// set the timezone for this kotamaops
		int timezoneOrdinal = getKotamaops().getTimeZone().ordinal();
		setZoneId(getKotamaops().getTimeZone().toZoneId(timezoneOrdinal));			

		// set current localdatetime
		setCurrentLocalDateTime(getLocalDateTime(getZoneId()));
		
		setupTwAwalAkhir();

		setReportTitle();
		
		setReportColumnTitle();
	}

	private void setReportTitle() {
		title01.setValue("DATA KEJADIAN MENONJOL");
		title02.setValue("BESERTA REKAPITULASI KERUGIAN PERSONEL AND MATERIIL TNI");
		title03.setValue("DI SELURUH KOTAMAOPS TNI");
		title04.setValue("PERIODE BULAN "+ (
				getLocalDateTimeString(akhirLocalDateTime, "MMMM")+" "+
				getLocalDateTimeString(akhirLocalDateTime, "YYYY")).toUpperCase());
	}

	private void setReportColumnTitle() {
		Label col01Label = new Label();
		col01Label.setPre(true);
		col01Label.setValue("No.\nURT");
		col01Label.setParent(idUrtCol);
		
		Label col02Label = new Label();
		col02Label.setValue("No.");
		col02Label.setParent(idNoCol);
		
		Label col03Label = new Label();
		col03Label.setValue("TW");
		col03Label.setParent(twCol);
		
		Label col04Label = new Label();
		col04Label.setValue("URAIAN KEJADIAN");
		col04Label.setParent(uraiCol);
		
		Label col05Label = new Label();
		col05Label.setPre(true);
		col05Label.setValue("JENIS\nKEJADIAN");
		col05Label.setParent(jenKejCol);
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
				"/dialogs/DatetimeWinDialog.zul", laporanRutinInfoWin02, args);
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
				"/dialogs/DatetimeWinDialog.zul", laporanRutinInfoWin02, args);
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
	
	private void setupTwAwalAkhir() {		
		LocalDate twAwalDate = getFirstdateOfTheMonth(asLocalDate(getCurrentLocalDateTime()));
		LocalTime twNoTime = LocalTime.of(0, 0, 0);
		
		twAwalTahunTextbox.setValue(
				getLocalDateTimeString(asLocalDateTime(twAwalDate, twNoTime), "YYYY"));
		twAwalTanggalJamTextbox.setValue(
				getLocalDateTimeString(asLocalDateTime(twAwalDate, twNoTime), "MM")+
				getLocalDateTimeString(asLocalDateTime(twAwalDate, twNoTime), "dd")+".0000");
		twAwalTimeZoneTextbox.setValue(getKotamaops().getTimeZone().toString());

		// set awal
		setAwalLocalDateTime(asLocalDateTime(twAwalDate, twNoTime));
		
		// akhir
		twAkhirTahunTextbox.setValue(getLocalDateTimeString(getCurrentLocalDateTime(), "YYYY"));
		twAkhirTanggalJamTextbox.setValue(
				getLocalDateTimeString(getCurrentLocalDateTime(), "MM")+
				getLocalDateTimeString(getCurrentLocalDateTime(), "dd")+".0000");
		twAkhirTimeZoneTextbox.setValue(getKotamaops().getTimeZone().toString());
		
		// set akhir
		setAkhirLocalDateTime(asLocalDateTime(getCurrentLocalDateTime().toLocalDate(), twNoTime));
		
		setTwAwalAkhirProper(true);
	}

	public void onClick$executeButton(Event event) throws Exception {
		List<Kejadian> kejadianList = null;
		if (getKotamaops().getKotamaopsType().equals(KotamaopsType.PUSDALOPS)) {
			Kotamaops kotamaopsKotamaopsByProxy = 
					getKotamaopsDao().findKotamaopsKotamaopsByProxy(getKotamaops().getId());
			List<Kotamaops> kotamaopsList = kotamaopsKotamaopsByProxy.getKotamaops();
			
			kejadianList = getLaporanRutinDao().findAllKejadianInKotamaops(kotamaopsList, 
					asDate(awalLocalDateTime), asDate(akhirLocalDateTime));			
		} else {
			kejadianList = getLaporanRutinDao().findAllKejadianByKotamaops(getKotamaops(),
					asDate(awalLocalDateTime), asDate(akhirLocalDateTime));
		}
		
		List<KejadianPrintData> kejadianPrintList = new ArrayList<KejadianPrintData>();

		KejadianPrintData kejadianPrintData;
		Kotamaops kotamaops;
		Kejadian kejadianKotamaopsByProxy;
		int noUrt = 1;
		int noKot = 1;
		if (!kejadianList.isEmpty()) {
			kejadianKotamaopsByProxy = 
					getKejadianDao().findKejadianKotamaopsByProxy(kejadianList.get(0).getId());
			
			kotamaops = kejadianKotamaopsByProxy.getKotamaops();
			kejadianPrintData = new KejadianPrintData();
			kejadianPrintData.setNamaKotamaops(kotamaops.getKotamaopsName());
			kejadianPrintData.setNoUrt(0);
			kejadianPrintData.setNoKejadianKotamaops(0);
			kejadianPrintData.setTw(null);
			kejadianPrintData.setUraianKejadian(null);
			kejadianPrintData.setJenisKejadian(null);
			
			kejadianPrintList.add(kejadianPrintData);
			
			for (Kejadian kejadian : kejadianList) {
				kejadianKotamaopsByProxy = 
						getKejadianDao().findKejadianKotamaopsByProxy(kejadian.getId());

				if (kejadianKotamaopsByProxy.getKotamaops().getKotamaopsName().compareTo(kotamaops.getKotamaopsName())==0) {
					kejadianPrintData = new KejadianPrintData();
					kejadianPrintData.setNamaKotamaops(kejadianKotamaopsByProxy.getKotamaops().getKotamaopsName());
					kejadianPrintData.setNoUrt(noUrt);
					kejadianPrintData.setNoKejadianKotamaops(noKot);
					kejadianPrintData.setTw(getLocalDateTimeString(asLocalDateTime(kejadian.getTwKejadianDateTime()), "MMdd.HHmm"));
					kejadianPrintData.setUraianKejadian(kejadian.getKronologis());
					kejadianPrintData.setJenisKejadian(kejadian.getJenisKejadian().getNamaJenis());
					
					kejadianPrintList.add(kejadianPrintData);
				} else {
					kotamaops = kejadianKotamaopsByProxy.getKotamaops();
					noKot = 1;
					
					kejadianPrintData = new KejadianPrintData();
					kejadianPrintData.setNamaKotamaops(kejadianKotamaopsByProxy.getKotamaops().getKotamaopsName());
					kejadianPrintData.setNoUrt(0);
					kejadianPrintData.setNoKejadianKotamaops(0);
					kejadianPrintData.setTw(null);
					kejadianPrintData.setUraianKejadian(null);
					kejadianPrintData.setJenisKejadian(null);
					
					kejadianPrintList.add(kejadianPrintData);
					
					kejadianPrintData = new KejadianPrintData();
					kejadianPrintData.setNamaKotamaops(kejadianKotamaopsByProxy.getKotamaops().getKotamaopsName());
					kejadianPrintData.setNoUrt(noUrt);
					kejadianPrintData.setNoKejadianKotamaops(noKot);
					kejadianPrintData.setTw(getLocalDateTimeString(asLocalDateTime(kejadian.getTwKejadianDateTime()), "MMdd.HHmm"));
					kejadianPrintData.setUraianKejadian(kejadian.getKronologis());
					kejadianPrintData.setJenisKejadian(kejadian.getJenisKejadian().getNamaJenis());
					
					kejadianPrintList.add(kejadianPrintData);
					
				}				
				log.info(kejadianPrintData.getNoUrt());
				noUrt++;
				noKot++;
				// Kejadian kejadianKotamaopsByProxy = 
				//		getKejadianDao().findKejadianKotamaopsByProxy(kejadian.getId());
				// log.info(kejadianKotamaopsByProxy.getKotamaops().getKotamaopsName());
			}			
		}
		
		// kejadianPrintList.forEach(kejadian->log.info(kejadian.toString()));
		
		kejadianGrid.setModel(new ListModelList<KejadianPrintData>(kejadianPrintList));
		kejadianGrid.setRowRenderer(getKejadianRowRenderer());
		
		printButton.setVisible(true);
	}
	
	private RowRenderer<KejadianPrintData> getKejadianRowRenderer() {
		
		return new RowRenderer<KejadianPrintData>() {
			
			@Override
			public void render(Row row, KejadianPrintData kejadianPrintData, int index) throws Exception {
				
				row.setSclass("kejadian-content");
				
				if (kejadianPrintData.getUraianKejadian()==null) {
					// display the kotamaops
					Cell kotamaopsCell = new Cell();
					kotamaopsCell.setSclass("kotamaopsCell");
					
					Label noUrtLabel = new Label();
					// noUrtLabel.setValue(String.valueOf(index+1)+".");
					noUrtLabel.setValue(kejadianPrintData.getNamaKotamaops());
					noUrtLabel.setParent(kotamaopsCell);
					
					kotamaopsCell.setColspan(5);
					kotamaopsCell.setStyle("text-align:left;");
					kotamaopsCell.setParent(row);					
				} else {
					// no urut
					Cell noUrtCell = new Cell();
					noUrtCell.setSclass("noUrtCell");
					
					Label noUrtLabel = new Label();
					// noUrtLabel.setValue(String.valueOf(index+1)+".");
					noUrtLabel.setValue(String.valueOf(kejadianPrintData.getNoUrt()));
					noUrtLabel.setParent(noUrtCell);
					
					noUrtCell.setParent(row);
					
					// no
					Cell noCell = new Cell();
					noCell.setSclass("noCell");
					
					Label noLabel = new Label();
					noLabel.setValue(String.valueOf(kejadianPrintData.getNoKejadianKotamaops()));
					noLabel.setParent(noCell);
					
					noCell.setParent(row);
					
					// tw
					Cell twCell = new Cell();
					twCell.setClass("twCell");
					
					Label twLabel = new Label();
					// twLabel.setValue(getLocalDateTimeString(asLocalDateTime(kejadian.getTwKejadianDateTime()), "MMdd.HHmm"));
					twLabel.setValue(kejadianPrintData.getTw());
					twLabel.setParent(twCell);
					
					twCell.setParent(row);
					
					// uraian kejadian
					
					Cell uraiCell = new Cell();
					uraiCell.setSclass("uraiCell");
					
					Label uraiLabel = new Label();
					// uraiLabel.setValue(kejadian.getKronologis());
					uraiLabel.setValue(kejadianPrintData.getUraianKejadian());
					uraiLabel.setParent(uraiCell);
					
					uraiCell.setParent(row);
					
					// jenis kejadian
					
					Cell jenKejCell = new Cell();
					jenKejCell.setSclass("jenKejCell");
					
					Label jenKejLabel = new Label();
					// jenKejLabel.setValue(kejadian.getJenisKejadian().getNamaJenis());
					jenKejLabel.setValue(kejadianPrintData.getJenisKejadian());
					jenKejLabel.setParent(jenKejCell);
					
					jenKejCell.setParent(row);
				}
			}
		};
	}

	public void onClick$resetButton(Event event) throws Exception {
		kejadianGrid.setModel(new ListModelList<Kejadian>());
		kejadianGrid.setRowRenderer(getKejadianRowRenderer());

		printButton.setVisible(false);
	}
	
	public void onClick$printButton(Event event) throws Exception {
		PrintUtil.print(printVbox);
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
	public LaporanRutinDao getLaporanRutinDao() {
		return laporanRutinDao;
	}
	public void setLaporanRutinDao(LaporanRutinDao laporanRutinDao) {
		this.laporanRutinDao = laporanRutinDao;
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

	public ZoneId getZoneId() {
		return zoneId;
	}

	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	public boolean isTwAwalAkhirProper() {
		return twAwalAkhirProper;
	}

	public void setTwAwalAkhirProper(boolean twAwalAkhirProper) {
		this.twAwalAkhirProper = twAwalAkhirProper;
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

	public KejadianDao getKejadianDao() {
		return kejadianDao;
	}

	public void setKejadianDao(KejadianDao kejadianDao) {
		this.kejadianDao = kejadianDao;
	}
}
