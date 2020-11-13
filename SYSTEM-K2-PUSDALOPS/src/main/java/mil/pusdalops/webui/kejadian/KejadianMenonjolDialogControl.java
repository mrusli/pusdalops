package mil.pusdalops.webui.kejadian;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kejadian.KejadianJenis;
import mil.pusdalops.domain.kejadian.KejadianMotif;
import mil.pusdalops.domain.kejadian.KejadianPelaku;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.domain.serial.DocumentSerialNumber;
import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;
import mil.pusdalops.domain.wilayah.Kecamatan;
import mil.pusdalops.domain.wilayah.Kelurahan;
import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.persistence.kabupaten_kotamadya.dao.Kabupaten_KotamadyaDao;
import mil.pusdalops.persistence.kecamatan.dao.KecamatanDao;
import mil.pusdalops.persistence.kejadian.dao.KejadianDao;
import mil.pusdalops.persistence.kejadian.jenis.dao.KejadianJenisDao;
import mil.pusdalops.persistence.kejadian.motif.dao.KejadianMotifDao;
import mil.pusdalops.persistence.kejadian.pelaku.dao.KejadianPelakuDao;
import mil.pusdalops.persistence.kelurahan.dao.KelurahanDao;
import mil.pusdalops.persistence.kotamaops.dao.KotamaopsDao;
import mil.pusdalops.persistence.propinsi.dao.PropinsiDao;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.common.SerialNumberGenerator;
import mil.pusdalops.webui.common.TwConversion;
import mil.pusdalops.webui.dialogs.DatetimeData;

