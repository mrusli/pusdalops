package mil.pusdalops.webui.kronologis;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kejadian.KejadianJenis;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.persistence.kejadian.dao.KejadianDao;
import mil.pusdalops.persistence.kotamaops.dao.KotamaopsDao;
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
	private KotamaopsDao kotamaopsDao;
	
	private Window kejadianKronologisListInfoWin;
	private Label formTitleLabel, kotamaPropsLabel;
	private Textbox twAwalTahunTextbox, twAwalTanggalJamTextbox, twAwalTimeZoneTextbox,
		twAkhirTahunTextbox, twAkhirTanggalJamTextbox, twAkhirTimeZoneTextbox,
		searchKronologisTextbox;
	private Listbox kejadianListbox;
	private Combobox kotamaPropsCombobox, jenisKejadianCombobox;
	private Button filterKotamaopsButton, filterPropinsiButton;
	
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
		
		// determine which filter button to activate
		setFilter();
		
		// set tw with current time -- 'semua' tab -- minus 360 days
		// setTwAwalAkhir(360L);
		
		// create indexer
		getKejadianDao().createIndexer();
		
		// empty message
		kejadianListbox.setEmptyMessage("Pencarian Kronologis Tidak Ditemukan");
	}

	private void setFilter() {
		kotamaPropsLabel.setValue(getKotamaops().getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0 ?
				"Kotamaops:" : "Propinsi:");
		
		filterKotamaopsButton.setVisible(
				getKotamaops().getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0);
		filterPropinsiButton.setVisible(
				getKotamaops().getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)!=0);
	}

	private void setTwAwalAkhir(LocalDateTime twAwal, LocalDateTime twAkhir) {
		// LocalDate twAwalDate = asLocalDate(getCurrentLocalDateTime().minusDays(minDays));
		// LocalTime twNoTime = LocalTime.of(0, 0, 0);

		twAwalTahunTextbox.setValue(twAwal.format(DateTimeFormatter.ofPattern("YYYY", getLocale())));
		twAwalTanggalJamTextbox.setValue(
				twAwal.format(DateTimeFormatter.ofPattern("MM", getLocale()))+
				twAwal.format(DateTimeFormatter.ofPattern("dd", getLocale()))+".0000");
		twAwalTimeZoneTextbox.setValue(getKotamaops().getTimeZone().toString());

		// set awal
		setAwalLocalDateTime(twAwal);
		
		// akhir
		twAkhirTahunTextbox.setValue(getLocalDateTimeString(twAkhir, "YYYY"));
		twAkhirTanggalJamTextbox.setValue(
				getLocalDateTimeString(twAkhir, "MM")+
				getLocalDateTimeString(twAkhir, "dd")+".0000");
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
		// reset filter
		resetFilter();
		
		String searchString = searchKronologisTextbox.getValue();
		
		if (searchString.isEmpty()) {
			throw new Exception("Pencarian Tidak Dapat Dilakukan");
		}
		
		List<Kejadian> kejadianList = null;
		if (getKotamaops().getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0) {
			// get all the kotamaops under pusdalops (pusat)
			Kotamaops kotamaopsKotamaopsByProxy = 
					getKotamaopsDao().findKotamaopsKotamaopsByProxy(getKotamaops().getId());
			List<Kotamaops> kotamaopsList = kotamaopsKotamaopsByProxy.getKotamaops();

			kejadianList = getKejadianDao().searchKronologis(searchString, kotamaopsList);
			
			setKotamaopsFilter(kotamaopsList);
			
			setKejadianJenisFilter(kejadianList);
		} else {
			// get all the propinsis under this kotamaops
			Kotamaops kotamaopsPropinsisByProxy =
					getKotamaopsDao().findKotamaopsPropinsiByProxy(getKotamaops().getId());
			List<Propinsi> propinsiList = kotamaopsPropinsisByProxy.getPropinsis();
			
			kejadianList = getKejadianDao().searchKronologisByPropinsiList(searchString, propinsiList);
			
			setPropinsisFilter(propinsiList);
			
			setKejadianJenisFilter(kejadianList);
		}
				
		kejadianList.sort(Comparator.comparing(Kejadian::getTwKejadianDateTime));
		
		kejadianListbox.setModel(new ListModelList<Kejadian>(kejadianList));
		kejadianListbox.setItemRenderer(getKejadianKronologisListitemRenderer());
		
		if (!kejadianList.isEmpty()) {
			Kejadian kejAwal = kejadianList.get(0);
			LocalDateTime twAwal = asLocalDateTime(kejAwal.getTwKejadianDateTime());
			
			log.info("twAwal: "+twAwal.toString());
			
			Kejadian kejAkhir = kejadianList.get(kejadianList.size()-1);
			LocalDateTime twAkhir = asLocalDateTime(kejAkhir.getTwKejadianDateTime());
			
			log.info("twAkhir: "+twAkhir.toString());
			
			setTwAwalAkhir(twAwal, twAkhir);			
		}
	}

	private void resetFilter() {
		twAwalTahunTextbox.setValue("");
		twAwalTanggalJamTextbox.setValue("");
		twAwalTimeZoneTextbox.setValue("");
		
		twAkhirTahunTextbox.setValue("");
		twAkhirTanggalJamTextbox.setValue("");
		twAkhirTimeZoneTextbox.setValue("");
		
		kotamaPropsCombobox.getItems().clear();	
		
		jenisKejadianCombobox.getItems().clear();
		
	}

	private void setKotamaopsFilter(List<Kotamaops> kotamaopsList) {
		Comboitem comboitem;
		
		comboitem = new Comboitem();
		comboitem.setLabel("--semua--");
		comboitem.setValue(null);
		comboitem.setParent(kotamaPropsCombobox);
		
		// can filter by kotamaops
		kotamaPropsLabel.setValue("Kotamaops:");
		for (Kotamaops kotamaops : kotamaopsList) {
			comboitem = new Comboitem();
			comboitem.setLabel(kotamaops.getKotamaopsName());
			comboitem.setValue(kotamaops);
			comboitem.setParent(kotamaPropsCombobox);
		}
		
		kotamaPropsCombobox.setSelectedIndex(0);
	}
	
	private void setPropinsisFilter(List<Propinsi> propinsiList) {
		Comboitem comboitem;
		
		comboitem = new Comboitem();
		comboitem.setLabel("--semua--");
		comboitem.setValue(null);
		comboitem.setParent(kotamaPropsCombobox);
		
		// can filter by propinsi
		kotamaPropsLabel.setValue("Propinsi:");
		for (Propinsi propinsi : propinsiList) {
			comboitem = new Comboitem();
			comboitem.setLabel(propinsi.getNamaPropinsi());
			comboitem.setValue(propinsi);
			comboitem.setParent(kotamaPropsCombobox);
		}
		
		kotamaPropsCombobox.setSelectedIndex(0);
	}	
	
	private void setKejadianJenisFilter(List<Kejadian> kejadianList) {
		Set<KejadianJenis> kejJenisSet = new HashSet<KejadianJenis>();
		KejadianJenis kejJenis = null;
		Comboitem comboitem;

		for (Kejadian kejadian : kejadianList) {
			kejJenis = kejadian.getJenisKejadian();
			
			kejJenisSet.add(kejJenis);
		}
		
		comboitem = new Comboitem();
		comboitem.setLabel("--semua--");
		comboitem.setValue(null);
		comboitem.setParent(jenisKejadianCombobox);
		
		for (KejadianJenis kejadianJenis : kejJenisSet) {
			comboitem = new Comboitem();
			comboitem.setLabel(kejadianJenis.getNamaJenis());
			comboitem.setValue(kejadianJenis);
			comboitem.setParent(jenisKejadianCombobox);
		}
		
		jenisKejadianCombobox.setSelectedIndex(0);
		// kejJenisSet.forEach((jenisKej)->log.info(jenisKej.getNamaJenis()));
		
	}	
	
	
	public void onClick$filterKotamaopsButton(Event event) throws Exception {
		List<Kejadian> kejadianList = null;
		
		if (!isTwAwalAkhirProper()) {
			throw new Exception("TW Awal dan Akhir BELUM sesuai");
		}
		
		if (kotamaPropsCombobox.getSelectedItem()!=null) {
			String searchString = searchKronologisTextbox.getValue();

			if (kotamaPropsCombobox.getSelectedItem().getValue()==null) {
				// get all the kotamaops under pusdalops (pusat)
				Kotamaops kotamaopsKotamaopsByProxy = 
						getKotamaopsDao().findKotamaopsKotamaopsByProxy(getKotamaops().getId());
				List<Kotamaops> kotamaopsList = kotamaopsKotamaopsByProxy.getKotamaops();
				// search
				kejadianList = getKejadianDao().searchKronologis(searchString, kotamaopsList, asDate(getAwalLocalDateTime()), asDate(getAkhirLocalDateTime()));

			} else {
				// get the selected kotamaops
				Kotamaops selKotamaops = kotamaPropsCombobox.getSelectedItem().getValue();
				// search
				kejadianList = getKejadianDao().searchKronologis(searchString, selKotamaops, asDate(getAwalLocalDateTime()), asDate(getAkhirLocalDateTime()));
			}
			
			kejadianList.sort(Comparator.comparing(Kejadian::getTwKejadianDateTime));
			
			kejadianListbox.setModel(new ListModelList<Kejadian>(kejadianList));
			kejadianListbox.setItemRenderer(getKejadianKronologisListitemRenderer());
		} 
		
	}

	public void onClick$filterPropinsiButton(Event event) throws Exception {
		List<Kejadian> kejadianList = null;
		
		if (!isTwAwalAkhirProper()) {
			throw new Exception("TW Awal dan Akhir BELUM sesuai");
		}

		if (kotamaPropsCombobox.getSelectedItem()!=null) {
			String searchString = searchKronologisTextbox.getValue();
			
			if (kotamaPropsCombobox.getSelectedItem().getValue()==null) {
				// get all the propinsi under this kotamaops
				Kotamaops kotamaopsPropinsiByProxy =
						getKotamaopsDao().findKotamaopsPropinsiByProxy(getKotamaops().getId());
				List<Propinsi> propinsiList = kotamaopsPropinsiByProxy.getPropinsis();
				// search
				kejadianList = getKejadianDao().searchKronologisByPropinsiList(searchString, propinsiList, asDate(getAwalLocalDateTime()), asDate(getAkhirLocalDateTime()));
				
			} else {
				// get the selected propinsi
				Propinsi selPropinsi = kotamaPropsCombobox.getSelectedItem().getValue();
				// search
				kejadianList = getKejadianDao().searchKronologisByPropinsi(searchString, selPropinsi, asDate(getAwalLocalDateTime()), asDate(getAkhirLocalDateTime()));
				
			}
			
			kejadianList.sort(Comparator.comparing(Kejadian::getTwKejadianDateTime));
			
			kejadianListbox.setModel(new ListModelList<Kejadian>(kejadianList));
			kejadianListbox.setItemRenderer(getKejadianKronologisListitemRenderer());			
		}
	}	
	
	public void onClick$filterJenisKejButton(Event event) throws Exception {
		List<Kejadian> kejadianList = null;
		
		if (!isTwAwalAkhirProper()) {
			throw new Exception("TW Awal dan Akhir BELUM sesuai");
		}

		if (jenisKejadianCombobox.getSelectedItem()!=null) {
			String searchString = searchKronologisTextbox.getValue();
			
			if (jenisKejadianCombobox.getSelectedItem().getValue()==null) {
				// get all the kejadian
				List<KejadianJenis> kejadianJenisList = new ArrayList<KejadianJenis>();
				for (Comboitem comboitem : jenisKejadianCombobox.getItems()) {
					if (comboitem.getValue()!=null) {
						kejadianJenisList.add(comboitem.getValue());						
					}
				}
				// search
				kejadianList = getKejadianDao().searchKronologisByKejadianList(searchString, kejadianJenisList, asDate(getAwalLocalDateTime()), asDate(getAkhirLocalDateTime()));
				
			} else {
				// get the selected kejadian
				KejadianJenis kejadianJenis = jenisKejadianCombobox.getSelectedItem().getValue();
				// search
				kejadianList = getKejadianDao().searchKronologisByKejadian(searchString, kejadianJenis, asDate(getAwalLocalDateTime()), asDate(getAkhirLocalDateTime()));
			}
			
			kejadianList.sort(Comparator.comparing(Kejadian::getTwKejadianDateTime));
			
			kejadianListbox.setModel(new ListModelList<Kejadian>(kejadianList));
			kejadianListbox.setItemRenderer(getKejadianKronologisListitemRenderer());
		}
		
		
	}
	
	private ListitemRenderer<Kejadian> getKejadianKronologisListitemRenderer() {
		
		return new ListitemRenderer<Kejadian>() {
			
			@Override
			public void render(Listitem item, Kejadian kejadian, int index) throws Exception {
				Listcell lc;
				
				// Kronologis
				lc = kejadianKronologis(new Listcell(), kejadian);
				lc.setSclass("autopaging-content-kronologis");
				lc.setParent(item);
				
				item.setValue(kejadian);
				
			}

			private Listcell kejadianKronologis(Listcell listcell, Kejadian kejadian) throws Exception {
				Vlayout vlayout = new Vlayout();
				Label headlineLabel, kronologisLabel, wilayahLabel;
				int words;
			
				// headline
				headlineLabel = new Label(getHeadline(kejadian));
				headlineLabel.setStyle("font-size: 20px; color: blue; white-space: nowrap;");
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
	
	public void onSelect$kejadianListbox(Event event) throws Exception {
		Kejadian selKejadian = kejadianListbox.getSelectedItem().getValue();
		
		Map<String, Kejadian> arg = Collections.singletonMap("selectedKejadian", selKejadian);
		Window kejadianViewDialogWin = (Window) Executions.createComponents(
				"/kronologis/KejadianKronologisViewDialog.zul", kejadianKronologisListInfoWin, arg);
		
		kejadianViewDialogWin.doModal();
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

	public KotamaopsDao getKotamaopsDao() {
		return kotamaopsDao;
	}

	public void setKotamaopsDao(KotamaopsDao kotamaopsDao) {
		this.kotamaopsDao = kotamaopsDao;
	}
}
