package mil.pusdalops.webui.sinkronisasi;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kejadian.KejadianJenis;
import mil.pusdalops.domain.kejadian.KejadianMotif;
import mil.pusdalops.domain.kerugian.Kerugian;
import mil.pusdalops.domain.kerugian.KerugianJenis;
import mil.pusdalops.domain.kerugian.KerugianKondisi;
import mil.pusdalops.domain.kerugian.KerugianSatuan;
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
import mil.pusdalops.persistence.kelurahan.dao.KelurahanDao;
import mil.pusdalops.persistence.kerugian.jenis.dao.KerugianJenisDao;
import mil.pusdalops.persistence.kerugian.kondisi.dao.KerugianKondisiDao;
import mil.pusdalops.persistence.kerugian.satuan.dao.KerugianSatuanDao;
import mil.pusdalops.persistence.kotamaops.dao.KotamaopsDao;
import mil.pusdalops.persistence.propinsi.dao.PropinsiDao;
import mil.pusdalops.webui.common.GFCBaseController;

public class SinkronisasiToCloudDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2686191938895165635L;

	private KejadianDao kejadianDao;
	private KotamaopsDao kotamaopsDao;
	private PropinsiDao propinsiDao;
	private Kabupaten_KotamadyaDao kabupaten_KotamadyaDao;
	private KecamatanDao kecamatanDao;
	private KelurahanDao kelurahanDao;
	
	private Window sinkronisasiToCloudDialogWin;
	private Textbox kejadianIdTextbox, kotamaopsTextbox, propinsiTextbox, 
		kabupatenKotTextbox, kecamatanTextbox, kelurahanTextbox,
		jenisKejadianTextbox, motifKejadianTextbox;
	private Label kabupatenKotRef, kecamatanRef, kelurahanRef, synchronNoteLabel, 
		actionSuccessLabel, jenisKejadianRef, motifKejadianRef;
	private Listbox kerugianListbox;
	private Checkbox referensiWilayahCheckbox, referensiKejadianCheckbox,
		referensiKerugianCheckbox;
	private Button synchronizeToCloudButton;
	
	private SinkronisasiData sinkronisasiData;
	private Kejadian kejadian;
	private LocalDateTime currentLocalDateTime;
	private TimezoneInd timezoneInd;
	private ApplicationContext ctx;
	
	private Map<Boolean, Propinsi> propinsiReferenceMap = 					new HashMap<Boolean, Propinsi>();
	private Map<Boolean, Kabupaten_Kotamadya> kabupatenKotReferenceMap = 	new HashMap<Boolean, Kabupaten_Kotamadya>();
	private Map<Boolean, Kecamatan> kecamatanReferenceMap = 				new HashMap<Boolean, Kecamatan>();
	private Map<Boolean, Kelurahan> kelurahanReferenceMap = 				new HashMap<Boolean, Kelurahan>();
	private Map<Boolean, KejadianJenis> kejadianJenisReferenceMap = 		new HashMap<Boolean, KejadianJenis>();
	private Map<Boolean, KejadianMotif> kejadianMotifReferenceMap = 		new HashMap<Boolean, KejadianMotif>();
		
	private final Logger log = Logger.getLogger(SinkronisasiToCloudDialogControl.class);
	
	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.GenericForwardComposer#doAfterCompose(org.zkoss.zk.ui.Component)
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// sinkronisasiData
		setSinkronisasiData(
				(SinkronisasiData) arg.get("sinkronisasiData"));
	}

	public void onCreate$sinkronisasiToCloudDialogWin(Event event) throws Exception {
		setKejadian(
				getSinkronisasiData().getKejadian());
		
		// prepare cloud dao context
		ctx = new ClassPathXmlApplicationContext("CommonContext-Cloud-Dao.xml");
		
		// init referenceMap
		propinsiReferenceMap.clear();
		kabupatenKotReferenceMap.clear();
		kecamatanReferenceMap.clear();
		kelurahanReferenceMap.clear();
		kejadianJenisReferenceMap.clear();
		kejadianMotifReferenceMap.clear();
				
		// current datetime and timezoneInd
		setCurrentLocalDateTime(getSinkronisasiData().getCurrentLocalDateTime());
		setTimezoneInd(getSinkronisasiData().getTimezoneInd());
		
		// init reference label
		initReferenceLabel();
		
		// display
		displayKejadianInfo();
		
		// check ref wilayah
		displayWilayahReferenceCheck();
		
		// check ref kejadian
		displayKejadianReferenceCheck();
	}
	
	private void initReferenceLabel() {
		kabupatenKotRef.setValue(""); 
		kecamatanRef.setValue("");
		kelurahanRef.setValue("");
		jenisKejadianRef.setValue("");
		motifKejadianRef.setValue("");
		
		kerugianListbox.getItems().clear();
	}

	private void displayKejadianInfo() throws Exception {
		kejadianIdTextbox.setValue(
				getKejadian().getSerialNumber().getSerialComp());
		
		// kotamaops
		Kejadian kejadianKotamaopsByProxy = 
				getKejadianDao().findKejadianKotamaopsByProxy(getKejadian().getId());
		kotamaopsTextbox.setValue(kejadianKotamaopsByProxy.getKotamaops().getKotamaopsName());
		
		// propinsi
		Kejadian kejadianPropByProxy = 
				getKejadianDao().findKejadianPropinsiByProxy(getKejadian().getId());
		propinsiTextbox.setValue(kejadianPropByProxy.getPropinsi().getNamaPropinsi());
		
		// kabupaten/kotamadya
		Kejadian kejadianKabKotByProxy =
				getKejadianDao().findKejadianKabupatenByProxy(getKejadian().getId());
		kabupatenKotTextbox.setValue(kejadianKabKotByProxy.getKabupatenKotamadya().getNamaKabupaten());
		
		// kecamatan
		Kejadian kejadianKecByProxy =
				getKejadianDao().findKejadianKecamatanByProxy(getKejadian().getId());
		kecamatanTextbox.setValue(kejadianKecByProxy.getKecamatan().getNamaKecamatan());
		
		// kelurahan
		Kejadian kejadianKelByProxy =
				getKejadianDao().findKejadianKelurahanByProxy(getKejadian().getId());
		kelurahanTextbox.setValue(kejadianKelByProxy.getKelurahan().getNamaKelurahan());
		
		jenisKejadianTextbox.setValue(getKejadian().getJenisKejadian().getNamaJenis());
		
		motifKejadianTextbox.setValue(getKejadian().getMotifKejadian().getNamaMotif());
		
		// kerugian
		Kejadian kejadianKerugianByProxy = 
				getKejadianDao().findKejadianKerugiansByProxy(getKejadian().getId());
		List<Kerugian> kerugianList = kejadianKerugianByProxy.getKerugians();
		// list -- including reference check
		kerugianListbox.setModel(new ListModelList<Kerugian>(kerugianList));
		kerugianListbox.setItemRenderer(getKerugianListitemRenderer());
		kerugianListbox.setEmptyMessage("Tidak ada data Kerugian");
	}
	
	private ListitemRenderer<Kerugian> getKerugianListitemRenderer() {

		return new ListitemRenderer<Kerugian>() {
			
			@Override
			public void render(Listitem item, Kerugian kerugian, int index) throws Exception {
				Listcell lc;
				
				// Pihak
				lc = new Listcell("Pihak "+kerugian.getParaPihak().toString());
				lc.setStyle("background-color: #f98a00;");
				lc.setParent(item);
				
				// Jenis
				lc = new Listcell(kerugian.getKerugianJenis().getNamaJenis());
				lc.setValue(kerugian.getKerugianJenis());
				lc.setParent(item);				
				
				// -- jenisRef
				lc = referenceCheckKerugianJenis(new Listcell(), kerugian.getKerugianJenis(), index);
				lc.setParent(item);
				
				// Kondisi
				lc = new Listcell(kerugian.getKerugianKondisi().getNamaKondisi());
				lc.setValue(kerugian.getKerugianKondisi());
				lc.setParent(item);
				
				// -- kondisiRef
				lc = referenceCheckKerugianKondisi(new Listcell(), kerugian.getKerugianKondisi(), index);
				lc.setParent(item);
				
				// Satuan
				lc = new Listcell(kerugian.getKerugianSatuan().getSatuan());
				lc.setValue(kerugian.getKerugianSatuan());
				lc.setParent(item);
				
				// -- satuanRef
				lc = referenceCheckKerugianSatuan(new Listcell(), kerugian.getKerugianSatuan(), index);
				lc.setParent(item);
				
				item.setValue(kerugian);
				
			}

			private Listcell referenceCheckKerugianJenis(Listcell listcell, KerugianJenis kerugianJenis, int index) throws Exception {
				boolean kerugianJenisNameFound = false;
				KerugianJenisDao cloudKerugianJenisDao = (KerugianJenisDao) ctx.getBean("kerugianJenisDao");
				List<KerugianJenis> kerugianJenisList = cloudKerugianJenisDao.findAllKerugianJenis(true);
				for (KerugianJenis cloudKerugianJenis : kerugianJenisList) {
					if (kerugianJenis.getNamaJenis().compareTo(cloudKerugianJenis.getNamaJenis())==0) {
						kerugianJenisNameFound = true;
						log.info(cloudKerugianJenis+" dengan Nama Jenis Kerugian: "+cloudKerugianJenis.getNamaJenis()+" Ditemukan.");
						listcell.setLabel("[OK]");
						listcell.setStyle("color: black;");
						listcell.setValue(cloudKerugianJenis);
						break;
					}
				}
				if (!kerugianJenisNameFound) {
					log.info("Jenis Kerugian : "+kerugianJenis.getNamaJenis()+" TIDAK Ditemukan...");
					//
					listcell.setLabel("[TIDAK ditemukan]");
					listcell.setStyle("color: red;");
					listcell.setValue(null);
				}
				return listcell;
			}

			private Listcell referenceCheckKerugianKondisi(Listcell listcell, KerugianKondisi kerugianKondisi, int index) throws Exception {
				boolean kerugianKondisiNameFound = false;
				KerugianKondisiDao cloudKerugianKondisiDao = (KerugianKondisiDao) ctx.getBean("kerugianKondisiDao");
				List<KerugianKondisi> kerugianKondisiList = cloudKerugianKondisiDao.findAllKerugianKondisi(true);
				for (KerugianKondisi cloudKerugianKondisi : kerugianKondisiList) {
					if (cloudKerugianKondisi.getNamaKondisi().compareTo(kerugianKondisi.getNamaKondisi())==0) {
						kerugianKondisiNameFound = true;
						log.info(cloudKerugianKondisi+" dengan Nama Kondisi Kerugian: "+cloudKerugianKondisi.getNamaKondisi()+" Ditemukan.");
						listcell.setLabel("[OK]");
						listcell.setStyle("color: black;");
						listcell.setValue(cloudKerugianKondisi);
					}
				}
				if (!kerugianKondisiNameFound) {
					log.info("Kondisi Kerugian : "+kerugianKondisi.getNamaKondisi()+" TIDAK Ditemukan...");
					//
					listcell.setLabel("[TDIAK ditemukan]");
					listcell.setStyle("color: red;");
					listcell.setValue(null);
				}
				return listcell;
			}

			private Listcell referenceCheckKerugianSatuan(Listcell listcell, KerugianSatuan kerugianSatuan, int index) throws Exception {
				boolean kerugianSatuanNameFound = false;
				KerugianSatuanDao cloudKerugianSatuanDao = (KerugianSatuanDao) ctx.getBean("kerugianSatuanDao");
				List<KerugianSatuan> kerugianSatuanList = cloudKerugianSatuanDao.findAllKerugianSatuan(true);
				for (KerugianSatuan cloudKerugianSatuan : kerugianSatuanList) {
					if (cloudKerugianSatuan.getSatuan().compareTo(kerugianSatuan.getSatuan())==0) {
						kerugianSatuanNameFound = true;
						log.info(cloudKerugianSatuan+" dengan Nama Satuan Kerugian: "+cloudKerugianSatuan.getSatuan()+" Ditemukan.");
						listcell.setValue("[OK]");
						listcell.setStyle("color: black;");
						listcell.setValue(cloudKerugianSatuan);
					}
				}
				if (!kerugianSatuanNameFound) {
					log.info("Satuan Kerugian : "+kerugianSatuan.getSatuan()+" TIDAK Ditemukan...");
					//
					listcell.setLabel("[TIDAK ditemukan]");
					listcell.setStyle("color: red;");
					listcell.setValue(null);
				}
				return listcell;
			}
		};
	}
	
	public void onAfterRender$kerugianListbox(Event event) throws Exception {
		boolean rowEval = true;
		boolean listEval = true;
		Listcell lc;
		List<Listitem> listitem = kerugianListbox.getItems();
		for (Listitem item : listitem) {
			// jenis kerugian
			lc = (Listcell) item.getChildren().get(2);
			KerugianJenis kerugianJenis = lc.getValue();
			
			// kondisi kerugian
			lc = (Listcell) item.getChildren().get(4);
			KerugianKondisi kerugianKondisi = lc.getValue();
			
			// satuan kerugian
			lc = (Listcell) item.getChildren().get(6);
			KerugianSatuan kerugianSatuan = lc.getValue();
			
			rowEval = (kerugianJenis!=null) && (kerugianKondisi!=null) && (kerugianSatuan!=null);
			listEval = listEval && rowEval;
		}
		
		referensiKerugianCheckbox.setChecked(listEval);
		referensiKerugianCheckbox.setDisabled(listEval);
	}
	
	private void displayWilayahReferenceCheck() throws Exception {
		Boolean referencePassed = false;
		
		// cek referensi propinsi
		Kejadian kejadianPropByProxy = 
				getKejadianDao().findKejadianPropinsiByProxy(getKejadian().getId());
		Propinsi propinsi = kejadianPropByProxy.getPropinsi();
		referencePassed = propinsiReferenceCheck(propinsi);
		
		// cek refrensi kabupaten/kotamadya
		referencePassed = kabupatenKotamadyaReferenceCheck(propinsi);			
				
		// cek referensi kecamatan
		Kabupaten_Kotamadya cloudKabupatenKot = kabupatenKotReferenceMap.get(Boolean.TRUE);
		referencePassed = kecamatanReferenceCheck(cloudKabupatenKot);
		
		// cek referensi kelurahan
		Kecamatan cloudKecamatan = kecamatanReferenceMap.get(Boolean.TRUE);
		referencePassed = kelurahanReferenceCheck(cloudKecamatan);
		
		referensiWilayahCheckbox.setChecked(referencePassed);
		referensiWilayahCheckbox.setDisabled(referencePassed);

		synchronNoteLabel.setVisible(referencePassed);
	}
	
	private boolean propinsiReferenceCheck(Propinsi propinsi) throws Exception {
		boolean propinsiNameFound = false;

		PropinsiDao cloudPropinsiDao = (PropinsiDao) ctx.getBean("propinsiDao");
		List<Propinsi> cloudPropinsiList = cloudPropinsiDao.findAllPropinsi();
		for (Propinsi cloudPropinsi : cloudPropinsiList) {
			if (propinsi.getNamaPropinsi().compareTo(cloudPropinsi.getNamaPropinsi())==0) {
				log.info(propinsi+" dengan Nama Propinsi: "+propinsi.getNamaPropinsi()+" Ditemukan...");
				propinsiNameFound = true;
				propinsiReferenceMap.put(Boolean.TRUE, cloudPropinsi);
				break;
			}
		}
		if (!propinsiNameFound) {
			// this shouldn't happen -- because before kotamaops receive this apps, it must have all the propinsi
			log.info("Nama Propinsi : "+propinsi.getNamaPropinsi()+" TIDAK Ditemukan...");
			propinsiReferenceMap.put(Boolean.FALSE, null);
		}

		return propinsiNameFound;
	}

	private boolean kabupatenKotamadyaReferenceCheck(Propinsi propinsi)	throws Exception {
		boolean kabupatenKotNameFound = false;

		PropinsiDao cloudPropinsiDao = (PropinsiDao) ctx.getBean("propinsiDao");
		Propinsi cloudPropinsiKabupatenKotByProxy = 
				cloudPropinsiDao.findKabupatenKotamadyaByProxy(propinsi.getId());
		List<Kabupaten_Kotamadya> cloudKabupatenKotList = 
				cloudPropinsiKabupatenKotByProxy.getKabupatenkotamadyas();
		for (Kabupaten_Kotamadya cloudKabupatenKot : cloudKabupatenKotList) {
			if (kabupatenKotTextbox.getValue().compareTo(cloudKabupatenKot.getNamaKabupaten())==0) {
				log.info(cloudKabupatenKot+" dengan Nama Kabupaten/Kotamadya: "+cloudKabupatenKot.getNamaKabupaten()+
						" Ditemukan dibawah Propinsi: "+propinsi.getNamaPropinsi());
				kabupatenKotReferenceMap.put(Boolean.TRUE, cloudKabupatenKot);
				kabupatenKotNameFound = true;
				//
				kabupatenKotRef.setValue("[OK]");
				kabupatenKotRef.setStyle("color: black;");
				break;
			}
		}		
		if (!kabupatenKotNameFound) {
			log.info("Nama Kabupaten/Kotamadya : "+kabupatenKotTextbox.getValue()+" TIDAK Ditemukan...");
			kabupatenKotRef.setValue("[TIDAK ditemukan]");
			kabupatenKotRef.setStyle("color: red;");
			kabupatenKotReferenceMap.put(Boolean.FALSE, null);
		}
		
		return kabupatenKotNameFound;	
	}

	private boolean kecamatanReferenceCheck(Kabupaten_Kotamadya cloudKabupatenKot) throws Exception {
		boolean kecamatanNameFound = false;		
		if (cloudKabupatenKot==null) {
			// no reference to look for kecamatan
			log.info("cloudKabupatenKot is NULL");
			kecamatanRef.setValue("[TIDAK ditemukan]");
			kecamatanRef.setStyle("color: red;");
			kecamatanReferenceMap.put(Boolean.FALSE, null);
			
			return kecamatanNameFound;
		}
		Kabupaten_KotamadyaDao cloudKabupatenKotDao = (Kabupaten_KotamadyaDao) ctx.getBean("kabupaten_KotamadyaDao");
		Kabupaten_Kotamadya cloudKabupatenKotKecamatanByProxy = cloudKabupatenKotDao.findKecamatanByProxy(cloudKabupatenKot.getId());
		List<Kecamatan> cloudKecamatanList = cloudKabupatenKotKecamatanByProxy.getKecamatans();
		for (Kecamatan cloudKecamatan : cloudKecamatanList) {
			if ((kecamatanTextbox.getValue().compareTo(cloudKecamatan.getNamaKecamatan()))==0) {
				log.info(cloudKecamatan+" dengan Nama Kecamatan: "+cloudKecamatan.getNamaKecamatan()+
						" Ditemukan dibawah Kabupaten/Kot: "+cloudKabupatenKot.getNamaKabupaten());
				kecamatanNameFound = true;
				kecamatanReferenceMap.put(Boolean.TRUE, cloudKecamatan);
				//
				kecamatanRef.setValue("[OK]");
				kecamatanRef.setStyle("color: black;");
				break;				
			}
		}
		if (!kecamatanNameFound) {
			log.info("Nama Kecamatan : "+kecamatanTextbox.getValue()+" TIDAK Ditemukan...");
			kecamatanRef.setValue("[TIDAK ditemukan]");
			kecamatanRef.setStyle("color: red;");
			kecamatanReferenceMap.put(Boolean.FALSE, null);
		}
		
		return kecamatanNameFound;
	}
	
	private Boolean kelurahanReferenceCheck(Kecamatan cloudKecamatan) throws Exception {
		boolean kelurahanNameFound = false;
		if (cloudKecamatan==null) {
			log.info("cloudKecamatan is NULL");
			kelurahanRef.setValue("[TIDAK ditemukan]");
			kelurahanRef.setStyle("color: red;");
			kelurahanReferenceMap.put(Boolean.FALSE, null);
			
			return kelurahanNameFound;
		}
		KecamatanDao cloudKecamatanDao = (KecamatanDao) ctx.getBean("kecamatanDao");
		Kecamatan kecamatanKelurahanByProxy = cloudKecamatanDao.findKelurahanByProxy(cloudKecamatan.getId());
		List<Kelurahan> kelurahanList = kecamatanKelurahanByProxy.getKelurahans();
		for (Kelurahan cloudKelurahan : kelurahanList) {
			if (kelurahanTextbox.getValue().compareTo(cloudKelurahan.getNamaKelurahan())==0) {
				log.info(cloudKelurahan+" dengan Nama Kelurahan: "+cloudKelurahan.getNamaKelurahan()+
						" Ditemukan dibawah Kecamatan : "+cloudKecamatan.getNamaKecamatan());
				kelurahanNameFound = true;
				kelurahanReferenceMap.put(Boolean.TRUE, cloudKelurahan);
				//
				kelurahanRef.setValue("[OK]");
				kelurahanRef.setStyle("color: black;");
				break;				
			}
		}
		if (!kelurahanNameFound) {
			log.info("Nama Kelurahan : "+kelurahanTextbox.getValue()+" TIDAK Ditemukan...");
			kelurahanRef.setValue("[TIDAK ditemukan]");
			kelurahanRef.setStyle("color: red;");
			kelurahanReferenceMap.put(Boolean.FALSE, null);
		}
		
		return kelurahanNameFound;
	}

	public void onCheck$referensiWilayahCheckbox(Event event) throws Exception {
		// to resolve to the cloud -- if necessary		
		log.info("Resolving Kotamaops Wilayah to Cloud...");

		Propinsi cloudPropinsi = propinsiReferenceMap.get(Boolean.TRUE);
		log.info("cloudPropinsi: "+cloudPropinsi);
		
		Kabupaten_Kotamadya cloudKabupatenKot = kabupatenKotReferenceMap.get(Boolean.TRUE);
		log.info("cloudKabupatenKot: "+cloudKabupatenKot);
		if (cloudKabupatenKot==null) {
			cloudKabupatenKot = resolveKabupatenKotamadya(cloudPropinsi);
			
			// add to the reference map for uploading kejadian to cloud
			kabupatenKotReferenceMap.put(Boolean.TRUE, cloudKabupatenKot);

			log.info("cloudKabupatenKot: [Resolved] "+cloudKabupatenKot);			
		}

		Kecamatan cloudKecamatan = kecamatanReferenceMap.get(Boolean.TRUE);
		log.info("cloudKecamatan: "+cloudKecamatan);
		if (cloudKecamatan==null) {
			cloudKecamatan = resolveKecamatan(cloudKabupatenKot);
			
			// add to the reference map for uploading kejadian to cloud
			kecamatanReferenceMap.put(Boolean.TRUE, cloudKecamatan);

			log.info("cloudKecamatan: [Resolved] "+cloudKecamatan);
		}
		
		Kelurahan cloudKelurahan = kelurahanReferenceMap.get(Boolean.TRUE);
		log.info("cloudKelurahan: "+cloudKelurahan);
		if (cloudKelurahan==null) {
			cloudKelurahan = resolveKelurahan(cloudKecamatan);
			
			// add to the reference map for uploading kejadian to cloud
			kelurahanReferenceMap.put(Boolean.TRUE, cloudKelurahan);			

			log.info("cloudKelurahan: [Resolved] "+cloudKelurahan);
		}
		
		referensiWilayahCheckbox.setDisabled(true);
	}

	private Kabupaten_Kotamadya resolveKabupatenKotamadya(Propinsi cloudPropinsi) throws Exception {
		PropinsiDao cloudPropinsiDao = (PropinsiDao) ctx.getBean("propinsiDao");
		// get the propinsi ref -- by proxy
		Propinsi cloudPropinsiKabupatenKotByProxy = 
				cloudPropinsiDao.findKabupatenKotamadyaByProxy(cloudPropinsi.getId());
		
		// create a new Kabupaten_Kotamadya object -- with name (from textbox)
		Kabupaten_Kotamadya cloudKabupatenKot = new Kabupaten_Kotamadya();
		cloudKabupatenKot.setCreatedAt(asDate(getCurrentLocalDateTime()));
		cloudKabupatenKot.setEditedAt(asDate(getCurrentLocalDateTime()));
		cloudKabupatenKot.setNamaKabupaten(kabupatenKotTextbox.getValue());
		
		// add to the list of kabupatenKotamadyas - from propinsi ref by proxy
		cloudPropinsiKabupatenKotByProxy.getKabupatenkotamadyas().add(cloudKabupatenKot);
				
		// update kabupaten using the propinsi proxy
		cloudPropinsiDao.update(cloudPropinsiKabupatenKotByProxy);
		
		// notify
		kabupatenKotRef.setValue("[OK]");
		kabupatenKotRef.setStyle("color: black;");
		
		return cloudKabupatenKot;
	}
	
	private Kecamatan resolveKecamatan(Kabupaten_Kotamadya cloudKabupatenKot) throws Exception {
		Kabupaten_KotamadyaDao cloudKabupatenKotDao = 
				(Kabupaten_KotamadyaDao) ctx.getBean("kabupaten_KotamadyaDao");
		// get the kabupaten kotamadya ref -- by proxy
		Kabupaten_Kotamadya cloudKabupatenKotByProxy = 
				cloudKabupatenKotDao.findKecamatanByProxy(cloudKabupatenKot.getId());
		
		// create a new Kecamatan object -- with name (from textbox)
		Kecamatan cloudKecamatan = new Kecamatan();
		cloudKecamatan.setCreatedAt(asDate(getCurrentLocalDateTime()));
		cloudKecamatan.setEditedAt(asDate(getCurrentLocalDateTime()));
		cloudKecamatan.setNamaKecamatan(kecamatanTextbox.getValue());
		
		// add to the list of kecamatans -- from kabupaten kotamadya ref by proxy
		cloudKabupatenKotByProxy.getKecamatans().add(cloudKecamatan);
		
		// update kecamatan using the kabupaten kotamadya proxy
		cloudKabupatenKotDao.update(cloudKabupatenKotByProxy);
		
		// notify
		kecamatanRef.setValue("[OK]");
		kecamatanRef.setStyle("color: black;");
		
		return cloudKecamatan;
	}	
	
	private Kelurahan resolveKelurahan(Kecamatan cloudKecamatan) throws Exception {
		KecamatanDao cloudKecamatanDao = (KecamatanDao) ctx.getBean("kecamatanDao");
		// get the kecamatan ref -- by proxy
		Kecamatan cloudKecamatanByProxy = cloudKecamatanDao.findKelurahanByProxy(cloudKecamatan.getId());
		
		// create a new Kelurahan object -- with name (from textbox)
		Kelurahan cloudKelurahan = new Kelurahan();
		cloudKelurahan.setCreatedAt(asDate(getCurrentLocalDateTime()));
		cloudKelurahan.setEditedAt(asDate(getCurrentLocalDateTime()));
		cloudKelurahan.setNamaKelurahan(kelurahanTextbox.getValue());
		
		// add to the list of kelurahans -- from kecamatan ref by proxy
		cloudKecamatanByProxy.getKelurahans().add(cloudKelurahan);
		
		// update kelurahan using the kecamatan proxy
		cloudKecamatanDao.update(cloudKecamatanByProxy);
		
		// notify
		kelurahanRef.setValue("[OK]");
		kelurahanRef.setStyle("color: black;");
		
		return cloudKelurahan;
	}		

	/*
	 * REFERENSI KEJADIAN
	 */
	
	private void displayKejadianReferenceCheck() throws Exception {
		
		// jenis kejadian
		boolean referenceJenisPassed = jenisKejadianReferenceCheck();
		
		// motif kejadian
		boolean referenceMotifPassed = motifKejadianReferenceCheck();

		// 
		referensiKejadianCheckbox.setChecked(referenceJenisPassed && referenceMotifPassed);
		referensiKejadianCheckbox.setDisabled(referenceJenisPassed && referenceMotifPassed);
	}
		
	private boolean jenisKejadianReferenceCheck() throws Exception {
		boolean jenisKejadianNameFound = false;
		KejadianJenisDao cloudKejadianJenisDao = (KejadianJenisDao) ctx.getBean("kejadianJenisDao");
		List<KejadianJenis> cloudKejadianJenisList = cloudKejadianJenisDao.findAllKejadianJenis(true);
		for (KejadianJenis cloudKejadianJenis : cloudKejadianJenisList) {
			if (getKejadian().getJenisKejadian().getNamaJenis().compareTo(cloudKejadianJenis.getNamaJenis())==0) {
				log.info(cloudKejadianJenis+" dengan Nama Jenis Kejadian: "+cloudKejadianJenis.getNamaJenis()+" Ditemukan.");
				jenisKejadianNameFound = true;
				kejadianJenisReferenceMap.put(Boolean.TRUE, cloudKejadianJenis);
				//
				jenisKejadianRef.setValue("[OK]");
				jenisKejadianRef.setStyle("color: black;");
				break;
			}
		}
		if (!jenisKejadianNameFound) {
			log.info("Nama Jenis Kejadian : "+jenisKejadianTextbox.getValue()+" TIDAK Ditemukan...");
			jenisKejadianRef.setValue("[TIDAK ditemukan]");
			jenisKejadianRef.setStyle("color: red;");
			kejadianJenisReferenceMap.put(Boolean.FALSE, getKejadian().getJenisKejadian());
		}
		
		return jenisKejadianNameFound;
	}	
	
	private boolean motifKejadianReferenceCheck() throws Exception {
		boolean motifKejadianNameFound = false;
		KejadianMotifDao cloudKejadianMotifDao = (KejadianMotifDao) ctx.getBean("kejadianMotifDao");
		List<KejadianMotif> cloudKejadianMotifList = cloudKejadianMotifDao.findAllKejadianMotif();
		for (KejadianMotif cloudKejadianMotif : cloudKejadianMotifList) {
			if ((getKejadian().getMotifKejadian().getNamaMotif().compareTo(cloudKejadianMotif.getNamaMotif()))==0) {
				log.info(cloudKejadianMotif+" dengan Nama Motif Kejadian: "+cloudKejadianMotif.getNamaMotif()+" Ditemukan.");
				motifKejadianNameFound = true;
				kejadianMotifReferenceMap.put(Boolean.TRUE, cloudKejadianMotif);
				//
				motifKejadianRef.setValue("[OK]");
				motifKejadianRef.setStyle("color: black;");
				break;
			}
		}
		if (!motifKejadianNameFound) {
			log.info("Nama Motif Kejadian : "+motifKejadianTextbox.getValue()+" TIDAK Ditemukan...");
			motifKejadianRef.setValue("[TIDAK ditemukan]");
			motifKejadianRef.setStyle("color: red;");
			kejadianMotifReferenceMap.put(Boolean.FALSE, getKejadian().getMotifKejadian());
		}
		
		return motifKejadianNameFound;
	}		
		
	public void onCheck$referensiKejadianCheckbox(Event event) throws Exception {
		KejadianJenis cloudKejadianJenis = kejadianJenisReferenceMap.get(Boolean.TRUE);
		log.info("cloudKejadianJenis: "+cloudKejadianJenis);
		if (cloudKejadianJenis==null) {
			cloudKejadianJenis = resolveKejadianJenis(jenisKejadianTextbox.getValue());
			
			// add to the reference map for uploading kejadian to cloud
			kejadianJenisReferenceMap.put(Boolean.TRUE, cloudKejadianJenis);
			
			log.info("cloudKejadianJenis: [Resolved] "+cloudKejadianJenis);
		}
		KejadianMotif cloudKejadianMotif = kejadianMotifReferenceMap.get(Boolean.TRUE);
		log.info("cloudKejadianMotif: "+cloudKejadianMotif);
		if (cloudKejadianMotif==null) {
			cloudKejadianMotif = resolveKejadianMotif(motifKejadianTextbox.getValue());
			
			// add to the reference map for uploading kejadian to cloud
			kejadianMotifReferenceMap.put(Boolean.TRUE, cloudKejadianMotif);
			
			log.info("cloudKejadianMotif: [Resolved] "+cloudKejadianMotif);
		}
		
		referensiKejadianCheckbox.setDisabled(true);
	}

	private KejadianJenis resolveKejadianJenis(String kejadianJenisName) throws Exception {
		KejadianJenisDao cloudKejadianJenisDao = (KejadianJenisDao) ctx.getBean("kejadianJenisDao");
		
		// create a new KejadianJenis object -- with name (from textbox)
		KejadianJenis cloudKejadianJenis = new KejadianJenis();
		cloudKejadianJenis.setCreatedAt(asDate(getCurrentLocalDateTime()));
		cloudKejadianJenis.setEditedAt(asDate(getCurrentLocalDateTime()));
		cloudKejadianJenis.setNamaJenis(kejadianJenisName);
		// save
		cloudKejadianJenisDao.save(cloudKejadianJenis);
		// notify
		jenisKejadianRef.setValue("[OK]");
		jenisKejadianRef.setStyle("color: black;");
		
		return cloudKejadianJenis;
	}

	private KejadianMotif resolveKejadianMotif(String kejadianMotifName) throws Exception {
		KejadianMotifDao cloudKejadianMotifDao = (KejadianMotifDao) ctx.getBean("kejadianMotifDao");
		
		// create a new KejadianMotif object -- with name (from textbox)
		KejadianMotif kejadianMotif = new KejadianMotif();
		kejadianMotif.setCreatedAt(asDate(getCurrentLocalDateTime()));
		kejadianMotif.setEditedAt(asDate(getCurrentLocalDateTime()));
		kejadianMotif.setNamaMotif(kejadianMotifName);
		// save
		cloudKejadianMotifDao.save(kejadianMotif);
		// notify
		motifKejadianRef.setValue("[OK]");
		motifKejadianRef.setStyle("color: black;");
		
		return kejadianMotif;
	}	
	
	/*
	 * REFERENSI KERUGIAN
	 */
	
	public void onCheck$referensiKerugianCheckbox(Event event) throws Exception {
		List<Listitem> listitem = kerugianListbox.getItems();
		for (Listitem item : listitem) {
			Listcell lc;
			// Kerugian kerugian = item.getValue();
			
			// kerugian jenis
			lc = (Listcell) item.getChildren().get(1);
			KerugianJenis kerugianJenis = lc.getValue();
			
			// status
			lc = (Listcell) item.getChildren().get(2);
			log.info("Jenis Kerugian: "+lc.getValue());
			if (lc.getValue()==null) {
				// has it been resolved previously?
				KerugianJenis cloudKerugianJenis = kerugianJenisPreviouslyResolved(kerugianJenis);
				if (cloudKerugianJenis==null) {
					// resolve to cloud database
					cloudKerugianJenis = resolveKerugianJenis(kerugianJenis);
				}
				lc.setValue(cloudKerugianJenis);
				// notify
				lc.setLabel("[OK]");
				lc.setStyle("color:black;");
			}
						
			// kerugian kondisi
			lc = (Listcell) item.getChildren().get(3);
			KerugianKondisi kerugianKondisi = lc.getValue();
			
			// status
			lc = (Listcell) item.getChildren().get(4);
			log.info("Kondisi Kerugian: "+lc.getValue());
			if (lc.getValue()==null) {
				// has it been resolved previously?
				KerugianKondisi cloudKerugianKondisi = kerugianKondisiPreviouslyResolved(kerugianKondisi);
				if (cloudKerugianKondisi==null) {
					// resolve to cloud database
					cloudKerugianKondisi = resolveKerugianKondisi(kerugianKondisi);
				}
				lc.setValue(cloudKerugianKondisi);
				// notify
				lc.setLabel("[OK]");
				lc.setStyle("color:black;");
			}
			
			// kerugian satuan
			lc = (Listcell) item.getChildren().get(5);
			KerugianSatuan kerugianSatuan = lc.getValue();

			// status
			lc = (Listcell) item.getChildren().get(6);
			log.info("Satuan Kerugian: "+lc.getValue());
			if (lc.getValue()==null) {
				// has it been resolved previously?
				KerugianSatuan cloudKerugianSatuan = kerugianSatuanPreviouslyResolved(kerugianSatuan);
				if (cloudKerugianSatuan==null) {
					// resolve to cloud database
					cloudKerugianSatuan = resolveKerugianSatuan(kerugianSatuan);
				}
				lc.setValue(cloudKerugianSatuan);
				// notify
				lc.setLabel("[OK]");
				lc.setStyle("color:black;");
			}
		}
		
		referensiKerugianCheckbox.setDisabled(true);
	}

	private KerugianSatuan kerugianSatuanPreviouslyResolved(KerugianSatuan kerugianSatuan) throws Exception {
		KerugianSatuanDao cloudKerugianSatuanDao = (KerugianSatuanDao) ctx.getBean("kerugianSatuanDao");
		List<KerugianSatuan> cloudKerugianSatuanList = cloudKerugianSatuanDao.findAllKerugianSatuan(true);
		for (KerugianSatuan cloudKerugianSatuan : cloudKerugianSatuanList) {
			if (kerugianSatuan.getSatuan().compareTo(cloudKerugianSatuan.getSatuan())==0) {
				return cloudKerugianSatuan;
			}
		}
		return null;
	}

	private KerugianKondisi kerugianKondisiPreviouslyResolved(KerugianKondisi kerugianKondisi) throws Exception {
		KerugianKondisiDao cloudKerugianKondisiDao = (KerugianKondisiDao) ctx.getBean("kerugianKondisiDao");
		List<KerugianKondisi> cloudKerugianKondisiList = cloudKerugianKondisiDao.findAllKerugianKondisi(true);
		for (KerugianKondisi cloudKerugianKondisi : cloudKerugianKondisiList) {
			if (kerugianKondisi.getNamaKondisi().compareTo(cloudKerugianKondisi.getNamaKondisi())==0) {
				return cloudKerugianKondisi;
			}
		}
		return null;
	}

	private KerugianJenis kerugianJenisPreviouslyResolved(KerugianJenis kerugianJenis) throws Exception {
		KerugianJenisDao cloudKerugianJenisDao = (KerugianJenisDao) ctx.getBean("kerugianJenisDao");
		List<KerugianJenis> cloudKerugianJenisList = cloudKerugianJenisDao.findAllKerugianJenis(true);
		for (KerugianJenis cloudKerugianJenis : cloudKerugianJenisList) {
			if (kerugianJenis.getNamaJenis().compareTo(cloudKerugianJenis.getNamaJenis())==0) {
				return cloudKerugianJenis;
			}
		}
		return null;
	}

	private KerugianJenis resolveKerugianJenis(KerugianJenis kerugianJenis) throws Exception {
		KerugianJenisDao cloudKerugianJenisDao = (KerugianJenisDao) ctx.getBean("kerugianJenisDao");
		
		// create a new kerugianJenis -- coming from the listbox
		KerugianJenis cloudKerugianJenis = new KerugianJenis();
		cloudKerugianJenis.setCreatedAt(asDate(getCurrentLocalDateTime()));
		cloudKerugianJenis.setEditedAt(asDate(getCurrentLocalDateTime()));
		cloudKerugianJenis.setTipeKerugian(kerugianJenis.getTipeKerugian());
		cloudKerugianJenis.setNamaJenis(kerugianJenis.getNamaJenis());
		// save
		cloudKerugianJenisDao.save(cloudKerugianJenis);
		
		return cloudKerugianJenis;
	}	
	
	private KerugianKondisi resolveKerugianKondisi(KerugianKondisi kerugianKondisi) throws Exception {
		KerugianKondisiDao cloudKerugianKondisiDao = (KerugianKondisiDao) ctx.getBean("kerugianKondisiDao");
		
		// create a new kerugianKondisi -- coming from the listbox
		KerugianKondisi cloudKerugianKondisi = new KerugianKondisi();
		cloudKerugianKondisi.setCreatedAt(asDate(getCurrentLocalDateTime()));
		cloudKerugianKondisi.setEditedAt(asDate(getCurrentLocalDateTime()));
		cloudKerugianKondisi.setNamaKondisi(kerugianKondisi.getNamaKondisi());
		// save
		cloudKerugianKondisiDao.save(cloudKerugianKondisi);
		
		return cloudKerugianKondisi;
	}	
	
	private KerugianSatuan resolveKerugianSatuan(KerugianSatuan kerugianSatuan) throws Exception {
		KerugianSatuanDao cloudKerugianSatuanDao = (KerugianSatuanDao) ctx.getBean("kerugianSatuanDao");
		
		// create a new kerugianSatuan -- coming from the listbox
		KerugianSatuan cloudKerugianSatuan = new KerugianSatuan();
		cloudKerugianSatuan.setCreatedAt(asDate(getCurrentLocalDateTime()));
		cloudKerugianSatuan.setEditedAt(asDate(getCurrentLocalDateTime()));
		cloudKerugianSatuan.setSatuan(kerugianSatuan.getSatuan());
		// save
		cloudKerugianSatuanDao.save(cloudKerugianSatuan);
		
		return cloudKerugianSatuan;
	}
	
	public void onClick$synchronizeToCloudButton(Event event) throws Exception {
		if (!referensiWilayahCheckbox.isChecked()) {
			throw new Exception("Referensi Wilayah BELUM ada resolusi.");
		}
		if (!referensiKejadianCheckbox.isChecked()) {
			throw new Exception("Referensi Kejadian BELUM ada resolusi.");
		}
		if (!referensiKerugianCheckbox.isChecked()) {
			throw new Exception("Referensi Kerugian BELUM ada resolusi.");
		}
		
		// create kejadian data in the cloud
		KejadianDao cloudKejadianDao = (KejadianDao) ctx.getBean("kejadianDao");
		try {
			cloudKejadianDao.save(kejadianToCloud(getKejadian()));
			//
			actionSuccessLabel.setValue("Sinkronisasi ke Pusdalops BERHASIL.");
		} catch (Exception e) {
			throw new Exception("Sinkronisasi ke Pusdalops TIDAK Berhasil.");
		}
			
		// if success - update local kejadian synch_datetime column
		Kejadian localKejadian = getKejadian();
		localKejadian.setSynchAt(asDate(getCurrentLocalDateTime()));
		// update
		getKejadianDao().update(localKejadian);
		
		synchronizeToCloudButton.setDisabled(true);
	}
	
	private Kejadian kejadianToCloud(Kejadian kejadian) {
		Kejadian cloudKejadian = new Kejadian();
		cloudKejadian.setCreatedAt(asDate(getCurrentLocalDateTime()));
		cloudKejadian.setEditedAt(asDate(getCurrentLocalDateTime()));
		cloudKejadian.setSerialNumber(getKejadianSerialNUmber(kejadian.getSerialNumber()));

		cloudKejadian.setTwPembuatanDateTime(kejadian.getTwPembuatanDateTime());
		cloudKejadian.setTwPembuatanTimezone(kejadian.getTwPembuatanTimezone());
		cloudKejadian.setTwKejadianDateTime(kejadian.getTwKejadianDateTime());
		cloudKejadian.setTwKejadianTimezone(kejadian.getTwKejadianTimezone());
		
		//  `kotamaops_id_fk` bigint(20) DEFAULT NULL,
		cloudKejadian.setKotamaops(kejadian.getKotamaops());
		//  `propinsi_id_fk` bigint(20) DEFAULT NULL,
		cloudKejadian.setPropinsi(kejadian.getPropinsi());
		//  `kabupaten_id_fk` bigint(20) DEFAULT NULL,
		cloudKejadian.setKabupatenKotamadya(kabupatenKotReferenceMap.get(Boolean.TRUE));
		//	`kecamatan_id_fk` bigint(20) DEFAUL NULL,
		cloudKejadian.setKecamatan(kecamatanReferenceMap.get(Boolean.TRUE));
		//  `kelurahan_id_fk` bigint(20) DEFAULT NULL,
		cloudKejadian.setKelurahan(kelurahanReferenceMap.get(Boolean.TRUE));

		//  `koord_gps` varchar(255) DEFAULT NULL,
		cloudKejadian.setKoordinatGps(kejadian.getKoordinatGps());
		//  `koor_peta` varchar(255) DEFAULT NULL,
		cloudKejadian.setKoordinatPeta(kejadian.getKoordinatPeta());
		//	`bujur_lintang` VARCHAR(255) NULL
		cloudKejadian.setBujurLintang(kejadian.getBujurLintang());
		//  `kampung` varchar(255) DEFAULT NULL,
		cloudKejadian.setKampung(kejadian.getKampung());
		//  `jalan` varchar(255) DEFAULT NULL,
		cloudKejadian.setJalan(kejadian.getJalan());
		//  `kronologis` varchar(3060) DEFAULT NULL,
		cloudKejadian.setKronologis(kejadian.getKronologis());
				
		//  `jenis_kejadian_id_fk` bigint(20) DEFAULT NULL,
		cloudKejadian.setJenisKejadian(kejadianJenisReferenceMap.get(Boolean.TRUE));
		//  `motif_kejadian_id_fk` bigint(20) DEFAULT NULL,
		cloudKejadian.setMotifKejadian(kejadianMotifReferenceMap.get(Boolean.TRUE));
		
		cloudKejadian.setPelakuKejadian(kejadian.getPelakuKejadian());
		cloudKejadian.setKeteranganPelaku(kejadian.getKeteranganPelaku());
		
		//  `sasaran` varchar(255) DEFAULT NULL,
		cloudKejadian.setSasaran(kejadian.getSasaran());
		
		// kerugian
		cloudKejadian.setKerugians(getKerugians(new ArrayList<Kerugian>()));
		
		// kotamaops_synch_date column
		cloudKejadian.setKotamaopsSynchAt(asDate(getCurrentLocalDateTime()));
		cloudKejadian.setSynchByKotamaops(getSinkronisasiData().getSynchByKotamaops());
		
		return cloudKejadian;
	}

	private List<Kerugian> getKerugians(List<Kerugian> kerugianList) {
		List<Listitem> listitem = kerugianListbox.getItems();
		for (Listitem item : listitem) {
			Listcell lc;
			// kerugian
			Kerugian kerugian = item.getValue();
			// kerugian jenis
			lc = (Listcell) item.getChildren().get(2);
			KerugianJenis cloudKerugianJenis = lc.getValue();
			// kerugian kondisi
			lc = (Listcell) item.getChildren().get(4);
			KerugianKondisi cloudKerugianKondisi = lc.getValue();
			// kerugian satuan
			lc = (Listcell) item.getChildren().get(6);
			KerugianSatuan cloudKerugianSatuan = lc.getValue();
			
			Kerugian cloudKerugian = new Kerugian();
			cloudKerugian.setCreatedAt(asDate(getCurrentLocalDateTime()));
			cloudKerugian.setEditedAt(asDate(getCurrentLocalDateTime()));
			cloudKerugian.setNamaMaterial(kerugian.getNamaMaterial());
			cloudKerugian.setParaPihak(kerugian.getParaPihak());
			cloudKerugian.setLembagaTerkait(kerugian.getLembagaTerkait());
			cloudKerugian.setKerugianJenis(cloudKerugianJenis);
			cloudKerugian.setTipeKerugian(cloudKerugianJenis.getTipeKerugian());
			cloudKerugian.setKerugianKondisi(cloudKerugianKondisi);
			cloudKerugian.setJumlah(kerugian.getJumlah());
			cloudKerugian.setKerugianSatuan(cloudKerugianSatuan);
			cloudKerugian.setKeterangan(kerugian.getKeterangan());
			
			kerugianList.add(cloudKerugian);
		}
		
		return kerugianList;
	}

	private DocumentSerialNumber getKejadianSerialNUmber(DocumentSerialNumber serialNumber) {
		DocumentSerialNumber serialNum = new DocumentSerialNumber();
		serialNum.setCreatedAt(serialNumber.getCreatedAt());
		serialNum.setEditedAt(serialNumber.getEditedAt());
		serialNum.setDocumentCode(serialNumber.getDocumentCode());
		serialNum.setSerialDate(serialNumber.getSerialDate());
		serialNum.setSerialNo(serialNumber.getSerialNo());
		serialNum.setSerialComp(serialNumber.getSerialComp());
		
		return serialNum;
	}

	public void onClick$closeButton(Event event) throws Exception {
		Events.sendEvent(Events.ON_CLOSE, sinkronisasiToCloudDialogWin, event);
		
		sinkronisasiToCloudDialogWin.detach();
	}

	public SinkronisasiData getSinkronisasiData() {
		return sinkronisasiData;
	}

	public void setSinkronisasiData(SinkronisasiData sinkronisasiData) {
		this.sinkronisasiData = sinkronisasiData;
	}

	public Kejadian getKejadian() {
		return kejadian;
	}

	public void setKejadian(Kejadian kejadian) {
		this.kejadian = kejadian;
	}

	public LocalDateTime getCurrentLocalDateTime() {
		return currentLocalDateTime;
	}

	public void setCurrentLocalDateTime(LocalDateTime currentLocalDateTime) {
		this.currentLocalDateTime = currentLocalDateTime;
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

	public KejadianDao getKejadianDao() {
		return kejadianDao;
	}

	public void setKejadianDao(KejadianDao kejadianDao) {
		this.kejadianDao = kejadianDao;
	}

	public Kabupaten_KotamadyaDao getKabupaten_KotamadyaDao() {
		return kabupaten_KotamadyaDao;
	}

	public void setKabupaten_KotamadyaDao(Kabupaten_KotamadyaDao kabupaten_KotamadyaDao) {
		this.kabupaten_KotamadyaDao = kabupaten_KotamadyaDao;
	}

	public TimezoneInd getTimezoneInd() {
		return timezoneInd;
	}

	public void setTimezoneInd(TimezoneInd timezoneInd) {
		this.timezoneInd = timezoneInd;
	}
}
