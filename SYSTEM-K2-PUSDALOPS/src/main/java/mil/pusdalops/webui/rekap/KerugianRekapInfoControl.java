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
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kerugian.Kerugian;
import mil.pusdalops.domain.kerugian.Pihak;
import mil.pusdalops.domain.kerugian.TipeKerugian;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;
import mil.pusdalops.domain.wilayah.Kecamatan;
import mil.pusdalops.domain.wilayah.Kelurahan;
import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.persistence.kabupaten_kotamadya.dao.Kabupaten_KotamadyaDao;
import mil.pusdalops.persistence.kecamatan.dao.KecamatanDao;
import mil.pusdalops.persistence.kejadian.dao.KejadianDao;
import mil.pusdalops.persistence.kejadian.rekap.dao.KejadianRekapDao;
import mil.pusdalops.persistence.kelurahan.dao.KelurahanDao;
import mil.pusdalops.persistence.kotamaops.dao.KotamaopsDao;
import mil.pusdalops.persistence.propinsi.dao.PropinsiDao;
import mil.pusdalops.persistence.settings.dao.SettingsDao;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.dialogs.DatetimeData;

public class KerugianRekapInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5170371846637129779L;

	private SettingsDao settingsDao;
	private KotamaopsDao kotamaopsDao;
	private PropinsiDao propinsiDao;
	private Kabupaten_KotamadyaDao kabupaten_KotamadyaDao;
	private KecamatanDao kecamatanDao;
	private KelurahanDao kelurahanDao;
	private KejadianRekapDao kejadianRekapDao;
	private KejadianDao kejadianDao;
	
	private Window kerugianRekapInfoWin;
	private Label formTitleLabel;
	private Combobox kotamaopsCombobox, propinsiCombobox, kabupatenCombobox,
		kecamatanCombobox, kelurahanCombobox, pihakCombobox;
	private Textbox twAwalTahunTextbox, twAkhirTahunTextbox, twAwalTimeZoneTextbox,
		twAkhirTimeZoneTextbox, twAwalTanggalJamTextbox, twAkhirTanggalJamTextbox,
		kerugianPersTextbox, kerugianMatrTextbox, kerugianTotalTextbox;
	private Listbox kerugianListbox;
	
	private Settings settings;
	private Kotamaops kotamaops;
	private ZoneId zoneId;
	private LocalDateTime currentLocalDateTime, awalLocalDateTime, akhirLocalDateTime;
	private boolean isTwAwalAkhirProper;
	
	private final long SETTINGS_DEFAULT_ID = 1L;
	private final static Logger log = Logger.getLogger(KerugianRekapInfoControl.class);
	
	public void onCreate$kerugianRekapInfoWin(Event event) throws Exception {
		setSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		
		setKotamaops(
				getSettings().getSelectedKotamaops());
		
		// set the timezone for this kotamaops
		int timezoneOrdinal = getKotamaops().getTimeZone().getValue();
		setZoneId(getKotamaops().getTimeZone().toZoneId(timezoneOrdinal));					
		
		// current datetime -- according to time zone (not just id)
		setCurrentLocalDateTime(getLocalDateTime(getZoneId()));				
		
		formTitleLabel.setValue(
				"Rekapitulasi | Kerugian-Keuntungan - Kotamaops: "+
						getKotamaops().getKotamaopsName());
		
		// set tw with current time
		setTwAwalAkhir(360L);
		
		// start with kotamaops
		loadKotamaopsCombobox();
		
		// pihak
		loadPihakCombobox();
	}

	private void setTwAwalAkhir(long minDays) {
		// awal - date minus 90 days
		LocalDate twAwalDate = minusDate(minDays, asLocalDate(getCurrentLocalDateTime()));
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
				"/dialogs/DatetimeWinDialog.zul", kerugianRekapInfoWin, args);
		datetimeWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				DatetimeData datetimeData = (DatetimeData) event.getData();
				
				log.info(datetimeData.getLocalDateTime());
				setAwalLocalDateTime(datetimeData.getLocalDateTime());
				
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
				"/dialogs/DatetimeWinDialog.zul", kerugianRekapInfoWin, args);
		datetimeWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				DatetimeData datetimeData = (DatetimeData) event.getData();
				
				log.info(datetimeData.getLocalDateTime());
				setAkhirLocalDateTime(datetimeData.getLocalDateTime());
				
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
		Comboitem comboitem;
		if (getKotamaops().getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0) {
			// populate the combobox with all the kotamaops, added to PUSDALOPS
			Kotamaops kotamaopsByProxy = 
					getKotamaopsDao().findKotamaopsKotamaopsByProxy(getKotamaops().getId());
			List<Kotamaops> kotamaopsList = kotamaopsByProxy.getKotamaops();
			
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
			comboitem = new Comboitem();
			comboitem.setLabel(getKotamaops().getKotamaopsName());
			comboitem.setValue(getKotamaops());
			comboitem.setParent(kotamaopsCombobox);

			kotamaopsCombobox.setSelectedItem(comboitem);
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

	private void loadPihakCombobox() {
		Comboitem comboitem;
		for (Pihak pihak : Pihak.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(pihak.toString());
			comboitem.setValue(pihak);
			comboitem.setParent(pihakCombobox);
		}
		pihakCombobox.setSelectedIndex(0);
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
			kecamatanCombobox.setValue("");
			kelurahanCombobox.setValue("");

			kecamatanCombobox.getItems().clear();
			kelurahanCombobox.getItems().clear();			

			kecamatanCombobox.setDisabled(true);
			kelurahanCombobox.setDisabled(true);
			break;
		case 1:
			// when kecamatan is selected
			kelurahanCombobox.setValue("");
			kelurahanCombobox.getItems().clear();			
			kelurahanCombobox.setDisabled(true);

			break;

		default:
			break;
		}
	}
	
	public void onClick$executeButton(Event event) throws Exception {
		// is twAwalAkhirProper? -- if yes, get twAwal and twAkhir
		if (!isTwAwalAkhirProper()) {
			throw new Exception("TW Awal dan Akhir BELUM sesuai");
		}
		
		Comboitem pihakComboitem = pihakCombobox.getSelectedItem();
		Comboitem selKotamaopsComboitem = kotamaopsCombobox.getSelectedItem();
		Comboitem selPropinsiComboitem = propinsiCombobox.getSelectedItem();
		Comboitem selKabupatenKotComboitem = kabupatenCombobox.getSelectedItem();
		Comboitem selKecamatanComboitem = kecamatanCombobox.getSelectedItem();
		Comboitem selKelurahanComboitem = kelurahanCombobox.getSelectedItem();
		
		List<Kerugian> allKerugians;
		BigInteger countKerugianPers, countKerugianMatr;
		if (selKotamaopsComboitem.getValue()==null) {
			countKerugianPers = getKejadianRekapDao().countKerugianByTipe(TipeKerugian.Personil, 
					pihakComboitem.getValue(), 
					getAkhirLocalDateTime(), getAwalLocalDateTime());
			countKerugianMatr = getKejadianRekapDao().countKerugianByTipe(TipeKerugian.Material, 
					pihakComboitem.getValue(), 
					getAkhirLocalDateTime(), getAwalLocalDateTime());
			allKerugians = findAllTipeKerugians(
					pihakComboitem.getValue(), getAkhirLocalDateTime(), getAwalLocalDateTime());
		} else if (selPropinsiComboitem.getValue()==null) {
			countKerugianPers = getKejadianRekapDao().countKerugianByTipe(TipeKerugian.Personil, 
					selKotamaopsComboitem.getValue(), 
					pihakComboitem.getValue(), 
					getAkhirLocalDateTime(), getAwalLocalDateTime());
			countKerugianMatr = getKejadianRekapDao().countKerugianByTipe(TipeKerugian.Material, 
					selKotamaopsComboitem.getValue(), 
					pihakComboitem.getValue(), 
					getAkhirLocalDateTime(), getAwalLocalDateTime());
			allKerugians = findAllTipeKerugians(selKotamaopsComboitem.getValue(), 
					pihakComboitem.getValue(), getAkhirLocalDateTime(), getAwalLocalDateTime());
		} else if (selKabupatenKotComboitem.getValue()==null) {
			countKerugianPers = getKejadianRekapDao().countKerugianByTipe(TipeKerugian.Personil,
					selKotamaopsComboitem.getValue(),
					selPropinsiComboitem.getValue(),
					pihakComboitem.getValue(), 
					getAkhirLocalDateTime(), getAwalLocalDateTime());
			countKerugianMatr = getKejadianRekapDao().countKerugianByTipe(TipeKerugian.Material,
					selKotamaopsComboitem.getValue(),
					selPropinsiComboitem.getValue(),
					pihakComboitem.getValue(), 
					getAkhirLocalDateTime(), getAwalLocalDateTime());
			allKerugians = findAllTipeKerugians(selKotamaopsComboitem.getValue(), selPropinsiComboitem.getValue(), 
					pihakComboitem.getValue(), getAkhirLocalDateTime(), getAwalLocalDateTime());

		} else if (selKecamatanComboitem.getValue()==null) {
			countKerugianPers = getKejadianRekapDao().countKerugianByTipe(TipeKerugian.Personil,
					selKotamaopsComboitem.getValue(),
					selPropinsiComboitem.getValue(),
					selKabupatenKotComboitem.getValue(),
					pihakComboitem.getValue(), 
					getAkhirLocalDateTime(), getAwalLocalDateTime());
			countKerugianMatr = getKejadianRekapDao().countKerugianByTipe(TipeKerugian.Material,
					selKotamaopsComboitem.getValue(),
					selPropinsiComboitem.getValue(),
					selKabupatenKotComboitem.getValue(),
					pihakComboitem.getValue(), 
					getAkhirLocalDateTime(), getAwalLocalDateTime());
			allKerugians = findAllTipeKerugians(selKotamaopsComboitem.getValue(), selPropinsiComboitem.getValue(), 
					selKabupatenKotComboitem.getValue(), 
					pihakComboitem.getValue(), getAkhirLocalDateTime(), getAwalLocalDateTime());	
			
		} else if (selKelurahanComboitem.getValue()==null) {
			countKerugianPers = getKejadianRekapDao().countKerugianByTipe(TipeKerugian.Personil,
					selKotamaopsComboitem.getValue(),
					selPropinsiComboitem.getValue(),
					selKabupatenKotComboitem.getValue(),
					selKecamatanComboitem.getValue(),
					pihakComboitem.getValue(), 
					getAkhirLocalDateTime(), getAwalLocalDateTime());
			countKerugianMatr = getKejadianRekapDao().countKerugianByTipe(TipeKerugian.Material,
					selKotamaopsComboitem.getValue(),
					selPropinsiComboitem.getValue(),
					selKabupatenKotComboitem.getValue(),
					selKecamatanComboitem.getValue(),
					pihakComboitem.getValue(), 
					getAkhirLocalDateTime(), getAwalLocalDateTime());
			allKerugians = findAllTipeKerugians(selKotamaopsComboitem.getValue(), selPropinsiComboitem.getValue(), 
					selKabupatenKotComboitem.getValue(), selKecamatanComboitem.getValue(),
					pihakComboitem.getValue(), getAkhirLocalDateTime(), getAwalLocalDateTime());			
		} else {
			countKerugianPers = getKejadianRekapDao().countKerugianByTipe(TipeKerugian.Personil,
					selKotamaopsComboitem.getValue(),
					selPropinsiComboitem.getValue(),
					selKabupatenKotComboitem.getValue(),
					selKecamatanComboitem.getValue(),
					selKelurahanComboitem.getValue(),
					pihakComboitem.getValue(), 
					getAkhirLocalDateTime(), getAwalLocalDateTime());
			countKerugianMatr = getKejadianRekapDao().countKerugianByTipe(TipeKerugian.Material,
					selKotamaopsComboitem.getValue(),
					selPropinsiComboitem.getValue(),
					selKabupatenKotComboitem.getValue(),
					selKecamatanComboitem.getValue(),
					selKelurahanComboitem.getValue(),
					pihakComboitem.getValue(), 
					getAkhirLocalDateTime(), getAwalLocalDateTime());
			allKerugians = findAllTipeKerugians(selKotamaopsComboitem.getValue(), selPropinsiComboitem.getValue(), 
					selKabupatenKotComboitem.getValue(), selKecamatanComboitem.getValue(), selKelurahanComboitem.getValue(),
					pihakComboitem.getValue(), getAkhirLocalDateTime(), getAwalLocalDateTime());
			
		}
		log.info("Kerugian Personil: "+countKerugianPers);
		log.info("Kerugian Material: "+countKerugianMatr);
		log.info("Kerugian All: "+allKerugians.size());
		
		kerugianPersTextbox.setValue(countKerugianPers.toString());
		kerugianMatrTextbox.setValue(countKerugianMatr.toString());				
		kerugianTotalTextbox.setValue(String.valueOf(allKerugians.size()));
		
		kerugianListbox.setModel(new ListModelList<Kerugian>(allKerugians));
		kerugianListbox.setItemRenderer(getAllKerugiansListitemRenderer());
	}

	private List<Kerugian> findAllTipeKerugians(
			Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception {
		List<Kerugian> allKerugians = new ArrayList<Kerugian>();
		List<Kejadian> kejadianList = getKejadianRekapDao().findAllKejadian(asDate(twAkhir), asDate(twAwal));
		for (Kejadian kejadian : kejadianList) {
			Kejadian kejadianKerugianByProxy = getKejadianDao().findKejadianKerugiansByProxy(kejadian.getId());			
  			List<Kerugian> kerugianList = kejadianKerugianByProxy.getKerugians();
			for (Kerugian kerugian : kerugianList) {
				if (kerugian.getParaPihak().ordinal()==pihak.ordinal()) { 
					allKerugians.add(kerugian);					
				}
			}
		
		}
		return allKerugians;
	}
	
	private List<Kerugian> findAllTipeKerugians(Kotamaops kotamaops, 
			Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception {
		List<Kerugian> allKerugians = new ArrayList<Kerugian>();
		List<Kejadian> kejadianList = getKejadianRekapDao().findAllKejadian(kotamaops, asDate(twAkhir), asDate(twAwal));
		for (Kejadian kejadian : kejadianList) {
			Kejadian kejadianKerugianByProxy = getKejadianDao().findKejadianKerugiansByProxy(kejadian.getId());			
  			List<Kerugian> kerugianList = kejadianKerugianByProxy.getKerugians();
			for (Kerugian kerugian : kerugianList) {
				if (kerugian.getParaPihak().ordinal()==pihak.ordinal()) { 
					allKerugians.add(kerugian);					
				}
			}
		
		}
		return allKerugians;
	}

	private List<Kerugian> findAllTipeKerugians(Kotamaops kotamaops, Propinsi propinsi, 
			Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception {
		List<Kerugian> allKerugians = new ArrayList<Kerugian>();
		List<Kejadian> kejadianList = getKejadianRekapDao().findAllKejadian(kotamaops, propinsi, asDate(twAkhir), asDate(twAwal));
		for (Kejadian kejadian : kejadianList) {
			Kejadian kejadianKerugianByProxy = getKejadianDao().findKejadianKerugiansByProxy(kejadian.getId());			
  			List<Kerugian> kerugianList = kejadianKerugianByProxy.getKerugians();
			for (Kerugian kerugian : kerugianList) {
				if (kerugian.getParaPihak().ordinal()==pihak.ordinal()) { 
					allKerugians.add(kerugian);					
				}
			}
		
		}
		return allKerugians;
	}

	private List<Kerugian> findAllTipeKerugians(Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, 
			Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception {
		
		List<Kerugian> allKerugians = new ArrayList<Kerugian>();
		List<Kejadian> kejadianList = getKejadianRekapDao().findAllKejadian(kotamaops, propinsi, kabupatenKot, asDate(twAkhir), asDate(twAwal));
		for (Kejadian kejadian : kejadianList) {
			Kejadian kejadianKerugianByProxy = getKejadianDao().findKejadianKerugiansByProxy(kejadian.getId());			
  			List<Kerugian> kerugianList = kejadianKerugianByProxy.getKerugians();
			for (Kerugian kerugian : kerugianList) {
				if (kerugian.getParaPihak().ordinal()==pihak.ordinal()) { 
					allKerugians.add(kerugian);					
				}
			}
		
		}
		return allKerugians;
	}
	
	private List<Kerugian> findAllTipeKerugians(Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot,
			Kecamatan kecamatan, 
			Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception {

		List<Kerugian> allKerugians = new ArrayList<Kerugian>();
		List<Kejadian> kejadianList = getKejadianRekapDao().findAllKejadian(kotamaops, propinsi, kabupatenKot, kecamatan, asDate(twAkhir), asDate(twAwal));
		for (Kejadian kejadian : kejadianList) {
			Kejadian kejadianKerugianByProxy = getKejadianDao().findKejadianKerugiansByProxy(kejadian.getId());			
  			List<Kerugian> kerugianList = kejadianKerugianByProxy.getKerugians();
			for (Kerugian kerugian : kerugianList) {
				if (kerugian.getParaPihak().ordinal()==pihak.ordinal()) { 
					allKerugians.add(kerugian);					
				}
			}
		
		}
		return allKerugians;
	}

	private List<Kerugian> findAllTipeKerugians(Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot,
			Kecamatan kecamatan, Kelurahan kelurahan, 
			Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception {

		List<Kerugian> allKerugians = new ArrayList<Kerugian>();
		List<Kejadian> kejadianList = getKejadianRekapDao().findAllKejadian(kotamaops, propinsi, kabupatenKot, kecamatan, kelurahan, asDate(twAkhir), asDate(twAwal));
		for (Kejadian kejadian : kejadianList) {
			Kejadian kejadianKerugianByProxy = getKejadianDao().findKejadianKerugiansByProxy(kejadian.getId());			
  			List<Kerugian> kerugianList = kejadianKerugianByProxy.getKerugians();
			for (Kerugian kerugian : kerugianList) {
				if (kerugian.getParaPihak().ordinal()==pihak.ordinal()) { 
					allKerugians.add(kerugian);					
				}
			}
		
		}
		return allKerugians;

	}
	
	
	private ListitemRenderer<Kerugian> getAllKerugiansListitemRenderer() {

		return new ListitemRenderer<Kerugian>() {
			
			@Override
			public void render(Listitem item, Kerugian kerugian, int index) throws Exception {
				Listcell lc;
				
				// Tipe
				lc = new Listcell(kerugian.getTipeKerugian().toString());
				lc.setParent(item);			
				
				// Nama Pers/Mat
				lc = new Listcell(kerugian.getNamaMaterial());
				lc.setStyle("white-space:nowrap;");
				lc.setParent(item);
								
				// ID
				lc = new Listcell(kerugian.getLembagaTerkait().toString());
				lc.setParent(item);
				
				// Jenis
				lc = new Listcell(kerugian.getKerugianJenis().getNamaJenis());
				lc.setParent(item);
				
				// Kondisi
				lc = new Listcell(kerugian.getKerugianKondisi().getNamaKondisi());
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
		return isTwAwalAkhirProper;
	}

	public void setTwAwalAkhirProper(boolean isTwAwalAkhirProper) {
		this.isTwAwalAkhirProper = isTwAwalAkhirProper;
	}

	public KejadianRekapDao getKejadianRekapDao() {
		return kejadianRekapDao;
	}

	public void setKejadianRekapDao(KejadianRekapDao kejadianRekapDao) {
		this.kejadianRekapDao = kejadianRekapDao;
	}

	public KejadianDao getKejadianDao() {
		return kejadianDao;
	}

	public void setKejadianDao(KejadianDao kejadianDao) {
		this.kejadianDao = kejadianDao;
	}
}
