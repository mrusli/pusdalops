package mil.pusdalops.webui.kronologis;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.persistence.kejadian.dao.KejadianDao;
import mil.pusdalops.persistence.settings.dao.SettingsDao;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.dialogs.DatetimeData;

public class KejadianKronologisListInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9105564146275854503L;

	private SettingsDao settingsDao;
	private KejadianDao kejadianDao;
	
	private Window kejadianKronologisListInfoWin;
	private Label formTitleLabel;
	private Textbox twAwalTahunTextbox, twAwalTanggalJamTextbox, twAwalTimeZoneTextbox,
		twAkhirTahunTextbox, twAkhirTanggalJamTextbox, twAkhirTimeZoneTextbox,
		searchKronologisTextbox;
	private Tabbox kejadianPeriodTabbox;
	private Listbox kejadianListbox;
	
	private Settings settings;
	private Kotamaops kotamaops;
	private ZoneId zoneId;
	private LocalDateTime currentLocalDateTime, awalLocalDateTime, akhirLocalDateTime;
	private boolean twAwalAkhirProper;
	
	private final long SETTINGS_DEFAULT_ID = 1L;
	private final int MAX_WORD = 40;
	private static final Logger log = Logger.getLogger(KejadianKronologisListInfoControl.class);
	
	public void onCreate$kejadianKronologisListInfoWin(Event event) throws Exception {
		log.info("Createing Kejadian Kronologis Control...");
		setSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		setKotamaops(
				getSettings().getSelectedKotamaops());
		formTitleLabel.setValue(
				"Data Input | Kejadian Kronologis - Kotamaops: "+
				getKotamaops().getKotamaopsName());

		// set the timezone for this kotamaops
		int timezoneOrdinal = getKotamaops().getTimeZone().getValue();
		setZoneId(getKotamaops().getTimeZone().toZoneId(timezoneOrdinal));					

		// set the current localdatetime
		setCurrentLocalDateTime(getLocalDateTime(getZoneId()));
		
		// set tw with current time -- 'semua' tab -- minus 360 days
		setTwAwalAkhir(360L);
		
		// create indexer
		// getKejadianDao().createIndexer();
	}

	public void onSelect$kejadianPeriodTabbox(Event event) throws Exception {
		switch (kejadianPeriodTabbox.getSelectedIndex()) {
			case 0: setTwAwalAkhir(360L);
				break;
			case 1: setTwAwalAkhir(0L);
				break;
			case 2: setTwAwalAkhir(7L);
				break;
			case 3: setTwAwalAkhir(30L);
				break;
			default:
				break;
		}
	}

	private void setTwAwalAkhir(Long minDays) {
		// LocalDate twAwalDate = asLocalDate(getCurrentLocalDateTime().minusDays(minDays));
		// LocalTime twNoTime = LocalTime.of(0, 0, 0);

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
				"/dialogs/DatetimeWinDialog.zul", kejadianKronologisListInfoWin, args);
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
				"/dialogs/DatetimeWinDialog.zul", kejadianKronologisListInfoWin, args);
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
	
	public void onClick$searchKronologisButton(Event event) throws Exception {
		String searchString = searchKronologisTextbox.getValue();
		List<Kejadian> kejadianList = getKejadianDao().searchKronologis(searchString);
		
		kejadianListbox.setModel(new ListModelList<Kejadian>(kejadianList));
		kejadianListbox.setItemRenderer(getKejadianKronologisListitemRenderer());
	}
	
	private ListitemRenderer<Kejadian> getKejadianKronologisListitemRenderer() {
		
		return new ListitemRenderer<Kejadian>() {
			
			@Override
			public void render(Listitem item, Kejadian kejadian, int index) throws Exception {
				Listcell lc;
				
				// Kronologis
				lc = kejadianKronologis(new Listcell(), kejadian);
				lc.setParent(item);
				
			}

			private Listcell kejadianKronologis(Listcell listcell, Kejadian kejadian) throws Exception {
				Vlayout vlayout = new Vlayout();
				Label headlineLabel, kronologisLabel, wilayahLabel;
				int words;
			
				// headline
				headlineLabel = new Label(getHeadline(kejadian));
				headlineLabel.setStyle("font-size: 20px; color: blue;");
				headlineLabel.setParent(vlayout);
				
				// count the number of words in the kronologis
				words = countWords(kejadian.getKronologis());
				// limit the words
				String limitSentence = words > MAX_WORD ? 
						limitWords(kejadian.getKronologis())+"..." : 
							kejadian.getKronologis();
				
				// TW
				// asLocalDateTime(kejadian.getTwKejadianDateTime())
				String yearStr = getLocalDateTimeString(asLocalDateTime(kejadian.getTwKejadianDateTime()), "YYYY");
				String monthStr = getLocalDateTimeString(asLocalDateTime(kejadian.getTwKejadianDateTime()), "MM");
				String dayStr = getLocalDateTimeString(asLocalDateTime(kejadian.getTwKejadianDateTime()), "dd");
				String hourStr = getLocalDateTimeString(asLocalDateTime(kejadian.getTwKejadianDateTime()), "hh");
				String minuteStr = getLocalDateTimeString(asLocalDateTime(kejadian.getTwKejadianDateTime()), "mm");
				String twKejadian = yearStr+"/"+monthStr+dayStr+"."+hourStr+minuteStr;		
				
				// kronologis
				kronologisLabel = new Label("["+twKejadian+"] "+limitSentence);
				kronologisLabel.setStyle("font-size: 12px;");
				kronologisLabel.setParent(vlayout);

				// wilayah
				wilayahLabel = new Label(getWilayahKronologis(kejadian));
				wilayahLabel.setStyle("font-size: 12px; color:grey;");
				wilayahLabel.setParent(vlayout);

				
				vlayout.setParent(listcell);
				
				return listcell;
			}

			private String getHeadline(Kejadian kejadian) {
				// jenis kejadian - keterangan pelaku - sasaran
				String jenisKejadian = kejadian.getJenisKejadian().getNamaJenis();
				// String ketPelaku = kejadian.getKeteranganPelaku();
				String sasaran = kejadian.getSasaran();
				
				return jenisKejadian +" | "+ sasaran;
			}

			private String getWilayahKronologis(Kejadian kejadian) throws Exception {
				// kotamaops >> propinsi >> kabupaten/kotamadya >> kecamatan >> kelurahan
				Kejadian kejadianKotamaopsByProxy = getKejadianDao().findKejadianKotamaopsByProxy(kejadian.getId());
				Kejadian kejadianPropinsiByProxy = getKejadianDao().findKejadianPropinsiByProxy(kejadian.getId());
				Kejadian kejadianKabupatenKotByProxy = getKejadianDao().findKejadianKabupatenByProxy(kejadian.getId());
				Kejadian kejadianKecamatanByProxy = getKejadianDao().findKejadianKecamatanByProxy(kejadian.getId());
				Kejadian kejadianKelurahanByProxy = getKejadianDao().findKejadianKelurahanByProxy(kejadian.getId());
				String wilayah = "Dilaporkan dari: "+
						kejadianKotamaopsByProxy.getKotamaops().getKotamaopsName()+
						" | "+
						kejadianPropinsiByProxy.getPropinsi().getNamaPropinsi()+" > "+
						kejadianKabupatenKotByProxy.getKabupatenKotamadya().getNamaKabupaten()+" > "+
						kejadianKecamatanByProxy.getKecamatan().getNamaKecamatan()+" > "+
						kejadianKelurahanByProxy.getKelurahan().getNamaKelurahan();
				return wilayah;
			}
		};
	}
	
	private int countWords(String sentence) {
		if (sentence == null || sentence.isEmpty()) {
			return 0;
		}
		StringTokenizer tokens = new StringTokenizer(sentence);
		
		return tokens.countTokens();
	}
	
	private String limitWords(String sentence) {
		if (sentence == null || sentence.isEmpty()) {
			return null;
		}		
		StringTokenizer tokens = new StringTokenizer(sentence);
		String limSent = "";
		int w = 0;
		while (tokens.hasMoreElements() && w<MAX_WORD) {
			limSent = limSent+" "+tokens.nextToken();
			w++;
		}
		return limSent;
	}

	public SettingsDao getSettingsDao() {
		return settingsDao;
	}

	public void setSettingsDao(SettingsDao settingsDao) {
		this.settingsDao = settingsDao;
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

	public KejadianDao getKejadianDao() {
		return kejadianDao;
	}

	public void setKejadianDao(KejadianDao kejadianDao) {
		this.kejadianDao = kejadianDao;
	}
}
