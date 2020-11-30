package mil.pusdalops.webui.rekap;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.chart.Charts;
import org.zkoss.chart.model.CategoryModel;
import org.zkoss.chart.model.DefaultCategoryModel;
import org.zkoss.chart.model.DefaultPieModel;
import org.zkoss.chart.model.PieModel;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kejadian.KejadianMotif;
import mil.pusdalops.domain.kejadian.KejadianPelaku;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.persistence.kejadian.motif.dao.KejadianMotifDao;
import mil.pusdalops.persistence.kejadian.pelaku.dao.KejadianPelakuDao;
import mil.pusdalops.persistence.kejadian.rekap.dao.KejadianRekapIntensitasDao;
import mil.pusdalops.persistence.kotamaops.dao.KotamaopsDao;
import mil.pusdalops.persistence.propinsi.dao.PropinsiDao;
import mil.pusdalops.persistence.settings.dao.SettingsDao;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.dialogs.DatetimeData;

public class KejadianIntensitasInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7024402727310837775L;

	private SettingsDao settingsDao;
	private KotamaopsDao kotamaopsDao;
	private PropinsiDao propinsiDao;
	private KejadianRekapIntensitasDao kejadianRekapIntensitasDao;
	private KejadianMotifDao kejadianMotifDao;
	private KejadianPelakuDao kejadianPelakuDao;
	
	private Window kejadianIntensitasInfoWin;
	private Label formTitleLabel;
	private Combobox kotamaopsCombobox, matraCombobox;
	private Grid kejadianPropinsiGrid;
	private Textbox twAwalTahunTextbox, twAwalTanggalJamTextbox, twAwalTimeZoneTextbox,
		twAkhirTahunTextbox, twAkhirTanggalJamTextbox, twAkhirTimeZoneTextbox;
	private Charts jenisKejadianChart, motifKejadianChart, pelakuKejadianChart,
		jenisKejadianPieChart, motifKejadianPieChart, pelakuKejadianPieChart;
	
	private Settings settings;
	private Kotamaops kotamaops;
	private ZoneId zoneId;
	private LocalDateTime currentLocalDateTime, awalLocalDateTime, akhirLocalDateTime;
	private boolean twAwalAkhirProper;
	
	private final long SETTINGS_DEFAULT_ID = 1L;
	private static final Logger log = Logger.getLogger(KejadianIntensitasInfoControl.class); 
	
	public void onCreate$kejadianIntensitasInfoWin(Event event) throws Exception {
		setSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		setKotamaops(
				getSettings().getSelectedKotamaops());
		
		formTitleLabel.setValue(
				"Rekapitualsi | Intensitas Kejadian - Kotamaops: "+
				getKotamaops().getKotamaopsName());
		
		// set the timezone for this kotamaops
		int timezoneOrdinal = getKotamaops().getTimeZone().getValue();
		setZoneId(getKotamaops().getTimeZone().toZoneId(timezoneOrdinal));					

		// set the current localdatetime
		setCurrentLocalDateTime(getLocalDateTime(getZoneId()));
		
		// set tw awal dan akhir
		setTwAwalAkhir(360L);
		
		// load matra combobox
		loadKotamaopsMatraTypeCombobox();
		
		// load kotamaops
		loadKotamaops();

		jenisKejadianChart.getExporting().setEnabled(false);
		jenisKejadianChart.getLegend().setEnabled(false);
		jenisKejadianChart.getYAxis().setTitle("");
		jenisKejadianChart.getPlotOptions().getSeries().setColorByPoint(true);
		
		jenisKejadianPieChart.getExporting().setEnabled(false);
		
		motifKejadianChart.getExporting().setEnabled(false);
		motifKejadianChart.getLegend().setEnabled(false);
		motifKejadianChart.getYAxis().setTitle("");
		motifKejadianChart.getPlotOptions().getSeries().setColorByPoint(true);
		
		motifKejadianPieChart.getExporting().setEnabled(false);
		
		pelakuKejadianChart.getExporting().setEnabled(false);
		pelakuKejadianChart.getYAxis().setTitle("");
		pelakuKejadianChart.getLegend().setEnabled(false);
		pelakuKejadianChart.getPlotOptions().getSeries().setColorByPoint(true);

		pelakuKejadianPieChart.getExporting().setEnabled(false);
	}

	private void setTwAwalAkhir(Long minDays) {
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
	
	private void loadKotamaopsMatraTypeCombobox() {
		Comboitem comboitem;
		if (getKotamaops().getKotamaopsType().equals(KotamaopsType.PUSDALOPS)) {
			// semua
			comboitem = new Comboitem();
			comboitem.setLabel("--Semua--");
			comboitem.setValue(null);
			comboitem.setParent(matraCombobox);
			// other matra
			for (KotamaopsType kotamaopsType : KotamaopsType.values()) {
				if (kotamaopsType.equals(KotamaopsType.PUSDALOPS)) {
					// do nothing
				} else {
					comboitem = new Comboitem();
					comboitem.setLabel(kotamaopsType.toString());
					comboitem.setValue(kotamaopsType);
					comboitem.setParent(matraCombobox);
				}
			}
			matraCombobox.setSelectedIndex(0);
		} else {
			// select the kotamaops matra dan disable the combobox
			comboitem = new Comboitem();
			comboitem.setLabel(getKotamaops().getKotamaopsType().toString());
			comboitem.setValue(getKotamaops().getKotamaopsType());
			comboitem.setParent(matraCombobox);
			
			matraCombobox.setSelectedItem(comboitem);
			matraCombobox.setDisabled(true);
		}
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
				"/dialogs/DatetimeWinDialog.zul", kejadianIntensitasInfoWin, args);
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
				"/dialogs/DatetimeWinDialog.zul", kejadianIntensitasInfoWin, args);
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

	private void loadKotamaops() throws Exception {
		Comboitem comboitem;
		if (getKotamaops().getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0) {
			// -- SEMUA -- 
			comboitem = new Comboitem();
			comboitem.setLabel("--Semua--");
			comboitem.setValue(null);
			comboitem.setParent(kotamaopsCombobox);
			
			// select --semua--
			kotamaopsCombobox.setSelectedIndex(0);
			
			// load other kotamaops under pusdalops
			Kotamaops kotamaopsKotamaopsByProxy = 
					getKotamaopsDao().findKotamaopsKotamaopsByProxy(getKotamaops().getId());
			List<Kotamaops> kotamaopsList = kotamaopsKotamaopsByProxy.getKotamaops();
			for (Kotamaops kotamaops : kotamaopsList) {
				comboitem = new Comboitem();
				comboitem.setLabel(kotamaops.getKotamaopsName());
				comboitem.setValue(kotamaops);
				comboitem.setParent(kotamaopsCombobox);				
			}
			displayPropinsi(
					getAllPropinsi());
		} else {
			comboitem = new Comboitem();
			comboitem.setLabel(getKotamaops().getKotamaopsName());
			comboitem.setValue(getKotamaops());
			comboitem.setParent(kotamaopsCombobox);		
			// select the kotamaops ONLY 
			kotamaopsCombobox.setSelectedIndex(0);
			// - and display all the propinsi under this kotamaops
			Kotamaops kotamaopsPropinsiByProxy = 
					getKotamaopsDao().findKotamaopsPropinsiByProxy(getKotamaops().getId());
			displayPropinsi(
					kotamaopsPropinsiByProxy.getPropinsis());
		}
		
	}

	public void onSelect$matraCombobox(Event event) throws Exception {
		Comboitem comboitem;
		
		// clear combobox
		kotamaopsCombobox.getItems().clear();			
		
		if (matraCombobox.getSelectedItem().getValue()==null) {
			// semua
			loadKotamaops();
		} else {
			// selected matra
			KotamaopsType selKotamaopsMatraType = matraCombobox.getSelectedItem().getValue();
			// semua
			comboitem = new Comboitem();
			comboitem.setLabel("--Semua--");
			comboitem.setValue(null);
			comboitem.setParent(kotamaopsCombobox);
			// load other kotamaops under pusdalops
			Kotamaops kotamaopsKotamaopsByProxy = 
					getKotamaopsDao().findKotamaopsKotamaopsByProxy(getKotamaops().getId());
			List<Kotamaops> kotamaopsList = kotamaopsKotamaopsByProxy.getKotamaops();

			List<Kotamaops> selMatraKotamaopsList = new ArrayList<Kotamaops>();
			for (Kotamaops kotamaops : kotamaopsList) {
				if (kotamaops.getKotamaopsType().equals(selKotamaopsMatraType)) {
					selMatraKotamaopsList.add(kotamaops);
				} 
			}
			
			for (Kotamaops kotamaops : selMatraKotamaopsList) {
				comboitem = new Comboitem();
				comboitem.setLabel(kotamaops.getKotamaopsName());
				comboitem.setValue(kotamaops);
				comboitem.setParent(kotamaopsCombobox);
			}			
			
			kotamaopsCombobox.setSelectedIndex(0);
			
			displayPropinsi(
					getAllPropinsi());

		}
	}
	
	public void onSelect$kotamaopsCombobox(Event event) throws Exception {
		// display all the propinsi under each kotamaops (when selected)
		if (kotamaopsCombobox.getSelectedItem().getValue()==null) {
			// --semua-- display all the propinsi under each kotamaops
			displayPropinsi(
					getAllPropinsi());
		} else {
			displayPropinsi(
					getAllPropinsi(
							kotamaopsCombobox.getSelectedItem().getValue()));
		}
		
	}
	
	private List<Propinsi> getAllPropinsi() throws Exception {
		List<Propinsi> allPropinsiList = new ArrayList<Propinsi>();

		for (Comboitem comboitem : kotamaopsCombobox.getItems()) {
			Kotamaops kotamaops = comboitem.getValue();
			if (kotamaops==null) {
				continue;
			}
			Kotamaops kotamaopsPropinsiByProxy = getKotamaopsDao().findKotamaopsPropinsiByProxy(kotamaops.getId());
			List<Propinsi> propinsiList = kotamaopsPropinsiByProxy.getPropinsis();
			for (Propinsi propinsi : propinsiList) {
				allPropinsiList.add(propinsi);
			}
		}
		
		return allPropinsiList;
	}
	
	private List<Propinsi> getAllPropinsi(Kotamaops kotamaops) throws Exception {
		List<Propinsi> allPropinsiList = new ArrayList<Propinsi>();

		Kotamaops kotamaopsPropinsiByProxy = getKotamaopsDao().findKotamaopsPropinsiByProxy(kotamaops.getId());
		List<Propinsi> propinsiList = kotamaopsPropinsiByProxy.getPropinsis();
		for (Propinsi propinsi : propinsiList) {
			allPropinsiList.add(propinsi);
		}
		
		return allPropinsiList;
	}
	
	
	private void displayPropinsi(List<Propinsi> propinsiList) {
		kejadianPropinsiGrid.setModel(new ListModelList<Propinsi>(propinsiList));
		kejadianPropinsiGrid.setRowRenderer(getPropinsiRowRenderer());
	}

	private RowRenderer<Propinsi> getPropinsiRowRenderer() {

		return new RowRenderer<Propinsi>() {
			
			@Override
			public void render(Row row, Propinsi propinsi, int index) throws Exception {
				Label label01, label02;
				
				label01 = new Label();
				label01.setValue(propinsi.getNamaPropinsi());
				label01.setSclass("h4");
				label01.setWidth("350px");

				row.appendChild(label01);
				
				label02 = new Label();
				label02.setValue(countKejadianByPropinsi(propinsi, getAwalLocalDateTime(), getAkhirLocalDateTime()));
				label02.setClass("badge");
				label02.setWidth("35px");

				row.appendChild(label02);
				
				row.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						log.info("row click: "+propinsi.getNamaPropinsi());
					}
				});
			}

			private String countKejadianByPropinsi(Propinsi propinsi, LocalDateTime twAwal, LocalDateTime twAkhir) {
				BigInteger kejCount = getKejadianRekapIntensitasDao().countKejadianByPropinsi(propinsi, 
						asDate(twAwal), asDate(twAkhir));
				
				return kejCount.toString();
			}
		};
	}
	
	public void onClick$executeButton(Event event) throws Exception {
		List<Kejadian> kejadianList; 
		List<KejadianJenisCount> kejadianJenisCountList;
		List<KejadianMotif> kejadianMotifList;
		List<KejadianMotifCount> kejadianMotifCountList;
		List<KejadianPelaku> kejadianPelakuList;
		List<KejadianPelakuCount> kejadianPelakuCountList;
		if (kotamaopsCombobox.getSelectedItem().getValue()==null) {
			// --semua--
			// find distinct kejadian
			// Kotamaops kotamaopsKotamaopsByProxy = 
			//		getKotamaopsDao().findKotamaopsKotamaopsByProxy(getKotamaops().getId());
			// List<Kotamaops> kotamaopsList = kotamaopsKotamaopsByProxy.getKotamaops();
			List<Kotamaops> kotamaopsList = new ArrayList<Kotamaops>();
			for (Comboitem comboitem : kotamaopsCombobox.getItems()) {
				if (comboitem.getValue()==null) {
					// do nothing
				} else {
					kotamaopsList.add(comboitem.getValue());
				}
			}
			
			kejadianList = 
					getKejadianRekapIntensitasDao().findInKotamaopsDistinctKejadianByJenisKejadian(
							kotamaopsList, asDate(getAwalLocalDateTime()), asDate(getAkhirLocalDateTime()));
			// count each kejadian  
			kejadianJenisCountList =
					getKejadianJenisCountList(kejadianList);
			
			// select all motif kejadian
			kejadianMotifList = getKejadianMotifDao().findAllKejadianMotif();
			kejadianMotifCountList = getKejadianMotifCountList(kotamaopsList, kejadianMotifList);
			
			// get all Pelaku kejadian
			kejadianPelakuList = getKejadianPelakuDao().findAllKejadianPelaku();
			kejadianPelakuCountList = getKejadianPelakuList(kotamaopsList, kejadianPelakuList);
		} else {
			// selected kotamaops
			Kotamaops selKotamaops = kotamaopsCombobox.getSelectedItem().getValue();
			kejadianList = 
					getKejadianRekapIntensitasDao().findDistinctKejadianByJenisKejadian(
							selKotamaops, 
							asDate(getAwalLocalDateTime()), asDate(getAkhirLocalDateTime()));			
			// count each kejadian  
			kejadianJenisCountList =
					getKejadianJenisCountList(kejadianList, selKotamaops);
			
			// select all motif kejadian
			kejadianMotifList = getKejadianMotifDao().findAllKejadianMotif();
			kejadianMotifCountList = getKejadianMotifCountList(kejadianMotifList, selKotamaops);
			
			// get all Pelaku kejadian
			kejadianPelakuList = getKejadianPelakuDao().findAllKejadianPelaku();
			kejadianPelakuCountList = getKejadianPelakuList(kejadianPelakuList, selKotamaops);
		}
		
		CategoryModel kejJenModel = new DefaultCategoryModel();
		
		for (KejadianJenisCount kejadianJenisCount : kejadianJenisCountList) {
			BigInteger count = kejadianJenisCount.getCount();
			String namaJenis = kejadianJenisCount.getKejadianJenis().getNamaJenis();
			
			kejJenModel.setValue("", namaJenis, count);
		}
		
		jenisKejadianChart.setModel(kejJenModel);
		
		PieModel kejJenPieModel = new DefaultPieModel();
		
		for (KejadianJenisCount kejadianJenisCount : kejadianJenisCountList) {
			BigInteger count = kejadianJenisCount.getCount();
			String namaJenis = kejadianJenisCount.getKejadianJenis().getNamaJenis();

			kejJenPieModel.setValue(namaJenis, count);
		}
		
		jenisKejadianPieChart.setModel(kejJenPieModel);
		
		CategoryModel kejMotModel = new DefaultCategoryModel();
		
		for (KejadianMotifCount kejadianMotifCount : kejadianMotifCountList) {
			BigInteger count = kejadianMotifCount.getCount();
			String namaMotif = kejadianMotifCount.getKejadianMotif().getNamaMotif();
			
			kejMotModel.setValue("", namaMotif, count);
		}
		
		motifKejadianChart.setModel(kejMotModel);

		PieModel kejMotPieModel = new DefaultPieModel();
		
		for (KejadianMotifCount kejadianMotifCount : kejadianMotifCountList) {
			BigInteger count = kejadianMotifCount.getCount();
			String namaMotif = kejadianMotifCount.getKejadianMotif().getNamaMotif();

			kejMotPieModel.setValue(namaMotif, count);
		}
		
		motifKejadianPieChart.setModel(kejMotPieModel);
		
		CategoryModel kejPelModel = new DefaultCategoryModel();

		for (KejadianPelakuCount kejadianPelakuCount : kejadianPelakuCountList) {
			BigInteger count = kejadianPelakuCount.getJumlah();
			String pelaku = kejadianPelakuCount.getKejadianPelaku().getNamaPelaku();
			
			kejPelModel.setValue("", pelaku, count);
		}

		pelakuKejadianChart.setModel(kejPelModel);

		PieModel kejPelPieModel = new DefaultPieModel();
		
		for (KejadianPelakuCount kejadianPelakuCount : kejadianPelakuCountList) {
			BigInteger count = kejadianPelakuCount.getJumlah();
			String pelaku = kejadianPelakuCount.getKejadianPelaku().getNamaPelaku();

			kejPelPieModel.setValue(pelaku, count);
		}
		
		pelakuKejadianPieChart.setModel(kejPelPieModel);
	}
	
	private List<KejadianPelakuCount> getKejadianPelakuList(List<KejadianPelaku> kejadianPelakuList,
			Kotamaops selKotamaops) {
		List<KejadianPelakuCount> kejadianPelakuCountList = new ArrayList<KejadianPelakuCount>();
		BigInteger countPelaku;
		for (KejadianPelaku kejadianPelaku : kejadianPelakuList) {
			countPelaku = 
					getKejadianRekapIntensitasDao().countPelakuKejadian(
							kejadianPelaku, selKotamaops, getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianPelakuCount kejadianPelakuCount = new KejadianPelakuCount();
			kejadianPelakuCount.setKejadianPelaku(kejadianPelaku);
			kejadianPelakuCount.setJumlah(countPelaku);
			
			kejadianPelakuCountList.add(kejadianPelakuCount);
		}
		return kejadianPelakuCountList;
	}

	private List<KejadianPelakuCount> getKejadianPelakuList(List<Kotamaops> kotamaopsList, List<KejadianPelaku> kejadianPelakuList) {
		List<KejadianPelakuCount> kejadianPelakuCountList = new ArrayList<KejadianPelakuCount>();
		BigInteger countPelaku;
		for (KejadianPelaku kejadianPelaku : kejadianPelakuList) {
			countPelaku = 
					getKejadianRekapIntensitasDao().countPelakuKejadian(
							kejadianPelaku, getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianPelakuCount kejadianPelakuCount = new KejadianPelakuCount();
			kejadianPelakuCount.setKejadianPelaku(kejadianPelaku);
			kejadianPelakuCount.setJumlah(countPelaku);
			
			kejadianPelakuCountList.add(kejadianPelakuCount);
		}
		return kejadianPelakuCountList;
	}

	private List<KejadianMotifCount> getKejadianMotifCountList(List<KejadianMotif> kejadianMotifList,
			Kotamaops kotamaops) {
		BigInteger kejMotCount;
		List<KejadianMotifCount> kejadianMotifCountList = new ArrayList<KejadianMotifCount>();
		for (KejadianMotif kejadianMotif : kejadianMotifList) {
			kejMotCount = 
					getKejadianRekapIntensitasDao().countMotifKejadian(
							kejadianMotif, kotamaops, getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianMotifCount kejMotifCount = new KejadianMotifCount();
			kejMotifCount.setKejadianMotif(kejadianMotif);
			kejMotifCount.setCount(kejMotCount);
			
			kejadianMotifCountList.add(kejMotifCount);
		}
		return kejadianMotifCountList;
	}

	private List<KejadianMotifCount> getKejadianMotifCountList(List<Kotamaops> kotamaopsList, List<KejadianMotif> kejadianMotifList) throws Exception {
		BigInteger kejMotCount;
		List<KejadianMotifCount> kejadianMotifCountList = new ArrayList<KejadianMotifCount>();
		for (KejadianMotif kejadianMotif : kejadianMotifList) {
			kejMotCount = 
					getKejadianRekapIntensitasDao().countMotifKejadianInKotamaops(kotamaopsList,
							kejadianMotif, getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianMotifCount kejMotifCount = new KejadianMotifCount();
			kejMotifCount.setKejadianMotif(kejadianMotif);
			kejMotifCount.setCount(kejMotCount);
			
			kejadianMotifCountList.add(kejMotifCount);
		}
		return kejadianMotifCountList;

	}

	private List<KejadianJenisCount> getKejadianJenisCountList(List<Kejadian> kejadianList, Kotamaops kotamaops) {
		// count the number of each distinch jenis kejadian
		BigInteger kejJenCount;
		List<KejadianJenisCount> kejadianJenisCountList = new ArrayList<KejadianJenisCount>();
		for (Kejadian kejadian : kejadianList) {
			kejJenCount = 
					getKejadianRekapIntensitasDao().countJenisKejadian(
							kejadian.getJenisKejadian(), kotamaops, getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianJenisCount kejJenisCount = new KejadianJenisCount();
			kejJenisCount.setKejadianJenis(kejadian.getJenisKejadian());
			kejJenisCount.setCount(kejJenCount);
			
			kejadianJenisCountList.add(kejJenisCount);
		}
		return kejadianJenisCountList;
	}

	private List<KejadianJenisCount> getKejadianJenisCountList(List<Kejadian> kejadianList) {
		// count the number of each distinch jenis kejadian
		BigInteger kejJenCount;
		List<KejadianJenisCount> kejadianJenisCountList = new ArrayList<KejadianJenisCount>();
		for (Kejadian kejadian : kejadianList) {
			kejJenCount = 
					getKejadianRekapIntensitasDao().countJenisKejadian(
							kejadian.getJenisKejadian(), getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianJenisCount kejJenisCount = new KejadianJenisCount();
			kejJenisCount.setKejadianJenis(kejadian.getJenisKejadian());
			kejJenisCount.setCount(kejJenCount);
			
			kejadianJenisCountList.add(kejJenisCount);
		}
		return kejadianJenisCountList;
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

	public PropinsiDao getPropinsiDao() {
		return propinsiDao;
	}

	public void setPropinsiDao(PropinsiDao propinsiDao) {
		this.propinsiDao = propinsiDao;
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

	public KejadianRekapIntensitasDao getKejadianRekapIntensitasDao() {
		return kejadianRekapIntensitasDao;
	}

	public void setKejadianRekapIntensitasDao(KejadianRekapIntensitasDao kejadianRekapIntensitasDao) {
		this.kejadianRekapIntensitasDao = kejadianRekapIntensitasDao;
	}

	public KejadianMotifDao getKejadianMotifDao() {
		return kejadianMotifDao;
	}

	public void setKejadianMotifDao(KejadianMotifDao kejadianMotifDao) {
		this.kejadianMotifDao = kejadianMotifDao;
	}

	public KejadianPelakuDao getKejadianPelakuDao() {
		return kejadianPelakuDao;
	}

	public void setKejadianPelakuDao(KejadianPelakuDao kejadianPelakuDao) {
		this.kejadianPelakuDao = kejadianPelakuDao;
	}
	
}