public class KejadianMenonjolDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8228664782095205749L;
	
	private SerialNumberGenerator serialNumberGenerator;
	private TwConversion twConversion;
	private KotamaopsDao kotamaopsDao;	
	private PropinsiDao propinsiDao;
	private Kabupaten_KotamadyaDao kabupaten_KotamadyaDao;
	private KecamatanDao kecamatanDao;
	private KelurahanDao kelurahanDao;
	private KejadianDao kejadianDao;
	private KejadianJenisDao kejadianJenisDao;
	private KejadianMotifDao kejadianMotifDao;
	private KejadianPelakuDao kejadianPelakuDao;
	
	private Window kejadianMenonjolDialogWin;
	private Textbox kejadianIdTextbox, twBuatTahunTextbox, twBuatTanggalJamTextbox,
		twJadiTahunTextbox, twJadiTanggalJamTextbox, twJadiTimeZoneTextbox,
		kronologisTextbox, koordGpsTextbox, koordPetaTextbox, bujurLintangTextbox,
		kampungTextbox, jalanTextbox, keteranganPelakuTextbox, sasaranTextbox;
	private Combobox twBuatTimeZoneCombobox, kotamaopsCombobox, propCombobox,
		kabupatenCombobox, kecamatanCombobox, kelurahanCombobox, jenisKejadianCombobox,
		motifKejadianCombobox, pelakuKejadianCombobox;
	private Label idLabel;
	
	private KejadianData kejadianData;
	private Kotamaops settingsKotamaops;
	private Kejadian kejadian;
	private ZoneId zoneId;
	private DocumentSerialNumber documentSerialNumber;
	private LocalDateTime currentLocalDateTime;
	
	private Kotamaops selectedKotamaops;
	private Propinsi selectedPropinsi;
	private Kabupaten_Kotamadya selectedKabupatenKot;
	private Kecamatan selectedKecamatan;
	
	private final String NO_INFO = "-Tidak Ada Info-";
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setKejadianData(
				(KejadianData) Executions.getCurrent().getArg().get("kejadianData"));
	}

	public void onCreate$kejadianMenonjolDialogWin(Event event) throws Exception {
		
/*
 * 		if (getKejadianData().getKejadian().getId().compareTo(Long.MIN_VALUE)==0) {
			// kotamaops -- will determine the timezone and documentCode
			setKotamaops(
					getKejadian().getKotamaops());
			setSettingsKotamaops();
		} else {
			Kejadian kejadianByProxy = getKejadianDao().findKejadianKotamaopsByProxy(getKejadian().getId());
			setKotamaops(
					kejadianByProxy.getKotamaops());			
		}
*/		
		
		// kotamaops -- according to the login / settings
		setSettingsKotamaops(getKejadianData().getSettingsKotamaops());
		
		// set the timezone for this kotamaops
		int timezoneOrdinal = getSettingsKotamaops().getTimeZone().ordinal();
		setZoneId(getSettingsKotamaops().getTimeZone().toZoneId(timezoneOrdinal));			
		
		// set kejadian object
		setKejadian(getKejadianData().getKejadian());
		
		// current datetime -- according to time zone (not just id)
		setCurrentLocalDateTime(getLocalDateTime(getZoneId()));		
		
		// load zonawaktu twBuatTimeZoneCombobox
		loadTwBuatZonaWaktuCombobox();
		
		// load kotamaopsCombobox
		loadKotamaopsCombobox();
		
		// load jenis kejadian
		loadJenisKejadianCombobox();

		// load motif kejadian
		loadMotifKejadianCombobox();
		
		// load pelaku kejadian
		loadPelakuKejadianCombobox();
		
		// load pelaku kejadian
		
		// display
		displayKejadianData();
	}

	public void onClick$twJadiRubahButton(Event event) throws Exception {
		// create the data object
		DatetimeData datetimeData = new DatetimeData();
		// set the dialog title
		datetimeData.setDialogWinTitle("Rubah TW Kejadian");
		
		if (getKejadian().getId().compareTo(Long.MIN_VALUE)==0) {
			datetimeData.setTimezoneInd(getSettingsKotamaops().getTimeZone());
			// check if the user actually selects the kotamaops
			TimezoneInd selKotamaopsTimezoneInd = 
					(TimezoneInd) twJadiTimeZoneTextbox.getAttribute("twJadiTimeZoneInd");
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
			TimezoneInd timezoneInd = getKejadian().getTwKejadianTimezone();
			int timezoneIndOrdinal = timezoneInd.ordinal();
			ZoneId zoneId = timezoneInd.toZoneId(timezoneIndOrdinal);
			
			datetimeData.setTimezoneInd(timezoneInd);
			datetimeData.setZoneId(zoneId);
			datetimeData.setLocalDateTime(asLocalDateTime(getKejadian().getTwKejadianDateTime()));
		}
		
		Map<String, DatetimeData> args = Collections.singletonMap("datetimeData", datetimeData);
		Window datetimeWin = (Window) Executions.createComponents(
				"/dialogs/DatetimeWinDialog.zul", kejadianMenonjolDialogWin, args);
		datetimeWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				DatetimeData datetimeData = (DatetimeData) event.getData();
				// System.out.println(datetimeData.toString());
				
				// year
				twJadiTahunTextbox.setValue(getLocalDateTimeString(datetimeData.getLocalDateTime(), "YYYY"));
				
				// mmdd.HHmm
				twJadiTanggalJamTextbox.setValue(					
						getLocalDateTimeString(datetimeData.getLocalDateTime(), "MM")+
						getLocalDateTimeString(datetimeData.getLocalDateTime(), "dd")+"."+
						getLocalDateTimeString(datetimeData.getLocalDateTime(), "HH")+
						getLocalDateTimeString(datetimeData.getLocalDateTime(), "mm"));
				
				// zona waktu
				twJadiTimeZoneTextbox.setValue(datetimeData.getTimezoneInd().toString());
				// save the object in the attribute
				twJadiTimeZoneTextbox.setAttribute("twJadiTimeZoneInd", datetimeData.getTimezoneInd());
			}
		});
		
		datetimeWin.doModal();
	}
	
	private void loadTwBuatZonaWaktuCombobox() {
		Comboitem comboitem;
		
		for (TimezoneInd timezoneInd : TimezoneInd.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(timezoneInd.toString());
			comboitem.setValue(timezoneInd);
			comboitem.setParent(twBuatTimeZoneCombobox);
		}
	}
	
	private void loadKotamaopsCombobox() throws Exception {
		if (getSettingsKotamaops().getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0) {
			// populate the combobox with all the kotamaops, associated with this PUSDALOPS
			Kotamaops kotamaopsByProxy = 
					getKotamaopsDao().findKotamaopsKotamaopsByProxy(getSettingsKotamaops().getId());
			List<Kotamaops> kotamaopsList = kotamaopsByProxy.getKotamaops();
			
			Comboitem comboitem;
			for (Kotamaops kotamaops : kotamaopsList) {
				comboitem = new Comboitem();
				comboitem.setLabel(kotamaops.getKotamaopsName());
				comboitem.setValue(kotamaops);
				comboitem.setParent(kotamaopsCombobox);
			}
			
		} else {
			// must create comboitem
			Comboitem comboitem;
			comboitem = new Comboitem();
			comboitem.setLabel(getSettingsKotamaops().getKotamaopsName());
			comboitem.setValue(getSettingsKotamaops());
			comboitem.setParent(kotamaopsCombobox);
			// no more kotamaops selection
			// kotamaopsCombobox.setValue(getSettingsKotamaops().getKotamaopsName());
			kotamaopsCombobox.setSelectedItem(comboitem);
			kotamaopsCombobox.setDisabled(true);
			
			// display the propinsi of the default kotamaops 
			Kotamaops kotamaopsByProxy = 
					getKotamaopsDao().findKotamaopsPropinsiByProxy(getSettingsKotamaops().getId());
			List<Propinsi> propinsiList = kotamaopsByProxy.getPropinsis();
			
			for (Propinsi propinsi : propinsiList) {
				comboitem = new Comboitem();
				comboitem.setLabel(propinsi.getNamaPropinsi());
				comboitem.setValue(propinsi);
				comboitem.setParent(propCombobox);
			}
		}
	}	
	
	private void loadJenisKejadianCombobox() throws Exception {
		List<KejadianJenis> kejadianJenisList = getKejadianJenisDao().findAllKejadianJenisOrderBy(true);
		
		Comboitem comboitem;
		for (KejadianJenis kejadianJenis : kejadianJenisList) {
			comboitem = new Comboitem();
			comboitem.setLabel(kejadianJenis.getNamaJenis());
			comboitem.setValue(kejadianJenis);
			comboitem.setParent(jenisKejadianCombobox);
		}
		// create comboitem to add to the jenis kejadian list
		// -- in case a new jenis kejadian not in the list
		comboitem = new Comboitem();
		comboitem.setLabel("Tambah Jenis Kejadian...");
		comboitem.setValue(null);
		comboitem.setParent(jenisKejadianCombobox);		
	}	
	
	private void loadMotifKejadianCombobox() throws Exception {
		List<KejadianMotif> kejadianMotifList = getKejadianMotifDao().findAllKejadianMotifOrderBy(true);
		
		Comboitem comboitem;
		for (KejadianMotif kejadianMotif : kejadianMotifList) {
			comboitem = new Comboitem();
			comboitem.setLabel(kejadianMotif.getNamaMotif());
			comboitem.setValue(kejadianMotif);
			comboitem.setParent(motifKejadianCombobox);
		}
		// create comboitem to add to the motif kejadian list
		// -- in case a new motif kejadian not in the list
		comboitem = new Comboitem();
		comboitem.setLabel("Tambah Motif Kejadian...");
		comboitem.setValue(null);
		comboitem.setParent(motifKejadianCombobox);		
	}

	private void loadPelakuKejadianCombobox() throws Exception {
		List<KejadianPelaku> kejadianPelakuList = getKejadianPelakuDao().findAllKejadianPelaku();
		
		Comboitem comboitem;
		for (KejadianPelaku kejadianPelaku : kejadianPelakuList) {
			comboitem = new Comboitem();
			comboitem.setLabel(kejadianPelaku.getNamaPelaku());
			comboitem.setValue(kejadianPelaku);
			comboitem.setParent(pelakuKejadianCombobox);
		}		
	}
	
	
	private void resetOtherComboboxes(int numComboboxes) {
		switch (numComboboxes) {
		case 4:
			// when kotamaops is selected
			propCombobox.setValue("");
			kabupatenCombobox.setValue("");
			kecamatanCombobox.setValue("");
			kelurahanCombobox.setValue("");
			
			propCombobox.getItems().clear();
			kabupatenCombobox.getItems().clear();
			kecamatanCombobox.getItems().clear();
			kelurahanCombobox.getItems().clear();
			break;
		case 3:
			// when propinsi is selected
			kabupatenCombobox.setValue("");
			kecamatanCombobox.setValue("");
			kelurahanCombobox.setValue("");

			kabupatenCombobox.getItems().clear();
			kecamatanCombobox.getItems().clear();
			kelurahanCombobox.getItems().clear();			
			break;
		case 2:
			// when kabupaten is selected
			kecamatanCombobox.setValue("");
			kelurahanCombobox.setValue("");

			kecamatanCombobox.getItems().clear();
			kelurahanCombobox.getItems().clear();			
			break;
		case 1:
			// when kecamatan is selected
			kelurahanCombobox.setValue("");
			kelurahanCombobox.getItems().clear();			
			break;

		default:
			break;
		}
	}
	
	public void onSelect$kotamaopsCombobox(Event event) throws Exception {
		resetOtherComboboxes(4);
		
		// only for users in PUSDALOPS
		// Kotamaops selKotamaops = 
		selectedKotamaops = kotamaopsCombobox.getSelectedItem().getValue();

		// set timezone kejadian according to selected kotamaops
		twJadiTimeZoneTextbox.setValue(selectedKotamaops.getTimeZone().toString());
		twJadiTimeZoneTextbox.setAttribute("twJadiTimeZoneInd", selectedKotamaops.getTimeZone());

		loadPropinsiCombobox(selectedKotamaops);
	}

	private void loadPropinsiCombobox(Kotamaops selKotamaops) throws Exception {
		Kotamaops kotamaopsByProxy = 
				getKotamaopsDao().findKotamaopsPropinsiByProxy(selKotamaops.getId());
		List<Propinsi> propinsiList = kotamaopsByProxy.getPropinsis();
		
		// init - clear previously stored propinsi
		// propCombobox.getItems().clear();
		
		Comboitem comboitem;
		for (Propinsi propinsi : propinsiList) {
			comboitem = new Comboitem();
			comboitem.setLabel(propinsi.getNamaPropinsi());
			comboitem.setValue(propinsi);
			comboitem.setParent(propCombobox);
		}
	}
	
	public void onSelect$propCombobox(Event event) throws Exception {
		resetOtherComboboxes(3);
		// Propinsi selPropinsi = propCombobox.getSelectedItem().getValue();
		selectedPropinsi = propCombobox.getSelectedItem().getValue();
		
		loadKabupatenCombobox(selectedPropinsi);
	}

	private void loadKabupatenCombobox(Propinsi selPropinsi) throws Exception {
		Propinsi selPropinsiByProxy = 
				getPropinsiDao().findKabupatenKotamadyaByProxy(selPropinsi.getId());
		List<Kabupaten_Kotamadya> kabupaten_Kotamadyas = 
				selPropinsiByProxy.getKabupatenkotamadyas();
		
		Comboitem comboitem;
		for (Kabupaten_Kotamadya kabupaten_Kotamadya : kabupaten_Kotamadyas) {
			comboitem = new Comboitem();
			comboitem.setLabel(kabupaten_Kotamadya.getNamaKabupaten());
			comboitem.setValue(kabupaten_Kotamadya);
			comboitem.setParent(kabupatenCombobox);
		}			
		// create comboitem to add to the kabupaten
		// -- in case the kabupaten not in the list
		comboitem = new Comboitem();
		comboitem.setLabel("Tambah Kabupaten/Kot...");
		comboitem.setValue(null);
		comboitem.setParent(kabupatenCombobox);
	}
	
	public void onSelect$kabupatenCombobox(Event event) throws Exception {
		if (kabupatenCombobox.getSelectedItem().getValue()==null) {
			// display textentry dialog
			Map<String, String> args = Collections.singletonMap("name", "Kabupaten/Kot:");
			Window textEntryWindow = (Window) Executions.createComponents("/kejadian/WilayahTextEntry.zul", kejadianMenonjolDialogWin, args);
			textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					String namaKabupaten = (String) event.getData();
										
					List<Kabupaten_Kotamadya> kabupatenKotList;
					Propinsi propByProxy;
					Propinsi selPropinsi = propCombobox.getSelectedItem().getValue();
					propByProxy = getPropinsiDao().findKabupatenKotamadyaByProxy(selPropinsi.getId());
					kabupatenKotList = propByProxy.getKabupatenkotamadyas();
					// check whether the name exists in the list
					for (Kabupaten_Kotamadya kabupatenKotExisting : kabupatenKotList) {
						if (kabupatenKotExisting.getNamaKabupaten().compareTo(namaKabupaten)==0) {
							throw new Exception("Kabupaten/Kot "+namaKabupaten+" sudah terdaftar sebelumnya.");
						}
					}

					// create a new kabupaten object
					Kabupaten_Kotamadya kabupatenKot = new Kabupaten_Kotamadya();
					kabupatenKot.setCreatedAt(asDate(getCurrentLocalDateTime()));
					kabupatenKot.setEditedAt(asDate(getCurrentLocalDateTime()));
					kabupatenKot.setNamaKabupaten(namaKabupaten);
					
					// add the kabupaten to the propinsi
					kabupatenKotList.add(kabupatenKot);
					
					// update propinsi
					getPropinsiDao().update(propByProxy);
					
					// notif
					Clients.showNotification("Kabupaten berhasil ditambahkan dibawah Propinsi");
					
					// display the kabupaten
					resetOtherComboboxes(3);
					
					loadKabupatenCombobox(selectedPropinsi);
/*					kabupatenCombobox.getItems().clear();
					
					propByProxy = 
							getPropinsiDao().findKabupatenKotamadyaByProxy(selPropinsi.getId());
					kabupatenKotList = propByProxy.getKabupatenkotamadyas();
					Comboitem comboitem;
					for (Kabupaten_Kotamadya kabupatenKotamadya : kabupatenKotList) {
						comboitem = new Comboitem();
						comboitem.setLabel(kabupatenKotamadya.getNamaKabupaten());
						comboitem.setValue(kabupatenKotamadya);
						comboitem.setParent(kabupatenCombobox);
					}
*/
					for (Comboitem comboitemKab : kabupatenCombobox.getItems()) {
						if (comboitemKab.getLabel().compareTo(namaKabupaten)==0) {
							kabupatenCombobox.setSelectedItem(comboitemKab);
							
							selectedKabupatenKot = comboitemKab.getValue();
							
							break;
						}						
					}
					loadKecamatanCombobox(selectedKabupatenKot);
				}
			});
			textEntryWindow.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					resetOtherComboboxes(3);
					
					loadKabupatenCombobox(selectedPropinsi);
				}
			});
			textEntryWindow.doModal();
		} else {
			resetOtherComboboxes(2);
			// Kabupaten_Kotamadya selKabupaten = kabupatenCombobox.getSelectedItem().getValue();
			selectedKabupatenKot = kabupatenCombobox.getSelectedItem().getValue();
			
			loadKecamatanCombobox(selectedKabupatenKot);			
		}
	}

	private void loadKecamatanCombobox(Kabupaten_Kotamadya selKabupaten) throws Exception {
		Kabupaten_Kotamadya kabupatenKotByProxy = 
				getKabupaten_KotamadyaDao().findKecamatanByProxy(selKabupaten.getId());
		List<Kecamatan> kecamatanList = 
				kabupatenKotByProxy.getKecamatans();
		if (selKabupaten.getNamaKabupaten().compareTo(NO_INFO)==0) {
			if (kecamatanList.isEmpty()) {
				// create comboitem to add to the kabupaten_kotmadya list
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel("Tambah Kecamatan...");
				comboitem.setValue(null);
				comboitem.setParent(kecamatanCombobox);
			} else {
				// no info for kecamatan
				noInfoForKecamatan(kecamatanList);
				// no info for kelurahan
				noInfoForKelurahan();
			}
		} else {			
			Comboitem comboitem;
			for (Kecamatan kecamatan : kecamatanList) {
				comboitem = new Comboitem();
				comboitem.setLabel(kecamatan.getNamaKecamatan());
				comboitem.setValue(kecamatan);
				comboitem.setParent(kecamatanCombobox);
			}
			// create comboitem to add to the propinsi
			// -- in case the kabupaten not in the list
			comboitem = new Comboitem();
			comboitem.setLabel("Tambah Kecamatan...");
			comboitem.setValue(null);
			comboitem.setParent(kecamatanCombobox);
		}
	}

	private void noInfoForKecamatan(List<Kecamatan> kecamatanList) {
		for (Kecamatan kecamatan : kecamatanList) {
			if (kecamatan.getNamaKecamatan().compareTo(NO_INFO)==0) {
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(NO_INFO);
				comboitem.setValue(kecamatan);
				comboitem.setParent(kecamatanCombobox);
				break;
			}
		}
		for (Comboitem comboitem : kecamatanCombobox.getItems()) {
			if (comboitem.getLabel().compareTo(NO_INFO)==0) {
				kecamatanCombobox.setSelectedItem(comboitem);
			}
		}
	}

	private void noInfoForKelurahan() throws Exception {
		Kecamatan selKecmatan = kecamatanCombobox.getSelectedItem().getValue();
		Kecamatan kecamatanByProxy = 
				getKecamatanDao().findKelurahanByProxy(selKecmatan.getId());
		List<Kelurahan> kelurahanList =
				kecamatanByProxy.getKelurahans();
		if (kelurahanList.isEmpty()) {
			// create comboitem to add to the kecamatan list
			Comboitem comboitem = new Comboitem();
			comboitem.setLabel("Tambah Kelurahan...");
			comboitem.setValue(null);
			comboitem.setParent(kecamatanCombobox);					
		} else {
			for (Kelurahan kelurahan : kelurahanList) {
				if (kelurahan.getNamaKelurahan().compareTo(NO_INFO)==0) {
					Comboitem comboitem = new Comboitem();
					comboitem.setLabel(NO_INFO);
					comboitem.setValue(kelurahan);
					comboitem.setParent(kelurahanCombobox);
					break;							
				}
			}
			for (Comboitem comboitem : kelurahanCombobox.getItems()) {
				if (comboitem.getLabel().compareTo(NO_INFO)==0) {
					kelurahanCombobox.setSelectedItem(comboitem);
					break;
				}
			}
		}
	}
	
	public void onSelect$kecamatanCombobox(Event event) throws Exception {
		if (kecamatanCombobox.getSelectedItem().getValue()==null) {
			// display textentry dialog
			Map<String, String> args = Collections.singletonMap("name", "Kecamatan:");
			Window textEntryWindow = (Window) Executions.createComponents("/kejadian/WilayahTextEntry.zul", kejadianMenonjolDialogWin, args);
			textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					String namaKecamatan = (String) event.getData();

					List<Kecamatan> kecamatanList;
					Kabupaten_Kotamadya kabupatenKotByProxy;
					Kabupaten_Kotamadya selKabupatenKot = kabupatenCombobox.getSelectedItem().getValue();
					kabupatenKotByProxy = getKabupaten_KotamadyaDao().findKecamatanByProxy(selKabupatenKot.getId());
					kecamatanList = kabupatenKotByProxy.getKecamatans();
					// check whether the name exists in the list
					for (Kecamatan kecamatanExisting : kecamatanList) {
						if (kecamatanExisting.getNamaKecamatan().compareTo(namaKecamatan)==0) {
							throw new Exception("Kecamatan "+namaKecamatan+" sudah terdaftar sebelumnya.");
						}
					}
					
					// create kecamatan object
					Kecamatan kecamatan = new Kecamatan();
					kecamatan.setCreatedAt(asDate(getCurrentLocalDateTime()));
					kecamatan.setEditedAt(asDate(getCurrentLocalDateTime()));
					kecamatan.setNamaKecamatan(namaKecamatan);
					
					// add kecamatan to Kabupaten
					kecamatanList.add(kecamatan);
					
					// update Kabupaten
					getKabupaten_KotamadyaDao().update(kabupatenKotByProxy);
					
					// notif
					Clients.showNotification("Kecamatan berhasil ditambahkan dibawah Kabupaten/Kotamadya");
					
					// display the kecamatan
					resetOtherComboboxes(2);
					
					loadKecamatanCombobox(selectedKabupatenKot);
					
/*					kabupatenKotByProxy = getKabupaten_KotamadyaDao().findKecamatanByProxy(selKabupatenKot.getId());
					kecamatanList = kabupatenKotByProxy.getKecamatans();
					Comboitem comboitem;
					for (Kecamatan kecamatanObj : kecamatanList) {
						comboitem = new Comboitem();
						comboitem.setLabel(kecamatanObj.getNamaKecamatan());
						comboitem.setValue(kecamatanObj);
						comboitem.setParent(kecamatanCombobox);
					}
*/
					for (Comboitem comboitemKec : kecamatanCombobox.getItems()) {
						if (comboitemKec.getLabel().compareTo(namaKecamatan)==0) {
							kecamatanCombobox.setSelectedItem(comboitemKec);
							
							selectedKecamatan = comboitemKec.getValue();
							
							break;
						}
					}
					loadKelurahanCombobox(selectedKecamatan);
				}
			});
			textEntryWindow.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					resetOtherComboboxes(2);

					loadKecamatanCombobox(selectedKabupatenKot);						
				}
			});
			textEntryWindow.doModal();
		} else {
			resetOtherComboboxes(1);
			
			loadKelurahanCombobox(kecamatanCombobox.getSelectedItem().getValue());
		}
	}

	private void loadKelurahanCombobox(Kecamatan selKecamatan) throws Exception {
		Kecamatan selKecamatanByProxy = 
				getKecamatanDao().findKelurahanByProxy(selKecamatan.getId());
		List<Kelurahan> kelurahanList = 
				selKecamatanByProxy.getKelurahans();
		if (selKecamatan.getNamaKecamatan().compareTo(NO_INFO)==0) {
			if (kelurahanList.isEmpty()) {
				// create comboitem to add to the kecamatan list
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel("Tambah Kecamatan...");
				comboitem.setValue(null);
				comboitem.setParent(kelurahanCombobox);
			} else {
				noInfoForKelurahan();
			}
		} else {
			Comboitem comboitem;
			for (Kelurahan kelurahan : kelurahanList) {
				comboitem = new Comboitem();
				comboitem.setLabel(kelurahan.getNamaKelurahan());
				comboitem.setValue(kelurahan);
				comboitem.setParent(kelurahanCombobox);
			}
			// create comboitem to add to the propinsi
			// -- in case the kabupaten not in the list
			comboitem = new Comboitem();
			comboitem.setLabel("Tambah Kelurahan...");
			comboitem.setValue(null);
			comboitem.setParent(kelurahanCombobox);
		}		
	}
	
	public void onSelect$kelurahanCombobox(Event event) throws Exception {
		if (kelurahanCombobox.getSelectedItem().getValue()==null) {
			// display textentry dialog
			Map<String, String> args = Collections.singletonMap("name", "Kelurahan:");
			Window textEntryWindow = (Window) Executions.createComponents("/kejadian/WilayahTextEntry.zul", kejadianMenonjolDialogWin, args);
			textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					String namaKelurahan = (String) event.getData();
					
					List<Kelurahan> kelurahanList;
					Kecamatan kecamatanByProxy;
					Kecamatan selKecamatan = kecamatanCombobox.getSelectedItem().getValue();
					kecamatanByProxy = getKecamatanDao().findKelurahanByProxy(selKecamatan.getId());
					kelurahanList = kecamatanByProxy.getKelurahans();
					// check whether the name exists in the list
					for (Kelurahan kelurahan : kelurahanList) {
						if (kelurahan.getNamaKelurahan().compareTo(namaKelurahan)==0) {
							throw new Exception("Kelurahan "+namaKelurahan+" sudah terdaftar sebelumnya.");
						}
					}
					// create kelurahan object
					Kelurahan kelurahan = new Kelurahan();
					kelurahan.setCreatedAt(asDate(getCurrentLocalDateTime()));
					kelurahan.setEditedAt(asDate(getCurrentLocalDateTime()));
					kelurahan.setNamaKelurahan(namaKelurahan);
					// add kelurahan to kecamatan
					kelurahanList.add(kelurahan);
					// update
					getKecamatanDao().update(kecamatanByProxy);
					// notif
					Clients.showNotification("Kelurahan berhasil ditambahkan dibawah Kecamatan");
					// display the kelurahan
					kelurahanCombobox.getItems().clear();
					// re-list
					kecamatanByProxy = getKecamatanDao().findKelurahanByProxy(selKecamatan.getId());
					kelurahanList = kecamatanByProxy.getKelurahans();
					Comboitem comboitem;
					for (Kelurahan kelurahanObj : kelurahanList) {
						comboitem = new Comboitem();
						comboitem.setLabel(kelurahanObj.getNamaKelurahan());
						comboitem.setValue(kelurahanObj);
						comboitem.setParent(kelurahanCombobox);
					}
					// select
					for (Comboitem comboitemKel : kelurahanCombobox.getItems()) {
						if (comboitemKel.getLabel().compareTo(namaKelurahan)==0) {
							kelurahanCombobox.setSelectedItem(comboitemKel);
							break;
						}
					}
				}
			});
			textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					kelurahanCombobox.getItems().clear();
					
					loadKelurahanCombobox(kecamatanCombobox.getSelectedItem().getValue());
				}
				
			});
			textEntryWindow.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					resetOtherComboboxes(1);
					
					loadKelurahanCombobox(kecamatanCombobox.getSelectedItem().getValue());
				}
			});
			textEntryWindow.doModal();
		}
	}
	
	public void onSelect$jenisKejadianCombobox(Event event) throws Exception {
		// respond to add new Jenis Kejadian ONLY
		if (jenisKejadianCombobox.getSelectedItem().getValue()==null) {
			Map<String, String> args = Collections.singletonMap("name", "Jenis Kejadian:");
			Window textEntryWindow = (Window) Executions.createComponents("/kejadian/WilayahTextEntry.zul", kejadianMenonjolDialogWin, args);
			textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// get the data from event
					String namaJenisKejadian = (String) event.getData();
					
					// check whether the name exists in the list
					List<KejadianJenis> jenisKejadianList = getKejadianJenisDao().findAllKejadianJenis(true);
					for (KejadianJenis kejadianJenis : jenisKejadianList) {
						if (kejadianJenis.getNamaJenis().compareTo(namaJenisKejadian)==0) {
							throw new Exception("Jenis Kejadian : "+namaJenisKejadian+" sudah terdaftar sebelumnya.");							
						}
					}
					
					// create jenisKejadian object
					KejadianJenis kejadianJenis = new KejadianJenis();
					kejadianJenis.setCreatedAt(asDate(getCurrentLocalDateTime()));
					kejadianJenis.setEditedAt(asDate(getCurrentLocalDateTime()));
					kejadianJenis.setNamaJenis(namaJenisKejadian);
					
					// save
					getKejadianJenisDao().save(kejadianJenis);
					
					// notif
					Clients.showNotification("Jenis Kejadian berhasil ditambahkan");
					
					// celar the combobox
					jenisKejadianCombobox.getItems().clear();
					
					// loadJenisKejadianCombobox()
					loadJenisKejadianCombobox();
					
					// select
					for (Comboitem comboitem : jenisKejadianCombobox.getItems()) {
						if (comboitem.getLabel().compareTo(namaJenisKejadian)==0) {
							jenisKejadianCombobox.setSelectedItem(comboitem);
						}
					}
				}
				
			});
			textEntryWindow.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					jenisKejadianCombobox.getItems().clear();
					jenisKejadianCombobox.setValue("");
					
					loadJenisKejadianCombobox();
				}
				
			});
			textEntryWindow.doModal();
		}
	}
	
	public void onSelect$motifKejadianCombobox(Event event) throws Exception {
		// respond to new motif kejadian ONLY
		if (motifKejadianCombobox.getSelectedItem().getValue()==null) {
			Map<String, String> args = Collections.singletonMap("name", "Motif Kejadian:");
			Window textEntryWindow = (Window) Executions.createComponents("/kejadian/WilayahTextEntry.zul", kejadianMenonjolDialogWin, args);
			textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// get the data from event
					String namaMotifKejadian = (String) event.getData();
					
					// check whether the name exists in the list
					List<KejadianMotif> kejadianMotifList = getKejadianMotifDao().findAllKejadianMotif();
					for (KejadianMotif kejadianMotif : kejadianMotifList) {
						if (kejadianMotif.getNamaMotif().compareTo(namaMotifKejadian)==0) {
							throw new Exception("Motif Kejadian : "+namaMotifKejadian+" sudah terdaftar sebelumnya.");
						}
					}
					
					// create motifKejadian object
					KejadianMotif kejadianMotif = new KejadianMotif();
					kejadianMotif.setCreatedAt(asDate(getCurrentLocalDateTime()));
					kejadianMotif.setEditedAt(asDate(getCurrentLocalDateTime()));
					kejadianMotif.setNamaMotif(namaMotifKejadian);
					
					// save
					getKejadianMotifDao().save(kejadianMotif);
					
					// notif
					Clients.showNotification("Motif Kejadian berhasil ditambahkan");

					// celar the combobox
					motifKejadianCombobox.getItems().clear();
					
					// loadMotifKejadianCombobox();
					loadMotifKejadianCombobox();
					
					// select
					for (Comboitem comboitem : motifKejadianCombobox.getItems()) {
						if (comboitem.getLabel().compareTo(namaMotifKejadian)==0) {
							motifKejadianCombobox.setSelectedItem(comboitem);
						}
					}
					
				}
			});
			textEntryWindow.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// clear the combobox
					motifKejadianCombobox.getItems().clear();
					motifKejadianCombobox.setValue("");
					
					// loadMotifKejadianCombobox();
					loadMotifKejadianCombobox();
				}
			});
			textEntryWindow.doModal();
		}
	}
	
	private void displayKejadianData() throws Exception {
		if (getKejadian().getId().compareTo(Long.MIN_VALUE)==0) {
			// new
			
			// get the document code from kotamaops
			String documentCode = getSettingsKotamaops().getDocumentCode();			
			
			// create a new ID : YYYYMMDDHHMMddd
			setDocumentSerialNumber(createSerialNumber(getCurrentLocalDateTime(), documentCode));
			kejadianIdTextbox.setValue(getDocumentSerialNumber().getSerialComp());
			twBuatTahunTextbox.setValue(getLocalDateTimeString(getCurrentLocalDateTime(), "YYYY"));
			// format to 0518.1313
			twBuatTanggalJamTextbox.setValue(
					getLocalDateTimeString(getCurrentLocalDateTime(), "MM")+
					getLocalDateTimeString(getCurrentLocalDateTime(), "dd")+"."+
					getLocalDateTimeString(getCurrentLocalDateTime(), "HH")+
					getLocalDateTimeString(getCurrentLocalDateTime(), "mm"));
			for (Comboitem comboitem : twBuatTimeZoneCombobox.getItems()) {
				if (comboitem.getValue().equals(getSettingsKotamaops().getTimeZone())) {
					twBuatTimeZoneCombobox.setSelectedItem(comboitem);
					
					break;
				}
			}
			// year
			twJadiTahunTextbox.setValue(getLocalDateTimeString(getCurrentLocalDateTime(), "yyyy"));
			// timezone
			twJadiTimeZoneTextbox.setValue(getSettingsKotamaops().getTimeZone().toString());
		} else {
			// edit
			idLabel.setValue("id#"+getKejadian().getId().toString());
			// display existing ID
			kejadianIdTextbox.setValue(getKejadian().getSerialNumber().getSerialComp());
			// pembuatan year
			twBuatTahunTextbox.setValue(getLocalDateTimeString(asLocalDateTime(getKejadian().getTwPembuatanDateTime()),"YYYY"));
			// format to 0518.1313
			// System.out.println(asLocalDateTime(getKejadian().getTwPembuatanDateTime()).toString());
			// System.out.println(getLocalDateTimeString(asLocalDateTime(getKejadian().getTwPembuatanDateTime()),"MM"));
			// System.out.println(getLocalDateTimeString(asLocalDateTime(getKejadian().getTwPembuatanDateTime()),"dd"));
			// System.out.println(getLocalDateTimeString(asLocalDateTime(getKejadian().getTwPembuatanDateTime()),"HH"));
			// System.out.println(getLocalDateTimeString(asLocalDateTime(getKejadian().getTwPembuatanDateTime()),"mm"));
			twBuatTanggalJamTextbox.setValue(
					getLocalDateTimeString(asLocalDateTime(getKejadian().getTwPembuatanDateTime()),"MM")+
					getLocalDateTimeString(asLocalDateTime(getKejadian().getTwPembuatanDateTime()),"dd")+"."+
					getLocalDateTimeString(asLocalDateTime(getKejadian().getTwPembuatanDateTime()), "HH")+
					getLocalDateTimeString(asLocalDateTime(getKejadian().getTwPembuatanDateTime()), "mm"));
			// time zone
			for (Comboitem comboitem : twBuatTimeZoneCombobox.getItems()) {
				if (comboitem.getValue().equals(getKejadian().getTwPembuatanTimezone())) {
					twBuatTimeZoneCombobox.setSelectedItem(comboitem);
					
					break;
				}
			}
			// kejadian year
			twJadiTahunTextbox.setValue(
					getLocalDateTimeString(asLocalDateTime(getKejadian().getTwKejadianDateTime()), "yyyy"));
			// format to 0518.1313
			twJadiTanggalJamTextbox.setValue(
					getLocalDateTimeString(asLocalDateTime(getKejadian().getTwKejadianDateTime()),"MM")+
					getLocalDateTimeString(asLocalDateTime(getKejadian().getTwKejadianDateTime()),"dd")+"."+
					getLocalDateTimeString(asLocalDateTime(getKejadian().getTwKejadianDateTime()), "HH")+
					getLocalDateTimeString(asLocalDateTime(getKejadian().getTwKejadianDateTime()), "mm"));
			// time zone
			twJadiTimeZoneTextbox.setValue(getKejadian().getTwKejadianTimezone().toString());
			twJadiTimeZoneTextbox.setAttribute("twJadiTimeZoneInd", getKejadian().getTwKejadianTimezone());
			
			// kotamaopsCombobox
			Kejadian kejadianByProxy = getKejadianDao().findKejadianKotamaopsByProxy(getKejadian().getId());			
			for (Comboitem comboitem : kotamaopsCombobox.getItems()) {
				if (comboitem.getLabel().compareTo(kejadianByProxy.getKotamaops().getKotamaopsName())==0) {
					kotamaopsCombobox.setSelectedItem(comboitem);
					break;
				}
			}
			if (kotamaopsCombobox.getSelectedItem()==null) {			
				return;
			}
			
			// propCombobox
			loadPropinsiCombobox(kotamaopsCombobox.getSelectedItem().getValue());
			Kejadian kejadianPropinsiByProxy = getKejadianDao().findKejadianPropinsiByProxy(getKejadian().getId());
			for (Comboitem comboitem : propCombobox.getItems()) {
				Propinsi propinsi = comboitem.getValue();
				String namaPropinsi = kejadianPropinsiByProxy.getPropinsi()==null?
						"" : kejadianPropinsiByProxy.getPropinsi().getNamaPropinsi();
				if (propinsi.getNamaPropinsi().compareTo(namaPropinsi)==0) {
					propCombobox.setSelectedItem(comboitem);
					break;
				}
			}
			
			// load the kabupaten based on the selected propinsi
			loadKabupatenCombobox(propCombobox.getSelectedItem().getValue());
			Kejadian kejadianKabupatenByProxy = getKejadianDao().findKejadianKabupatenByProxy(getKejadian().getId());
			for (Comboitem comboitem : kabupatenCombobox.getItems()) {
				Kabupaten_Kotamadya kabupatenKotamadya = comboitem.getValue();
				String namaKabupatenKot = kejadianKabupatenByProxy.getKabupatenKotamadya()==null?
						"" : kejadianKabupatenByProxy.getKabupatenKotamadya().getNamaKabupaten();
				if (kabupatenKotamadya.getNamaKabupaten().compareTo(namaKabupatenKot)==0) {
					kabupatenCombobox.setSelectedItem(comboitem);
					break;
				}
			}
			
			// load the kecamatan based on the selected kabupaten
			loadKecamatanCombobox(kabupatenCombobox.getSelectedItem().getValue());
			Kejadian kejadianKecamatanByProxy = getKejadianDao().findKejadianKecamatanByProxy(getKejadian().getId());
			for (Comboitem comboitem : kecamatanCombobox.getItems()) {
				Kecamatan kecamatan = comboitem.getValue();
				String namaKecamatan = kejadianKecamatanByProxy.getKecamatan()==null?
						"" : kejadianKecamatanByProxy.getKecamatan().getNamaKecamatan();
				if (kecamatan.getNamaKecamatan().compareTo(namaKecamatan)==0) {
					kecamatanCombobox.setSelectedItem(comboitem);
					break;
				}
			}
			
			// load the kelurahan based on the selected kecamatan
			loadKelurahanCombobox(kecamatanCombobox.getSelectedItem().getValue());
			Kejadian kejadianKelurahanByProxy = getKejadianDao().findKejadianKelurahanByProxy(getKejadian().getId());
			for (Comboitem comboitem : kelurahanCombobox.getItems()) {
				Kelurahan kelurahan = comboitem.getValue();
				String namaKelurahan = kejadianKelurahanByProxy.getKelurahan()==null?
						"" : kejadianKelurahanByProxy.getKelurahan().getNamaKelurahan();
				if (kelurahan.getNamaKelurahan().compareTo(namaKelurahan)==0) {
					kelurahanCombobox.setSelectedItem(comboitem);
					break;
				}
			}
			
			koordGpsTextbox.setValue(getKejadian().getKoordinatGps());
			koordPetaTextbox.setValue(getKejadian().getKoordinatPeta());
			bujurLintangTextbox.setValue(getKejadian().getBujurLintang());
			kampungTextbox.setValue(getKejadian().getKampung());
			jalanTextbox.setValue(getKejadian().getJalan());
			
			// kronologis
			kronologisTextbox.setValue(getKejadian().getKronologis());

			for (Comboitem comboitem : jenisKejadianCombobox.getItems()) {
				String namaJenisKejadian = getKejadian().getJenisKejadian()==null?
						"" : getKejadian().getJenisKejadian().getNamaJenis();
				if (comboitem.getLabel().compareTo(namaJenisKejadian)==0) {
					jenisKejadianCombobox.setSelectedItem(comboitem);
					break;
				}
			}
			
			for (Comboitem comboitem : motifKejadianCombobox.getItems()) {
				String namaMotifKejadian = getKejadian().getMotifKejadian()==null?
						"" : getKejadian().getMotifKejadian().getNamaMotif();
				if (comboitem.getLabel().compareTo(namaMotifKejadian)==0) {
					motifKejadianCombobox.setSelectedItem(comboitem);
					break;
				}
			}
			
			for (Comboitem comboitem : pelakuKejadianCombobox.getItems()) {
				String namaPelakuKejadian = getKejadian().getPelakuKejadian()==null?
						"" : getKejadian().getPelakuKejadian().getNamaPelaku();
				if (comboitem.getLabel().compareTo(namaPelakuKejadian)==0) {
					pelakuKejadianCombobox.setSelectedItem(comboitem);
					break;
				}
			}
			// keterangan Pelaku
			keteranganPelakuTextbox.setValue(getKejadian().getKeteranganPelaku());
			// sasaran
			sasaranTextbox.setValue(getKejadian().getSasaran());
		}		
	}

	private DocumentSerialNumber createSerialNumber(LocalDateTime currentDateTime, String documentCode) throws Exception {
		
		// get the serial number
		int serNum = getSerialNumberGenerator().getSerialNumber(documentCode, getZoneId(), currentDateTime);

		// create a new object
		DocumentSerialNumber serialNumber = new DocumentSerialNumber();
		serialNumber.setDocumentCode(getSettingsKotamaops().getDocumentCode());
		serialNumber.setCreatedAt(asDate(getCurrentLocalDateTime()));
		serialNumber.setEditedAt(asDate(getCurrentLocalDateTime()));
		serialNumber.setSerialDate(asDate(currentDateTime));
		serialNumber.setSerialNo(serNum);
		serialNumber.setSerialComp(formatSerialComp(documentCode, currentDateTime, serNum));
		
		return serialNumber;
	}

	public void onClick$saveButton(Event event) throws Exception {
		// check empty fields
		checkEmptyFields();
		
		// obtain the boject
		Kejadian userModKejadian = getKejadian();
		
		if (userModKejadian.getId().compareTo(Long.MIN_VALUE)==0) {
			// new serial number
			userModKejadian.setSerialNumber(getDocumentSerialNumber());
			// timestamp
			userModKejadian.setCreatedAt(asDate(getCurrentLocalDateTime()));
			userModKejadian.setEditedAt(asDate(getCurrentLocalDateTime()));
			
		} else {
			// no changes to serial number

			// timestamp
			userModKejadian.setEditedAt(asDate(getCurrentLocalDateTime()));
		}
		
		// tgl pembuatan
		LocalDateTime twPembuatanLocalDateTime =
				getTwConversion().convertTwToLocalDateTime(
						twBuatTahunTextbox.getValue(),
						twBuatTanggalJamTextbox.getValue());
		userModKejadian.setTwPembuatanDateTime(asDate(twPembuatanLocalDateTime));
		userModKejadian.setTwPembuatanTimezone(twBuatTimeZoneCombobox.getSelectedItem().getValue());
		// tgl kejadian
		LocalDateTime twKejadianLocalDateTime =
				getTwConversion().convertTwToLocalDateTime(
						twJadiTahunTextbox.getValue(),
						twJadiTanggalJamTextbox.getValue());
		userModKejadian.setTwKejadianDateTime(asDate(twKejadianLocalDateTime));
		userModKejadian.setTwKejadianTimezone(
				(TimezoneInd) twJadiTimeZoneTextbox.getAttribute("twJadiTimeZoneInd"));
		
		if (getSettingsKotamaops().getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0) {
			// save based on user selection of the kotamaops
			userModKejadian.setKotamaops(kotamaopsCombobox.getSelectedItem().getValue());			
		} else {
			// user cannot choose, therefore assigned to settings kotamaops
			userModKejadian.setKotamaops(getSettingsKotamaops());
		}
		
		// set in the ListInfo --> userModKejadian.setKotamaops(getKotamaops()); -- NOT ALWAYS
		userModKejadian.setPropinsi(propCombobox.getSelectedItem().getValue());
		userModKejadian.setKabupatenKotamadya(kabupatenCombobox.getSelectedItem().getValue());
		userModKejadian.setKecamatan(kecamatanCombobox.getSelectedItem().getValue());
		userModKejadian.setKelurahan(kelurahanCombobox.getSelectedItem().getValue());
		
		userModKejadian.setKoordinatGps(koordGpsTextbox.getValue());
		userModKejadian.setKoordinatPeta(koordPetaTextbox.getValue());
		userModKejadian.setBujurLintang(bujurLintangTextbox.getValue());
		userModKejadian.setKampung(kampungTextbox.getValue());
		userModKejadian.setJalan(jalanTextbox.getValue());
		
		userModKejadian.setKronologis(kronologisTextbox.getValue());

		userModKejadian.setJenisKejadian(jenisKejadianCombobox.getSelectedItem().getValue());
		userModKejadian.setMotifKejadian(motifKejadianCombobox.getSelectedItem().getValue());
		userModKejadian.setPelakuKejadian(pelakuKejadianCombobox.getSelectedItem().getValue());
		userModKejadian.setKeteranganPelaku(keteranganPelakuTextbox.getValue());
		userModKejadian.setSasaran(sasaranTextbox.getValue());
		
		Events.sendEvent(Events.ON_OK, kejadianMenonjolDialogWin, userModKejadian);
		
		kejadianMenonjolDialogWin.detach();
	}

	private void checkEmptyFields() throws Exception {
		if (twBuatTahunTextbox.getValue().isEmpty()) {
			throw new Exception("Tw Pembuatan Kejadian Tahun belum diisi.");
		}
		if (twBuatTanggalJamTextbox.getValue().isEmpty()) {
			throw new Exception("Tw Pembuatan Kejadian Tanggal dan Jam belum diisi.");
		}
		if (twJadiTanggalJamTextbox.getValue().isEmpty()) {
			throw new Exception("Tw Kejadian Tanggal dan Jam belum diisi.");
		}
		if (kotamaopsCombobox.getValue().isEmpty()) {
			throw new Exception("Kotamaops belum dipilih.");
		}
		if (propCombobox.getValue().isEmpty()) {
			throw new Exception("Propinsis belum dipilih.");
		}
		if (kabupatenCombobox.getValue().isEmpty()) {
			throw new Exception("Kabupaten / Kotamadya belum dipilih.");
		}
		if (kecamatanCombobox.getValue().isEmpty()) {
			throw new Exception("Kecamatan belum dipilih.");
		}
		if (kelurahanCombobox.getValue().isEmpty()) {
			throw new Exception("Kelurahan belum dipilih.");
		}
		if (jenisKejadianCombobox.getValue().isEmpty()) {
			throw new Exception("Jenis Kejadian belum dipilih.");
		}
		if (motifKejadianCombobox.getValue().isEmpty()) {
			throw new Exception("Motif Kejadian belum dipilih.");
		}
	}

	public void onClick$cancelButton(Event event) throws Exception {
		kejadianMenonjolDialogWin.detach();
	}

	public SerialNumberGenerator getSerialNumberGenerator() {
		return serialNumberGenerator;
	}

	public void setSerialNumberGenerator(SerialNumberGenerator serialNumberGenerator) {
		this.serialNumberGenerator = serialNumberGenerator;
	}

	public ZoneId getZoneId() {
		return zoneId;
	}

	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	public DocumentSerialNumber getDocumentSerialNumber() {
		return documentSerialNumber;
	}

	public void setDocumentSerialNumber(DocumentSerialNumber documentSerialNumber) {
		this.documentSerialNumber = documentSerialNumber;
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

	public LocalDateTime getCurrentLocalDateTime() {
		return currentLocalDateTime;
	}

	public void setCurrentLocalDateTime(LocalDateTime currentLocalDateTime) {
		this.currentLocalDateTime = currentLocalDateTime;
	}

	public KejadianDao getKejadianDao() {
		return kejadianDao;
	}

	public void setKejadianDao(KejadianDao kejadianDao) {
		this.kejadianDao = kejadianDao;
	}

	/**
	 * @return the kejadianJenisDao
	 */
	public KejadianJenisDao getKejadianJenisDao() {
		return kejadianJenisDao;
	}

	/**
	 * @param kejadianJenisDao the kejadianJenisDao to set
	 */
	public void setKejadianJenisDao(KejadianJenisDao kejadianJenisDao) {
		this.kejadianJenisDao = kejadianJenisDao;
	}

	/**
	 * @return the kejadianMotifDao
	 */
	public KejadianMotifDao getKejadianMotifDao() {
		return kejadianMotifDao;
	}

	/**
	 * @param kejadianMotifDao the kejadianMotifDao to set
	 */
	public void setKejadianMotifDao(KejadianMotifDao kejadianMotifDao) {
		this.kejadianMotifDao = kejadianMotifDao;
	}

	public KejadianData getKejadianData() {
		return kejadianData;
	}

	public void setKejadianData(KejadianData kejadianData) {
		this.kejadianData = kejadianData;
	}

	public Kotamaops getSettingsKotamaops() {
		return settingsKotamaops;
	}

	public void setSettingsKotamaops(Kotamaops settingsKotamaops) {
		this.settingsKotamaops = settingsKotamaops;
	}

	public Kejadian getKejadian() {
		return kejadian;
	}

	public void setKejadian(Kejadian kejadian) {
		this.kejadian = kejadian;
	}

	public TwConversion getTwConversion() {
		return twConversion;
	}

	public void setTwConversion(TwConversion twConversion) {
		this.twConversion = twConversion;
	}

	public Kotamaops getSelectedKotamaops() {
		return selectedKotamaops;
	}

	public void setSelectedKotamaops(Kotamaops selectedKotamaops) {
		this.selectedKotamaops = selectedKotamaops;
	}

	public KejadianPelakuDao getKejadianPelakuDao() {
		return kejadianPelakuDao;
	}

	public void setKejadianPelakuDao(KejadianPelakuDao kejadianPelakuDao) {
		this.kejadianPelakuDao = kejadianPelakuDao;
	}
}
