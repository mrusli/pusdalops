package mil.pusdalops.webui.rekap;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kejadian.KejadianMotif;
import mil.pusdalops.domain.kejadian.KejadianPelaku;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;
import mil.pusdalops.domain.wilayah.Kecamatan;
import mil.pusdalops.domain.wilayah.Kelurahan;
import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.persistence.kabupaten_kotamadya.dao.Kabupaten_KotamadyaDao;
import mil.pusdalops.persistence.kecamatan.dao.KecamatanDao;
import mil.pusdalops.persistence.kejadian.motif.dao.KejadianMotifDao;
import mil.pusdalops.persistence.kejadian.pelaku.dao.KejadianPelakuDao;
import mil.pusdalops.persistence.kejadian.rekap.dao.KejadianRekapPelakuMotifDao;
import mil.pusdalops.persistence.kelurahan.dao.KelurahanDao;
import mil.pusdalops.persistence.kotamaops.dao.KotamaopsDao;
import mil.pusdalops.persistence.propinsi.dao.PropinsiDao;
import mil.pusdalops.persistence.settings.dao.SettingsDao;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.dialogs.DatetimeData;

public class KejadianPelakuRekapInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8984262133346435124L;

	private SettingsDao settingsDao;
	private KotamaopsDao kotamaopsDao;
	private PropinsiDao propinsiDao;
	private Kabupaten_KotamadyaDao kabupaten_KotamadyaDao;
	private KecamatanDao kecamatanDao;
	private KelurahanDao kelurahanDao;
	private KejadianRekapPelakuMotifDao kejadianRekapPelakuMotifDao;
	private KejadianPelakuDao kejadianPelakuDao;
	private KejadianMotifDao kejadianMotifDao;
	
	private Window kejadianPelakuRekapInfoWin;
	private Label formTitleLabel;
	private Textbox twAwalTahunTextbox, twAwalTanggalJamTextbox, twAwalTimeZoneTextbox, 
		twAkhirTahunTextbox, twAkhirTanggalJamTextbox, twAkhirTimeZoneTextbox;
	private Combobox kotamaopsCombobox, propinsiCombobox, kabupatenCombobox,
		kecamatanCombobox, kelurahanCombobox, matraCombobox;
	private Label jumlahKejKotamaops, jumlahKejPropinsi, jumlahKejKabupatenKot, 
		jumlahKejKecamatan, jumlahKejKelurahan;
	// private Listbox pelakuKejadianListbox, motifKejadianListbox;
	private Charts pelakuKejadianChart, motifKejadianChart, pelakuKejadianPieChart,
		motifKejadianPieChart;
	
	private Settings settings;
	private Kotamaops kotamaops;
	private LocalDateTime currentLocalDateTime, awalLocalDateTime, akhirLocalDateTime;
	private boolean isTwAwalAkhirProper;
	private ZoneId zoneId;

	private final long SETTINGS_DEFAULT_ID = 1L;
	private static final Logger log = Logger.getLogger(KejadianPelakuRekapInfoControl.class);
	
	public void onCreate$kejadianPelakuRekapInfoWin(Event event) throws Exception {
		setSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		setKotamaops(
				getSettings().getSelectedKotamaops());
		log.info("Creating Rekapitulais Kejadian dan Motif for Kotamaops: "+
				getKotamaops().getKotamaopsName());
		formTitleLabel.setValue(
				"Rekapitulasi | Kejadian dan Motif - Kotamaops: "+
				getKotamaops().getKotamaopsName());

		// set the timezone for this kotamaops
		int timezoneOrdinal = getKotamaops().getTimeZone().getValue();
		setZoneId(getKotamaops().getTimeZone().toZoneId(timezoneOrdinal));					
		
		// current datetime -- according to time zone (not just id)
		setCurrentLocalDateTime(getLocalDateTime(getZoneId()));				

		// set tw with current time
		setTwAwalAkhir();
		
		// load matra combobox
		loadKotamaopsMatraTypeCombobox();		
		
		// start with kotamaops
		loadKotamaopsCombobox();
		
		pelakuKejadianChart.getExporting().setEnabled(false);
		pelakuKejadianChart.getYAxis().setTitle("");
		pelakuKejadianChart.getLegend().setEnabled(false);
		pelakuKejadianChart.getPlotOptions().getSeries().setColorByPoint(true);
		
		pelakuKejadianPieChart.getExporting().setEnabled(false);
		
		motifKejadianChart.getExporting().setEnabled(false);
		motifKejadianChart.getYAxis().setTitle("");
		motifKejadianChart.getLegend().setEnabled(false);
		motifKejadianChart.getPlotOptions().getSeries().setColorByPoint(true);
		
		motifKejadianPieChart.getExporting().setEnabled(false);
	}
	
	private void setTwAwalAkhir() {
		// date minus 360 days
		LocalDate twAwalDate = minusDate(360L, asLocalDate(getCurrentLocalDateTime()));
		LocalTime twNoTime = LocalTime.of(0, 0, 0);

		twAwalTahunTextbox.setValue(
				getLocalDateTimeString(asLocalDateTime(twAwalDate, twNoTime), "YYYY"));
		twAwalTanggalJamTextbox.setValue(
				getLocalDateTimeString(asLocalDateTime(twAwalDate, twNoTime), "MM")+
				getLocalDateTimeString(getCurrentLocalDateTime(), "dd"));
		twAwalTanggalJamTextbox.setAttribute(
				"twAwalLocalDateTime", asLocalDateTime(twAwalDate, twNoTime));
		
		twAwalTimeZoneTextbox.setValue(getKotamaops().getTimeZone().toString());

		// set awal
		setAwalLocalDateTime(asLocalDateTime(twAwalDate, twNoTime));
		
		// akhir
		twAkhirTahunTextbox.setValue(getLocalDateTimeString(getCurrentLocalDateTime(), "YYYY"));
		twAkhirTanggalJamTextbox.setValue(
				getLocalDateTimeString(getCurrentLocalDateTime(), "MM")+
				getLocalDateTimeString(getCurrentLocalDateTime(), "dd"));
		twAkhirTanggalJamTextbox.setAttribute(
				"twAkhirLocalDateTime", asLocalDateTime(getCurrentLocalDateTime().toLocalDate(), twNoTime));

		twAkhirTimeZoneTextbox.setValue(getKotamaops().getTimeZone().toString());
		
		// set akhir
		setAkhirLocalDateTime(asLocalDateTime(getCurrentLocalDateTime().toLocalDate(), twNoTime));
		
		setTwAwalAkhirProper(true);			
	}

	private void loadKotamaopsMatraTypeCombobox() {
		if (getKotamaops().getKotamaopsType().equals(KotamaopsType.PUSDALOPS)) {
			// semua
			Comboitem comboitem;
			comboitem = new Comboitem();
			comboitem.setLabel("--Semua Matra--");
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
			// only selected kotamaops matra - disabled
			Comboitem comboitem;
			comboitem = new Comboitem();
			comboitem.setLabel(getKotamaops().getKotamaopsType().toString());
			comboitem.setValue(null);
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
		datetimeData.setLocalDateTime(
				(LocalDateTime) twAwalTanggalJamTextbox.getAttribute("twAwalLocalDateTime"));

		// pass to the dialog window
		Map<String, DatetimeData> args = Collections.singletonMap("datetimeData", datetimeData);
		Window datetimeWin = (Window) Executions.createComponents(
				"/dialogs/DatetimeWinDialog.zul", kejadianPelakuRekapInfoWin, args);
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
		datetimeData.setLocalDateTime(
				(LocalDateTime) twAkhirTanggalJamTextbox.getAttribute("twAkhirLocalDateTime"));

		// pass to the dialog window
		Map<String, DatetimeData> args = Collections.singletonMap("datetimeData", datetimeData);
		Window datetimeWin = (Window) Executions.createComponents(
				"/dialogs/DatetimeWinDialog.zul", kejadianPelakuRekapInfoWin, args);
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
			Comboitem comboitem;
			
			comboitem = new Comboitem();
			comboitem.setLabel(getKotamaops().getKotamaopsName());
			comboitem.setValue(getKotamaops());
			comboitem.setParent(kotamaopsCombobox);
			// no more kotamaops selection
			kotamaopsCombobox.setSelectedIndex(0);
			kotamaopsCombobox.setDisabled(true);
			
			// display the propinsi of the default kotamaops 
			Kotamaops kotamaopsByProxy = 
					getKotamaopsDao().findKotamaopsPropinsiByProxy(getKotamaops().getId());
			List<Propinsi> propinsiList = kotamaopsByProxy.getPropinsis();
			
			propinsiCombobox.setDisabled(false);
			propinsiCombobox.getItems().clear();
			
			// semua
			comboitem = new Comboitem();
			comboitem.setLabel("--semua--");
			comboitem.setValue(null);
			comboitem.setParent(propinsiCombobox);
			// propinsi
			for (Propinsi propinsi : propinsiList) {
				comboitem = new Comboitem();
				comboitem.setLabel(propinsi.getNamaPropinsi());
				comboitem.setValue(propinsi);
				comboitem.setParent(propinsiCombobox);
			}
			propinsiCombobox.setSelectedIndex(0);
		}
		
	}

	public void onSelect$matraCombobox(Event event) throws Exception {
		Comboitem comboitem;
		
		resetOtherComboboxes(4);
		// clear combobox
		kotamaopsCombobox.getItems().clear();		
		
		if (matraCombobox.getSelectedItem().getValue()==null) {
			// semua
			loadKotamaopsCombobox();
		} else {
			// display selected matra
			KotamaopsType kotamaopsMatraType = matraCombobox.getSelectedItem().getValue();
			// get all the kotamaops under this kotmaops
			Kotamaops kotamaopsByProxy = 
					getKotamaopsDao().findKotamaopsKotamaopsByProxy(getKotamaops().getId());
			List<Kotamaops> kotamaopsList = kotamaopsByProxy.getKotamaops();
			// filter out
			List<Kotamaops> selMatraKotamaopsList = new ArrayList<Kotamaops>();
			for (Kotamaops kotamaops : kotamaopsList) {
				if (kotamaops.getKotamaopsType().equals(kotamaopsMatraType)) {
					selMatraKotamaopsList.add(kotamaops);
				}
			}
			// semua
			comboitem = new Comboitem();
			comboitem.setLabel("--semua--");
			comboitem.setValue(null);
			comboitem.setParent(kotamaopsCombobox);
			// kotamaops
			for (Kotamaops kotamaops : selMatraKotamaopsList) {
				comboitem = new Comboitem();
				comboitem.setLabel(kotamaops.getKotamaopsName());
				comboitem.setValue(kotamaops);
				comboitem.setParent(kotamaopsCombobox);
			}
			kotamaopsCombobox.setSelectedIndex(0);
		}
	}
	
	public void onSelect$kotamaopsCombobox(Event event) throws Exception {
		// only for users in PUSDALOPS
		Kotamaops selKotamaops = kotamaopsCombobox.getSelectedItem().getValue();
		resetOtherComboboxes(4);

		// --semua-- : nothing to do
		if (selKotamaops == null) {
			return;
		}
		
		// find the propinsi
		Kotamaops kotamaopsByProxy = 
				getKotamaopsDao().findKotamaopsPropinsiByProxy(selKotamaops.getId());
		List<Propinsi> propinsiList = kotamaopsByProxy.getPropinsis();
		
		// allow user to selects the propinsi
		propinsiCombobox.setDisabled(false);
		propinsiCombobox.getItems().clear();
		
		// populate the propinsi combobox
		Comboitem comboitem;
		// semua
		comboitem = new Comboitem();
		comboitem.setLabel("--semua--");
		comboitem.setValue(null);
		comboitem.setParent(propinsiCombobox);
		// propinsi
		for (Propinsi propinsi : propinsiList) {
			comboitem = new Comboitem();
			comboitem.setLabel(propinsi.getNamaPropinsi());
			comboitem.setValue(propinsi);
			comboitem.setParent(propinsiCombobox);
		}
		propinsiCombobox.setSelectedIndex(0);		
	}
	
	public void onSelect$propinsiCombobox(Event event) throws Exception {
		Propinsi selPropinsi = propinsiCombobox.getSelectedItem().getValue();
		resetOtherComboboxes(3);
		
		if (selPropinsi == null) {			
			return;
		}
		
		Propinsi selPropinsiByProxy = 
				getPropinsiDao().findKabupatenKotamadyaByProxy(selPropinsi.getId());
		List<Kabupaten_Kotamadya> kabupaten_Kotamadyas = 
				selPropinsiByProxy.getKabupatenkotamadyas();

		kabupatenCombobox.setDisabled(false);
		kabupatenCombobox.getItems().clear();

		// semua
		Comboitem comboitem;
		comboitem = new Comboitem();
		comboitem.setLabel("--semua--");
		comboitem.setValue(null);
		comboitem.setParent(kabupatenCombobox);
		// kabupaten_kotamadya
		for (Kabupaten_Kotamadya kabupaten_Kotamadya : kabupaten_Kotamadyas) {
			comboitem = new Comboitem();
			comboitem.setLabel(kabupaten_Kotamadya.getNamaKabupaten());
			comboitem.setValue(kabupaten_Kotamadya);
			comboitem.setParent(kabupatenCombobox);
		}
		kabupatenCombobox.setSelectedIndex(0);
		
	}
	
	public void onSelect$kabupatenCombobox(Event event) throws Exception {
		Kabupaten_Kotamadya selKabupaten = kabupatenCombobox.getSelectedItem().getValue();
		resetOtherComboboxes(2);
		
		if (selKabupaten == null) {			
			return;
		}
		
		Kabupaten_Kotamadya selKabupatenByProxy = 
				getKabupaten_KotamadyaDao().findKecamatanByProxy(selKabupaten.getId());
		List<Kecamatan> kecamatans = 
				selKabupatenByProxy.getKecamatans();

		kecamatanCombobox.setDisabled(false);
		kecamatanCombobox.getItems().clear();
		
		// semua
		Comboitem comboitem;
		comboitem = new Comboitem();
		comboitem.setLabel("--semua--");
		comboitem.setValue(null);
		comboitem.setParent(kecamatanCombobox);
		// kecamatan
		for (Kecamatan kecamatan : kecamatans) {
			comboitem = new Comboitem();
			comboitem.setLabel(kecamatan.getNamaKecamatan());
			comboitem.setValue(kecamatan);
			comboitem.setParent(kecamatanCombobox);
		}
		kecamatanCombobox.setSelectedIndex(0);
		
	}
	
	public void onSelect$kecamatanCombobox(Event event) throws Exception {
		Kecamatan selKecamatan = kecamatanCombobox.getSelectedItem().getValue();
		resetOtherComboboxes(1);
		
		if (selKecamatan == null) {			
			return;
		}
		
		Kecamatan selKecamatanByProxy = 
				getKecamatanDao().findKelurahanByProxy(selKecamatan.getId());
		List<Kelurahan> kelurahans = 
				selKecamatanByProxy.getKelurahans();

		kelurahanCombobox.setDisabled(false);
		kelurahanCombobox.getItems().clear();
		
		// semua
		Comboitem comboitem;
		comboitem = new Comboitem();
		comboitem.setLabel("--semua--");
		comboitem.setValue(null);
		comboitem.setParent(kelurahanCombobox);
		// kelurahan
		for (Kelurahan kelurahan : kelurahans) {
			comboitem = new Comboitem();
			comboitem.setLabel(kelurahan.getNamaKelurahan());
			comboitem.setValue(kelurahan);
			comboitem.setParent(kelurahanCombobox);
		}
		kelurahanCombobox.setSelectedIndex(0);		
	}
	
	public void onSelect$kelurahanCombobox(Event event) throws Exception {
		
	}	
	
	private void resetOtherComboboxes(int numComboboxes) {
		switch (numComboboxes) {
		case 4:
			// when kotamaops is selected
			jumlahKejKotamaops.setValue(BigInteger.ZERO.toString());
			jumlahKejPropinsi.setValue(BigInteger.ZERO.toString());
			jumlahKejKabupatenKot.setValue(BigInteger.ZERO.toString());
			jumlahKejKecamatan.setValue(BigInteger.ZERO.toString());
			jumlahKejKelurahan.setValue(BigInteger.ZERO.toString());
			
			propinsiCombobox.setValue("");
			kabupatenCombobox.setValue("");
			kecamatanCombobox.setValue("");
			kelurahanCombobox.setValue("");
			
			propinsiCombobox.getItems().clear();
			kabupatenCombobox.getItems().clear();
			kecamatanCombobox.getItems().clear();
			kelurahanCombobox.getItems().clear();
			
			propinsiCombobox.setDisabled(true);
			kabupatenCombobox.setDisabled(true);
			kecamatanCombobox.setDisabled(true);
			kelurahanCombobox.setDisabled(true);
			break;
		case 3:
			// when propinsi is selected
			jumlahKejPropinsi.setValue(BigInteger.ZERO.toString());
			jumlahKejKabupatenKot.setValue(BigInteger.ZERO.toString());
			jumlahKejKecamatan.setValue(BigInteger.ZERO.toString());
			jumlahKejKelurahan.setValue(BigInteger.ZERO.toString());

			kabupatenCombobox.setValue("");
			kecamatanCombobox.setValue("");
			kelurahanCombobox.setValue("");

			kabupatenCombobox.getItems().clear();
			kecamatanCombobox.getItems().clear();
			kelurahanCombobox.getItems().clear();			

			kabupatenCombobox.setDisabled(true);
			kecamatanCombobox.setDisabled(true);
			kelurahanCombobox.setDisabled(true);
			break;
		case 2:
			// when kabupaten is selected
			jumlahKejKabupatenKot.setValue(BigInteger.ZERO.toString());
			jumlahKejKecamatan.setValue(BigInteger.ZERO.toString());
			jumlahKejKelurahan.setValue(BigInteger.ZERO.toString());

			kecamatanCombobox.setValue("");
			kelurahanCombobox.setValue("");

			kecamatanCombobox.getItems().clear();
			kelurahanCombobox.getItems().clear();			

			kecamatanCombobox.setDisabled(true);
			kelurahanCombobox.setDisabled(true);
			break;
		case 1:
			// when kecamatan is selected
			jumlahKejKecamatan.setValue(BigInteger.ZERO.toString());
			jumlahKejKelurahan.setValue(BigInteger.ZERO.toString());

			kelurahanCombobox.setValue("");
			kelurahanCombobox.getItems().clear();			
			kelurahanCombobox.setDisabled(true);

			break;

		default:
			break;
		}
	}	
	
	/**
	 * @param event
	 * @throws Exception
	 */
	public void onClick$executeButton(Event event) throws Exception {
		// is twAwalAkhirProper? -- if yes, get twAwal and twAkhir
		if (!isTwAwalAkhirProper) {
			throw new Exception("TW Awal dan Akhir BELUM sesuai");
		}

		Comboitem selKotamaopsComboitem = kotamaopsCombobox.getSelectedItem();
		Comboitem selPropinsiComboitem = propinsiCombobox.getSelectedItem();
		Comboitem selKabupatenKotComboitem = kabupatenCombobox.getSelectedItem();
		Comboitem selKecamatanComboitem = kecamatanCombobox.getSelectedItem();
		Comboitem selKelurahanComboitem = kelurahanCombobox.getSelectedItem();
		BigInteger countKej;
		// List<Kejadian> kejadianList;
		// List<KejadianJenisCount> kejadianJenisCountList;
		List<KejadianMotif> kejadianMotifList;
		List<KejadianPelakuCount> kejadianPelakuCountList;
		List<KejadianMotifCount> kejadianMotifCountList;
		if (selKotamaopsComboitem.getValue()==null) {
			// count the number of kejadian for all kotamaops under Pusdalops
			// Kotamaops kotamaopsByProxy = 
			//		getKotamaopsDao().findKotamaopsKotamaopsByProxy(getKotamaops().getId());
			// List<Kotamaops> kotamaopsList = kotamaopsByProxy.getKotamaops();
			List<Kotamaops> kotamaopsList = new ArrayList<Kotamaops>();
			for (Comboitem comboitem : kotamaopsCombobox.getItems()) {
				if (comboitem.getValue()==null) {
					// do nothing
				} else {
					kotamaopsList.add(comboitem.getValue());
				}
			}
			
			// kejadianRekapPelakuMotifDao
			countKej = getKejadianRekapPelakuMotifDao().countKejadianInKotamaopsList(kotamaopsList, getAwalLocalDateTime(), getAkhirLocalDateTime());
			jumlahKejKotamaops.setValue(countKej.toString());
			
			// get all Pelaku kejadian
			List<KejadianPelaku> kejadianPelakuList = getKejadianPelakuDao().findAllKejadianPelaku();
			kejadianPelakuCountList = getKejadianPelakuList(kejadianPelakuList);
			// pelakuKejadianListbox.setModel(
			//		new ListModelList<KejadianPelakuCount>(kejadianPelakuCountList));
			// pelakuKejadianListbox.setItemRenderer(getKejadianPelakuCountListitemRenderer());
			
			// motif
			kejadianMotifList = getKejadianMotifDao().findAllKejadianMotif();
			// count the number of motif in the kejadian
			kejadianMotifCountList = getKejadianMotifCountList(kejadianMotifList);
			// motifKejadianListbox.setModel(new ListModelList<KejadianMotifCount>(kejadianMotifCountList));
			// motifKejadianListbox.setItemRenderer(getKejadianMotifCountListitemRenderer());
			
		} else if (selPropinsiComboitem.getValue()==null) {
			// count the number of kejadian for selected kotamaops
			countKej = getKejadianRekapPelakuMotifDao().countKejadian(
					selKotamaopsComboitem.getValue(), 
					getAwalLocalDateTime(), getAkhirLocalDateTime());
			jumlahKejKotamaops.setValue(countKej.toString());

			// get all Pelaku kejadian
			List<KejadianPelaku> kejadianPelakuList = getKejadianPelakuDao().findAllKejadianPelaku();
			kejadianPelakuCountList = getKejadianPelakuList(kejadianPelakuList, 
					selKotamaopsComboitem.getValue());
			// pelakuKejadianListbox.setModel(
			//		new ListModelList<KejadianPelakuCount>(kejadianPelakuCountList));
			// pelakuKejadianListbox.setItemRenderer(getKejadianPelakuCountListitemRenderer());
						
			// motif
			kejadianMotifList = getKejadianMotifDao().findAllKejadianMotif();
			// count the number of motif in the kejadian
			kejadianMotifCountList = getKejadianMotifCountList(kejadianMotifList, 
					selKotamaopsComboitem.getValue());
			// motifKejadianListbox.setModel(new ListModelList<KejadianMotifCount>(kejadianMotifCountList));
			// motifKejadianListbox.setItemRenderer(getKejadianMotifCountListitemRenderer());
			
		} else if (selKabupatenKotComboitem.getValue()==null) {
			// count the number of kejadian for selected kotamaops and propinsi
			countKej = getKejadianRekapPelakuMotifDao().countKejadian(
					selKotamaopsComboitem.getValue(), selPropinsiComboitem.getValue(), 
					getAwalLocalDateTime(), getAkhirLocalDateTime());
			jumlahKejPropinsi.setValue(countKej.toString());
					
			// get all Pelaku kejadian
			List<KejadianPelaku> kejadianPelakuList = getKejadianPelakuDao().findAllKejadianPelaku();
			kejadianPelakuCountList = getKejadianPelakuList(kejadianPelakuList, 
					selKotamaopsComboitem.getValue(), selPropinsiComboitem.getValue());
			// pelakuKejadianListbox.setModel(
			//		new ListModelList<KejadianPelakuCount>(kejadianPelakuCountList));
			// pelakuKejadianListbox.setItemRenderer(getKejadianPelakuCountListitemRenderer());
						
			// motif
			kejadianMotifList = getKejadianMotifDao().findAllKejadianMotif();
			// count the number of motif in the kejadian
			kejadianMotifCountList = getKejadianMotifCountList(kejadianMotifList,
					selKotamaopsComboitem.getValue(), selPropinsiComboitem.getValue());
			// motifKejadianListbox.setModel(new ListModelList<KejadianMotifCount>(kejadianMotifCountList));
			// motifKejadianListbox.setItemRenderer(getKejadianMotifCountListitemRenderer());
			
			
		} else if (selKecamatanComboitem.getValue()==null) {
			// count the number of kejadian for selected kotamaops propinsi kabupatenKot
			countKej = getKejadianRekapPelakuMotifDao().countKejadian(
					selKotamaopsComboitem.getValue(), selPropinsiComboitem.getValue(),
					selKabupatenKotComboitem.getValue(),
					getAwalLocalDateTime(), getAkhirLocalDateTime());
			jumlahKejKabupatenKot.setValue(countKej.toString());

			// get all Pelaku kejadian
			List<KejadianPelaku> kejadianPelakuList = getKejadianPelakuDao().findAllKejadianPelaku();
			kejadianPelakuCountList = getKejadianPelakuList(kejadianPelakuList, 
					selKotamaopsComboitem.getValue(), selPropinsiComboitem.getValue(), selKabupatenKotComboitem.getValue());
			// pelakuKejadianListbox.setModel(
			//		new ListModelList<KejadianPelakuCount>(kejadianPelakuCountList));
			// pelakuKejadianListbox.setItemRenderer(getKejadianPelakuCountListitemRenderer());

			// motif
			kejadianMotifList = getKejadianMotifDao().findAllKejadianMotif();
			// count the number of motif in the kejadian
			kejadianMotifCountList = getKejadianMotifCountList(kejadianMotifList,
					selKotamaopsComboitem.getValue(), selPropinsiComboitem.getValue(), selKabupatenKotComboitem.getValue());
			// motifKejadianListbox.setModel(new ListModelList<KejadianMotifCount>(kejadianMotifCountList));
			// motifKejadianListbox.setItemRenderer(getKejadianMotifCountListitemRenderer());

		} else if (selKelurahanComboitem.getValue()==null) {
			// count the number of kejadian for selected kotamaops propinsi kabupatenKot kecamatan
			countKej = getKejadianRekapPelakuMotifDao().countKejadian(
					selKotamaopsComboitem.getValue(), selPropinsiComboitem.getValue(),
					selKabupatenKotComboitem.getValue(), selKecamatanComboitem.getValue(),
					getAwalLocalDateTime(), getAkhirLocalDateTime());
			jumlahKejKecamatan.setValue(countKej.toString());
			
			// get all Pelaku kejadian
			List<KejadianPelaku> kejadianPelakuList = getKejadianPelakuDao().findAllKejadianPelaku();
			kejadianPelakuCountList = getKejadianPelakuList(kejadianPelakuList, 
					selKotamaopsComboitem.getValue(), selPropinsiComboitem.getValue(), selKabupatenKotComboitem.getValue(),
					selKecamatanComboitem.getValue());
			// pelakuKejadianListbox.setModel(
			// 		new ListModelList<KejadianPelakuCount>(kejadianPelakuCountList));
			// pelakuKejadianListbox.setItemRenderer(getKejadianPelakuCountListitemRenderer());

			// motif
			kejadianMotifList = getKejadianMotifDao().findAllKejadianMotif();
			// count the number of motif in the kejadian
			kejadianMotifCountList = getKejadianMotifCountList(kejadianMotifList,
					selKotamaopsComboitem.getValue(), selPropinsiComboitem.getValue(), selKabupatenKotComboitem.getValue(),
					selKecamatanComboitem.getValue());
			// motifKejadianListbox.setModel(new ListModelList<KejadianMotifCount>(kejadianMotifCountList));
			// motifKejadianListbox.setItemRenderer(getKejadianMotifCountListitemRenderer());

		} else {
			// count the number of kejadian for selected kotamaops propinsi kabupatenKot kecamatan kelurahan
			countKej = getKejadianRekapPelakuMotifDao().countKejadian(
					selKotamaopsComboitem.getValue(), selPropinsiComboitem.getValue(),
					selKabupatenKotComboitem.getValue(), selKecamatanComboitem.getValue(),
					selKelurahanComboitem.getValue(),
					getAwalLocalDateTime(), getAkhirLocalDateTime());
			jumlahKejKelurahan.setValue(countKej.toString());
			
			// get all Pelaku kejadian
			List<KejadianPelaku> kejadianPelakuList = getKejadianPelakuDao().findAllKejadianPelaku();
			kejadianPelakuCountList = getKejadianPelakuList(kejadianPelakuList, 
					selKotamaopsComboitem.getValue(), selPropinsiComboitem.getValue(), selKabupatenKotComboitem.getValue(),
					selKecamatanComboitem.getValue(), selKelurahanComboitem.getValue());
			// pelakuKejadianListbox.setModel(
			//		new ListModelList<KejadianPelakuCount>(kejadianPelakuCountList));
			// pelakuKejadianListbox.setItemRenderer(getKejadianPelakuCountListitemRenderer());

			// motif
			kejadianMotifList = getKejadianMotifDao().findAllKejadianMotif();
			// count the number of motif in the kejadian
			kejadianMotifCountList = getKejadianMotifCountList(kejadianMotifList,
					selKotamaopsComboitem.getValue(), selPropinsiComboitem.getValue(), selKabupatenKotComboitem.getValue(),
					selKecamatanComboitem.getValue(), selKelurahanComboitem.getValue());
			// motifKejadianListbox.setModel(new ListModelList<KejadianMotifCount>(kejadianMotifCountList));
			// motifKejadianListbox.setItemRenderer(getKejadianMotifCountListitemRenderer());

		}
		
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
		
		CategoryModel kejMotModel = new DefaultCategoryModel();
		
		for (KejadianMotifCount kejadianMotifCount : kejadianMotifCountList) {
			BigInteger count = kejadianMotifCount.getCount();
			String namaMotif = kejadianMotifCount.getKejadianMotif().getNamaMotif();
			
			kejMotModel.setValue("", namaMotif, count);
		}
		
		motifKejadianChart.setModel(kejMotModel);

		PieModel motKejPieModel = new DefaultPieModel();
		
		for (KejadianMotifCount kejadianMotifCount : kejadianMotifCountList) {
			BigInteger count = kejadianMotifCount.getCount();
			String namaMotif = kejadianMotifCount.getKejadianMotif().getNamaMotif();

			motKejPieModel.setValue(namaMotif, count);
		}

		motifKejadianPieChart.setModel(motKejPieModel);

	}

	private List<KejadianPelakuCount> getKejadianPelakuList(List<KejadianPelaku> kejadianPelakuList) {
		List<KejadianPelakuCount> kejadianPelakuCountList = new ArrayList<KejadianPelakuCount>();
		BigInteger countPelaku;
		for (KejadianPelaku kejadianPelaku : kejadianPelakuList) {
			countPelaku = getKejadianRekapPelakuMotifDao().countPelakuKejadian(kejadianPelaku, getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianPelakuCount kejadianPelakuCount = new KejadianPelakuCount();
			kejadianPelakuCount.setKejadianPelaku(kejadianPelaku);
			kejadianPelakuCount.setJumlah(countPelaku);
			
			kejadianPelakuCountList.add(kejadianPelakuCount);
		}
		return kejadianPelakuCountList;
	}	

	private List<KejadianPelakuCount> getKejadianPelakuList(List<KejadianPelaku> kejadianPelakuList, Kotamaops kotamaops) {
		List<KejadianPelakuCount> kejadianPelakuCountList = new ArrayList<KejadianPelakuCount>();
		BigInteger countPelaku;
		for (KejadianPelaku kejadianPelaku : kejadianPelakuList) {
			countPelaku = getKejadianRekapPelakuMotifDao().countPelakuKejadian(kejadianPelaku, kotamaops, getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianPelakuCount kejadianPelakuCount = new KejadianPelakuCount();
			kejadianPelakuCount.setKejadianPelaku(kejadianPelaku);
			kejadianPelakuCount.setJumlah(countPelaku);
			
			kejadianPelakuCountList.add(kejadianPelakuCount);
		}
		return kejadianPelakuCountList;
	}	

	private List<KejadianPelakuCount> getKejadianPelakuList(List<KejadianPelaku> kejadianPelakuList, Kotamaops kotamaops, Propinsi propinsi) {
		List<KejadianPelakuCount> kejadianPelakuCountList = new ArrayList<KejadianPelakuCount>();
		BigInteger countPelaku;
		for (KejadianPelaku kejadianPelaku : kejadianPelakuList) {
			countPelaku = getKejadianRekapPelakuMotifDao().countPelakuKejadian(kejadianPelaku, kotamaops, propinsi, getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianPelakuCount kejadianPelakuCount = new KejadianPelakuCount();
			kejadianPelakuCount.setKejadianPelaku(kejadianPelaku);
			kejadianPelakuCount.setJumlah(countPelaku);
			
			kejadianPelakuCountList.add(kejadianPelakuCount);
		}
		return kejadianPelakuCountList;
	}	

	private List<KejadianPelakuCount> getKejadianPelakuList(List<KejadianPelaku> kejadianPelakuList, Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot) {
		List<KejadianPelakuCount> kejadianPelakuCountList = new ArrayList<KejadianPelakuCount>();
		BigInteger countPelaku;
		for (KejadianPelaku kejadianPelaku : kejadianPelakuList) {
			countPelaku = getKejadianRekapPelakuMotifDao().countPelakuKejadian(kejadianPelaku, kotamaops, propinsi, kabupatenKot, getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianPelakuCount kejadianPelakuCount = new KejadianPelakuCount();
			kejadianPelakuCount.setKejadianPelaku(kejadianPelaku);
			kejadianPelakuCount.setJumlah(countPelaku);
			
			kejadianPelakuCountList.add(kejadianPelakuCount);
		}
		return kejadianPelakuCountList;
	}	
	
	private List<KejadianPelakuCount> getKejadianPelakuList(List<KejadianPelaku> kejadianPelakuList, Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot,
			Kecamatan kecamatan) {
		List<KejadianPelakuCount> kejadianPelakuCountList = new ArrayList<KejadianPelakuCount>();
		BigInteger countPelaku;
		for (KejadianPelaku kejadianPelaku : kejadianPelakuList) {
			countPelaku = getKejadianRekapPelakuMotifDao().countPelakuKejadian(kejadianPelaku, kotamaops, propinsi, kabupatenKot, kecamatan, getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianPelakuCount kejadianPelakuCount = new KejadianPelakuCount();
			kejadianPelakuCount.setKejadianPelaku(kejadianPelaku);
			kejadianPelakuCount.setJumlah(countPelaku);
			
			kejadianPelakuCountList.add(kejadianPelakuCount);
		}
		return kejadianPelakuCountList;
	}		

	private List<KejadianPelakuCount> getKejadianPelakuList(List<KejadianPelaku> kejadianPelakuList, Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot,
			Kecamatan kecamatan, Kelurahan kelurahan) {
		List<KejadianPelakuCount> kejadianPelakuCountList = new ArrayList<KejadianPelakuCount>();
		BigInteger countPelaku;
		for (KejadianPelaku kejadianPelaku : kejadianPelakuList) {
			countPelaku = getKejadianRekapPelakuMotifDao().countPelakuKejadian(kejadianPelaku, kotamaops, propinsi, kabupatenKot, kecamatan, kelurahan, getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianPelakuCount kejadianPelakuCount = new KejadianPelakuCount();
			kejadianPelakuCount.setKejadianPelaku(kejadianPelaku);
			kejadianPelakuCount.setJumlah(countPelaku);
			
			kejadianPelakuCountList.add(kejadianPelakuCount);
		}
		return kejadianPelakuCountList;
	}		
	
	@SuppressWarnings("unused")
	private ListitemRenderer<KejadianMotifCount> getKejadianMotifCountListitemRenderer() {
		
		return new ListitemRenderer<KejadianMotifCount>() {
			
			@Override
			public void render(Listitem item, KejadianMotifCount kejMotCount, int index) throws Exception {
				Listcell lc;
				
				// Motif Kejadian
				lc = new Listcell(kejMotCount.getKejadianMotif().getNamaMotif());
				lc.setParent(item);
				
				// Jumlah
				lc = new Listcell(kejMotCount.getCount().toString());
				lc.setParent(item);
				
			}
		};
	}

	private List<KejadianMotifCount> getKejadianMotifCountList(List<KejadianMotif> kejadianMotifList) {
		BigInteger kejMotCount;
		List<KejadianMotifCount> kejadianMotifCountList = new ArrayList<KejadianMotifCount>();
		for (KejadianMotif kejadianMotif : kejadianMotifList) {
			kejMotCount = getKejadianRekapPelakuMotifDao().countMotifKejadian(kejadianMotif, getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianMotifCount kejMotifCount = new KejadianMotifCount();
			kejMotifCount.setKejadianMotif(kejadianMotif);
			kejMotifCount.setCount(kejMotCount);
			
			kejadianMotifCountList.add(kejMotifCount);
		}
		return kejadianMotifCountList;

	}

	private List<KejadianMotifCount> getKejadianMotifCountList(List<KejadianMotif> kejadianMotifList, Kotamaops kotamaops) {
		BigInteger kejMotCount;
		List<KejadianMotifCount> kejadianMotifCountList = new ArrayList<KejadianMotifCount>();
		for (KejadianMotif kejadianMotif : kejadianMotifList) {
			kejMotCount = getKejadianRekapPelakuMotifDao().countMotifKejadian(kejadianMotif, kotamaops, getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianMotifCount kejMotifCount = new KejadianMotifCount();
			kejMotifCount.setKejadianMotif(kejadianMotif);
			kejMotifCount.setCount(kejMotCount);
			
			kejadianMotifCountList.add(kejMotifCount);
		}
		return kejadianMotifCountList;
	}
	
	private List<KejadianMotifCount> getKejadianMotifCountList(List<KejadianMotif> kejadianMotifList, Kotamaops kotamaops,
			Propinsi propinsi) {
		BigInteger kejMotCount;
		List<KejadianMotifCount> kejadianMotifCountList = new ArrayList<KejadianMotifCount>();
		for (KejadianMotif kejadianMotif : kejadianMotifList) {
			kejMotCount = getKejadianRekapPelakuMotifDao().countMotifKejadian(kejadianMotif, kotamaops, propinsi, getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianMotifCount kejMotifCount = new KejadianMotifCount();
			kejMotifCount.setKejadianMotif(kejadianMotif);
			kejMotifCount.setCount(kejMotCount);
			
			kejadianMotifCountList.add(kejMotifCount);
		}
		return kejadianMotifCountList;
	}

	private List<KejadianMotifCount> getKejadianMotifCountList(List<KejadianMotif> kejadianMotifList, Kotamaops kotamaops,
			Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot) {

		BigInteger kejMotCount;
		List<KejadianMotifCount> kejadianMotifCountList = new ArrayList<KejadianMotifCount>();
		for (KejadianMotif kejadianMotif : kejadianMotifList) {
			kejMotCount = getKejadianRekapPelakuMotifDao().countMotifKejadian(kejadianMotif, kotamaops, propinsi, kabupatenKot, getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianMotifCount kejMotifCount = new KejadianMotifCount();
			kejMotifCount.setKejadianMotif(kejadianMotif);
			kejMotifCount.setCount(kejMotCount);
			
			kejadianMotifCountList.add(kejMotifCount);
		}
		return kejadianMotifCountList;
	}
	
	private List<KejadianMotifCount> getKejadianMotifCountList(List<KejadianMotif> kejadianMotifList, Kotamaops kotamaops,
			Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, Kecamatan kecamatan) {

		BigInteger kejMotCount;
		List<KejadianMotifCount> kejadianMotifCountList = new ArrayList<KejadianMotifCount>();
		for (KejadianMotif kejadianMotif : kejadianMotifList) {
			kejMotCount = getKejadianRekapPelakuMotifDao().countMotifKejadian(kejadianMotif, kotamaops, propinsi, kabupatenKot, kecamatan, 
					getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianMotifCount kejMotifCount = new KejadianMotifCount();
			kejMotifCount.setKejadianMotif(kejadianMotif);
			kejMotifCount.setCount(kejMotCount);
			
			kejadianMotifCountList.add(kejMotifCount);
		}
		return kejadianMotifCountList;

	}
	
	private List<KejadianMotifCount> getKejadianMotifCountList(List<KejadianMotif> kejadianMotifList, Kotamaops kotamaops,
			Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, Kecamatan kecamatan, Kelurahan kelurahan) {
		
		BigInteger kejMotCount;
		List<KejadianMotifCount> kejadianMotifCountList = new ArrayList<KejadianMotifCount>();
		for (KejadianMotif kejadianMotif : kejadianMotifList) {
			kejMotCount = getKejadianRekapPelakuMotifDao().countMotifKejadian(kejadianMotif, kotamaops, propinsi, kabupatenKot, kecamatan, 
					kelurahan, getAwalLocalDateTime(), getAkhirLocalDateTime());
			
			KejadianMotifCount kejMotifCount = new KejadianMotifCount();
			kejMotifCount.setKejadianMotif(kejadianMotif);
			kejMotifCount.setCount(kejMotCount);
			
			kejadianMotifCountList.add(kejMotifCount);
		}
		return kejadianMotifCountList;
	}	
	
	@SuppressWarnings("unused")
	private ListitemRenderer<KejadianPelakuCount> getKejadianPelakuCountListitemRenderer() {
		
		return new ListitemRenderer<KejadianPelakuCount>() {
			
			@Override
			public void render(Listitem item, KejadianPelakuCount kejPelCount, int index) throws Exception {
				Listcell lc;
				
				// Pelaku
				lc = new Listcell(kejPelCount.getKejadianPelaku().getNamaPelaku());
				lc.setParent(item);
				
				// Sasaran
				// lc = new Listcell();
				// lc.setParent(item);
				
				// Jumlah
				lc = new Listcell(kejPelCount.getJumlah().toString());
				lc.setParent(item);
				
				
			}
		};
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

	public Kabupaten_KotamadyaDao getKabupaten_KotamadyaDao() {
		return kabupaten_KotamadyaDao;
	}

	public void setKabupaten_KotamadyaDao(Kabupaten_KotamadyaDao kabupaten_KotamadyaDao) {
		this.kabupaten_KotamadyaDao = kabupaten_KotamadyaDao;
	}

	public KecamatanDao getKecamatanDao() {
		return kecamatanDao;
	}

	public void setKecamatanDao(KecamatanDao kecamatanDao) {
		this.kecamatanDao = kecamatanDao;
	}

	public KelurahanDao getKelurahanDao() {
		return kelurahanDao;
	}

	public void setKelurahanDao(KelurahanDao kelurahanDao) {
		this.kelurahanDao = kelurahanDao;
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
		return isTwAwalAkhirProper;
	}

	public void setTwAwalAkhirProper(boolean isTwAwalAkhirProper) {
		this.isTwAwalAkhirProper = isTwAwalAkhirProper;
	}

	public ZoneId getZoneId() {
		return zoneId;
	}

	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	public KejadianRekapPelakuMotifDao getKejadianRekapPelakuMotifDao() {
		return kejadianRekapPelakuMotifDao;
	}

	public void setKejadianRekapPelakuMotifDao(KejadianRekapPelakuMotifDao kejadianRekapPelakuMotifDao) {
		this.kejadianRekapPelakuMotifDao = kejadianRekapPelakuMotifDao;
	}

	public KejadianPelakuDao getKejadianPelakuDao() {
		return kejadianPelakuDao;
	}

	public void setKejadianPelakuDao(KejadianPelakuDao kejadianPelakuDao) {
		this.kejadianPelakuDao = kejadianPelakuDao;
	}

	public KejadianMotifDao getKejadianMotifDao() {
		return kejadianMotifDao;
	}

	public void setKejadianMotifDao(KejadianMotifDao kejadianMotifDao) {
		this.kejadianMotifDao = kejadianMotifDao;
	}

}
