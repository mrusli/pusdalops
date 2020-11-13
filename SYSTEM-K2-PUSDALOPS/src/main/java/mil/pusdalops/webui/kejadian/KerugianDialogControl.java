package mil.pusdalops.webui.kejadian;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kerugian.Kerugian;
import mil.pusdalops.domain.kerugian.KerugianJenis;
import mil.pusdalops.domain.kerugian.KerugianKondisi;
import mil.pusdalops.domain.kerugian.KerugianSatuan;
import mil.pusdalops.domain.kerugian.Lembaga;
import mil.pusdalops.domain.kerugian.Pihak;
import mil.pusdalops.domain.kerugian.TipeKerugian;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.persistence.kerugian.jenis.dao.KerugianJenisDao;
import mil.pusdalops.persistence.kerugian.kondisi.dao.KerugianKondisiDao;
import mil.pusdalops.persistence.kerugian.satuan.dao.KerugianSatuanDao;
import mil.pusdalops.webui.common.GFCBaseController;

public class KerugianDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3878479274998404637L;
	
	private KerugianJenisDao kerugianJenisDao;
	private KerugianKondisiDao kerugianKondisiDao;
	private KerugianSatuanDao kerugianSatuanDao;
	
	private Window kerugianDialogWin;
	private Textbox kejadianIdTextbox, namaPersMatTextbox, keteranganTextbox;	
	private Combobox tipeKerugianCombobox, lembagaCombobox, kerugianJenisCombobox,
		kondisiCombobox, satuanCombobox;
	private Intbox jumlahIntbox;
	
	private KerugianData kerugianData;
	private Pihak paraPihak;
	private Kerugian kerugian;
	private Kejadian kejadian;
	private Kotamaops kotamaops; 
	private LocalDateTime currentDateTime;
	private TimezoneInd kotamaopsTimezoneInd;
	
	private static final Logger log = Logger.getLogger(KerugianDialogControl.class);
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		// kerugianData
		setKerugianData(
				(KerugianData) Executions.getCurrent().getArg().get("kerugianData"));
	}

	public void onCreate$kerugianDialogWin(Event event) throws Exception {
		setKejadian(
				getKerugianData().getKejadian());
		setKerugian(
				getKerugianData().getKerugian());
		setKotamaops(
				getKerugianData().getKotamaops());
		
		String purposeTitle = 
				getKerugian().getId().compareTo(Long.MIN_VALUE)==0 ? 
					// new
					"Menambah " : 
					// edit
					"Merubah ";

		setKotamaopsTimezoneInd(
				getKotamaops().getTimeZone());
		int ordinalVal = kotamaopsTimezoneInd.getValue();
		setCurrentDateTime(
				getLocalDateTime(getKotamaops().getTimeZone().toZoneId(ordinalVal)));
		
		setParaPihak(
				getKerugianData().getParaPihak());
		kerugianDialogWin.setTitle(purposeTitle+"Kerugian Pihak: "+
				getParaPihak());

		loadTipeKerugian();
		
		loadLembagaCombobox();
		
		loadKerugianJenisCombobox();
		
		loadKondisiCombobox();
		
		loadSatuanCombobox();
		
		displayData();
		
		log.info("Kerugian ID: "+getKerugian().getId());
	}

	private void loadTipeKerugian() {
		Comboitem comboitem;
		int i=0;
		for (TipeKerugian tipeKerugian : TipeKerugian.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(tipeKerugian.toString(i));
			comboitem.setValue(tipeKerugian);
			comboitem.setParent(tipeKerugianCombobox);
			
			i++;
		}
	}

	private void loadLembagaCombobox() {
		Comboitem comboitem;
		int i=0;
		for (Lembaga lembaga : Lembaga.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(lembaga.toString(i));
			comboitem.setValue(lembaga);
			comboitem.setParent(lembagaCombobox);
			
			i++;
		}
	}

	private void loadKerugianJenisCombobox() throws Exception {
		List<KerugianJenis> kerugianJenisList = getKerugianJenisDao().findAllKerugianJenis(true);
		Comboitem comboitem;
		for (KerugianJenis kerugianJenis : kerugianJenisList) {
			comboitem = new Comboitem();
			comboitem.setLabel(kerugianJenis.getNamaJenis());
			comboitem.setValue(kerugianJenis);
			comboitem.setParent(kerugianJenisCombobox);
		}
		// create comboitem to add to the jenis kerugian list
		// -- in case jenis kerugian not in the list		
		comboitem = new Comboitem();
		comboitem.setLabel("Tambah Jenis Kerugian");
		comboitem.setValue(null);
		comboitem.setParent(kerugianJenisCombobox);
	}	
	
	public void onSelect$kerugianJenisCombobox(Event event) throws Exception {
		// respond to new jenis kerugian ONLY
		if (kerugianJenisCombobox.getSelectedItem().getValue()==null) {
			Window textEntryWindow = (Window) Executions.createComponents("/kejadian/KerugianJenisTextEntry.zul", kerugianDialogWin, null);
			textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// get the data from event
					KerugianJenisData kerugianJenisData = (KerugianJenisData) event.getData();
					
					// check whether the name exists in the list
					List<KerugianJenis> kerugianJenisList = getKerugianJenisDao().findAllKerugianJenis(true);
					for (KerugianJenis kerugianJenis : kerugianJenisList) {
						if (kerugianJenisData.getNamaKerugianJenis().compareTo(kerugianJenis.getNamaJenis())==0) {
							throw new Exception("Jenis Keugian : "+kerugianJenisData.getNamaKerugianJenis()+" sudah terdaftar sebelumnya.");							
						}
					}
					
					// create jenisKerugian object
					KerugianJenis kerugianJenis = new KerugianJenis();
					kerugianJenis.setCreatedAt(asDate(getCurrentDateTime()));
					kerugianJenis.setEditedAt(asDate(getCurrentDateTime()));
					kerugianJenis.setNamaJenis(kerugianJenisData.getNamaKerugianJenis());
					kerugianJenis.setTipeKerugian(kerugianJenisData.getTipeKerugian());
					
					// save
					getKerugianJenisDao().save(kerugianJenis);
					
					// notif
					Clients.showNotification("Jenis Kerugian berhasil ditambahkan");

					// clear the combobox
					kerugianJenisCombobox.getItems().clear();
					
					loadKerugianJenisCombobox();

					// select
					for (Comboitem  comboitem : kerugianJenisCombobox.getItems()) {
						if (comboitem.getLabel().compareTo(kerugianJenisData.getNamaKerugianJenis())==0) {
							kerugianJenisCombobox.setSelectedItem(comboitem);
						}
					}
				}
			});
			textEntryWindow.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// clear the combobox, including the value
					kerugianJenisCombobox.getItems().clear();
					kerugianJenisCombobox.setValue("");
					
					loadKerugianJenisCombobox();
				}
			});
			textEntryWindow.doModal();
		}
	}
	
	private void loadKondisiCombobox() throws Exception {
		List<KerugianKondisi> kerugianKondisiList = getKerugianKondisiDao().findAllKerugianKondisi(true);
		Comboitem comboitem;
		for (KerugianKondisi kerugianKondisi : kerugianKondisiList) {
			comboitem = new Comboitem();
			comboitem.setLabel(kerugianKondisi.getNamaKondisi());
			comboitem.setValue(kerugianKondisi);
			comboitem.setParent(kondisiCombobox);
		}
		// create comboitem to add to the kondisi kerugian list
		// -- in case kondisi kerugian not in the list		
		comboitem = new Comboitem();
		comboitem.setLabel("Tambah Kondisi Kerugian");
		comboitem.setValue(null);
		comboitem.setParent(kondisiCombobox);
	}	

	public void onSelect$kondisiCombobox(Event event) throws Exception {
		// respond to new jenis kerugian ONLY
		if (kondisiCombobox.getSelectedItem().getValue()==null) {
			Map<String, String> arg = Collections.singletonMap("name", "Kondisi Kerugian:");
			Window textEntryWindow = (Window) Executions.createComponents("/kejadian/WilayahTextEntry.zul", kerugianDialogWin, arg);
			textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					String namaKondisi = (String) event.getData();
					
					// check whether the name exist in the list
					List<KerugianKondisi> kerugianKondisiList = getKerugianKondisiDao().findAllKerugianKondisi(true);
					for (KerugianKondisi kerugianKondisi : kerugianKondisiList) {
						if (namaKondisi.compareTo(kerugianKondisi.getNamaKondisi())==0) {
							throw new Exception("Kondisi Kerugian "+namaKondisi+" sudah terdaftar sebelumnya.");
						}
					}
					
					// create KerugianKondisi
					KerugianKondisi kerugianKondisi = new KerugianKondisi();
					kerugianKondisi.setCreatedAt(asDate(getCurrentDateTime()));
					kerugianKondisi.setEditedAt(asDate(getCurrentDateTime()));
					kerugianKondisi.setNamaKondisi(namaKondisi);
					
					// save
					getKerugianKondisiDao().save(kerugianKondisi);
					
					// notif
					Clients.showNotification("Kondisi Kerugian berhasil ditambahkan");

					// clear
					kondisiCombobox.getItems().clear();
					
					// load
					loadKondisiCombobox();
					
					// select
					for (Comboitem comboitem : kondisiCombobox.getItems()) {
						if (comboitem.getLabel().compareTo(namaKondisi)==0) {
							kondisiCombobox.setSelectedItem(comboitem);
						}
					}
					
				}
			});
			textEntryWindow.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// clear the combobox, including the value
					kondisiCombobox.getItems().clear();
					kondisiCombobox.setValue("");
					
					loadKondisiCombobox();
				}
			});
			textEntryWindow.doModal();
			
		}
	}
	
	private void loadSatuanCombobox() throws Exception {
		List<KerugianSatuan> kerugianSatuanList = getKerugianSatuanDao().findAllKerugianSatuan(true);
		Comboitem comboitem;
		for (KerugianSatuan kerugianSatuan : kerugianSatuanList) {
			comboitem = new Comboitem();
			comboitem.setLabel(kerugianSatuan.getSatuan());
			comboitem.setValue(kerugianSatuan);
			comboitem.setParent(satuanCombobox);
		}
		// create comboitem to add to the satuan kerugian list
		// -- in case satuan kerugian not in the list		
		comboitem = new Comboitem();
		comboitem.setLabel("Tambah Satuan Kerugian");
		comboitem.setValue(null);
		comboitem.setParent(satuanCombobox);
	}

	public void onSelect$satuanCombobox(Event event) throws Exception {
		// respond to new satuan kerugian ONLY
		if (satuanCombobox.getSelectedItem().getValue()==null) {
			Map<String, String> arg = Collections.singletonMap("name", "Satuan Kerugian:");
			Window textEntryWindow = (Window) Executions.createComponents("/kejadian/WilayahTextEntry.zul", kerugianDialogWin, arg);
			textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					String namaSatuan = (String) event.getData();

					// check namaSatuan already in the list
					List<KerugianSatuan> kerugianSatuanList = getKerugianSatuanDao().findAllKerugianSatuan(true);
					for (KerugianSatuan kerugianSatuan : kerugianSatuanList) {
						if (namaSatuan.compareTo(kerugianSatuan.getSatuan())==0) {
							throw new Exception("Satuan Kerugian "+namaSatuan+" sudah terdaftar sebelumnya.");
						}
					}
					
					// create Kerugian Satuan
					KerugianSatuan kerugianSatuan = new KerugianSatuan();
					kerugianSatuan.setCreatedAt(asDate(getCurrentDateTime()));
					kerugianSatuan.setEditedAt(asDate(getCurrentDateTime()));
					kerugianSatuan.setSatuan(namaSatuan);
					
					// save
					getKerugianSatuanDao().save(kerugianSatuan);
					
					// notify
					Clients.showNotification("Kondisi Kerugian berhasil ditambahkan");

					// clear
					satuanCombobox.getItems().clear();
					
					// load
					loadSatuanCombobox();
					
					// select
					for (Comboitem comboitem : satuanCombobox.getItems()) {
						if (namaSatuan.compareTo(comboitem.getLabel())==0) {
							satuanCombobox.setSelectedItem(comboitem);
						}
					}
				}
			});
			textEntryWindow.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// clear
					satuanCombobox.getItems().clear();
					satuanCombobox.setValue("");
					
					// load
					loadSatuanCombobox();					
				}
			});			
			textEntryWindow.doModal();
		}
	}
	
	private void displayData() {
		kejadianIdTextbox.setValue(getKejadian().getSerialNumber().getSerialComp());
		if (getKerugianData().getKerugian().getId().compareTo(Long.MIN_VALUE)==0) {
			// new - user fill in
			
			// default to 1st comboitem
			tipeKerugianCombobox.setSelectedIndex(0);
			lembagaCombobox.setSelectedIndex(0);
			kerugianJenisCombobox.setSelectedIndex(0);
			kondisiCombobox.setSelectedIndex(0);
			satuanCombobox.setSelectedIndex(0);
			// init to 0
			jumlahIntbox.setValue(0);
		} else {
			// edit - system fill in
			namaPersMatTextbox.setValue(getKerugian().getNamaMaterial());
			for (Comboitem comboitem : tipeKerugianCombobox.getItems()) {
				if (comboitem.getValue().equals(getKerugian().getTipeKerugian())) {
					tipeKerugianCombobox.setSelectedItem(comboitem);
					break;
				}
			}
			for (Comboitem comboitem : lembagaCombobox.getItems()) {
				if (comboitem.getValue().equals(getKerugian().getLembagaTerkait())) {
					lembagaCombobox.setSelectedItem(comboitem);
				}
			}
			for (Comboitem comboitem : kerugianJenisCombobox.getItems()) {
				String kerugianJenis = getKerugian().getKerugianJenis()==null?
						"" : getKerugian().getKerugianJenis().getNamaJenis();
				if (comboitem.getLabel().compareTo(kerugianJenis)==0) {
					kerugianJenisCombobox.setSelectedItem(comboitem);
				}
			}
			for (Comboitem comboitem : kondisiCombobox.getItems()) {
				String kerugianKondisi = getKerugian().getKerugianKondisi()==null?
						"" : getKerugian().getKerugianKondisi().getNamaKondisi();
				if (comboitem.getLabel().compareTo(kerugianKondisi)==0) {
					kondisiCombobox.setSelectedItem(comboitem);
				}
			}
			jumlahIntbox.setValue(getKerugian().getJumlah());
			for (Comboitem comboitem : satuanCombobox.getItems()) {
				String kerugianSatuan = getKerugian().getKerugianSatuan()==null?
						"" : getKerugian().getKerugianSatuan().getSatuan();
				if (comboitem.getLabel().compareTo(kerugianSatuan)==0) {
					satuanCombobox.setSelectedItem(comboitem);
				}
			}
			keteranganTextbox.setValue(getKerugian().getKeterangan());
		}
	}

	public void onClick$saveButton(Event event) throws Exception {
		Kerugian userModKerugian = getKerugian();
		
		if (getKerugianData().getKerugian().getId().compareTo(Long.MIN_VALUE)==0) {	
			userModKerugian.setCreatedAt(asDate(getCurrentDateTime()));
			userModKerugian.setEditedAt(asDate(getCurrentDateTime()));
		} else {
			userModKerugian.setEditedAt(asDate(getCurrentDateTime()));			
		}
		
		userModKerugian.setNamaMaterial(namaPersMatTextbox.getValue());
		userModKerugian.setParaPihak(getParaPihak());
		userModKerugian.setTipeKerugian(tipeKerugianCombobox.getSelectedItem().getValue());
		userModKerugian.setLembagaTerkait(lembagaCombobox.getSelectedItem().getValue());
		userModKerugian.setKerugianJenis(kerugianJenisCombobox.getSelectedItem().getValue());
		userModKerugian.setKerugianKondisi(kondisiCombobox.getSelectedItem().getValue());
		userModKerugian.setJumlah(jumlahIntbox.getValue());
		userModKerugian.setKerugianSatuan(satuanCombobox.getSelectedItem().getValue());
		userModKerugian.setKeterangan(keteranganTextbox.getValue());
		
		// send event
		Events.sendEvent(Events.ON_OK, kerugianDialogWin, userModKerugian);
		
		kerugianDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		
		kerugianDialogWin.detach();
	}

	public KerugianData getKerugianData() {
		return kerugianData;
	}

	public void setKerugianData(KerugianData kerugianData) {
		this.kerugianData = kerugianData;
	}

	public Pihak getParaPihak() {
		return paraPihak;
	}

	public void setParaPihak(Pihak paraPihak) {
		this.paraPihak = paraPihak;
	}

	/**
	 * @return the kerugian
	 */
	public Kerugian getKerugian() {
		return kerugian;
	}

	/**
	 * @param kerugian the kerugian to set
	 */
	public void setKerugian(Kerugian kerugian) {
		this.kerugian = kerugian;
	}

	/**
	 * @return the kejadian
	 */
	public Kejadian getKejadian() {
		return kejadian;
	}

	/**
	 * @param kejadian the kejadian to set
	 */
	public void setKejadian(Kejadian kejadian) {
		this.kejadian = kejadian;
	}

	public KerugianJenisDao getKerugianJenisDao() {
		return kerugianJenisDao;
	}

	public void setKerugianJenisDao(KerugianJenisDao kerugianJenisDao) {
		this.kerugianJenisDao = kerugianJenisDao;
	}

	public KerugianKondisiDao getKerugianKondisiDao() {
		return kerugianKondisiDao;
	}

	public void setKerugianKondisiDao(KerugianKondisiDao kerugianKondisiDao) {
		this.kerugianKondisiDao = kerugianKondisiDao;
	}

	public KerugianSatuanDao getKerugianSatuanDao() {
		return kerugianSatuanDao;
	}

	public void setKerugianSatuanDao(KerugianSatuanDao kerugianSatuanDao) {
		this.kerugianSatuanDao = kerugianSatuanDao;
	}

	public LocalDateTime getCurrentDateTime() {
		return currentDateTime;
	}

	public void setCurrentDateTime(LocalDateTime currentDateTime) {
		this.currentDateTime = currentDateTime;
	}

	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}

	public TimezoneInd getKotamaopsTimezoneInd() {
		return kotamaopsTimezoneInd;
	}

	public void setKotamaopsTimezoneInd(TimezoneInd kotamaopsTimezoneInd) {
		this.kotamaopsTimezoneInd = kotamaopsTimezoneInd;
	}

}
