package mil.pusdalops.webui.gelar;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.gelarops.GelarOperasi;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.persistence.gelaroperasi.dao.GelarOperasiDao;
import mil.pusdalops.persistence.settings.dao.SettingsDao;
import mil.pusdalops.webui.common.GFCBaseController;

public class GelarOperasiListInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2113941819889037132L;
	
	private SettingsDao settingsDao;
	private GelarOperasiDao gelarOperasiDao;
	
	private Window gelarOperasiListInfoWin;
	private Label formTitleLabel, infoResultlabel;
	private Listbox gelarOperasiLainListbox;
	
	private Settings settings;
	private Kotamaops kotamaops;
	private List<GelarOperasi> gelarOperasiList;
	
	private final long SETTINGS_DEFAULT_ID = 1L;
	
	public void onCreate$gelarOperasiListInfoWin(Event event) throws Exception {
		setSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		
		setKotamaops(
				getSettings().getSelectedKotamaops());
		
		formTitleLabel.setValue("Data Input | Gelar Operasi - Kotamaops: "+
				getKotamaops().getKotamaopsName());
		
		// load
		loadGelarOperasiList();
		
		// display
		displayGelarOperasiListInfo();
	}

	private void loadGelarOperasiList() throws Exception {
		setGelarOperasiList(
				getGelarOperasiDao().findAllGelarOperasi());
	}

	private void displayGelarOperasiListInfo() {
		gelarOperasiLainListbox.setModel(
				new ListModelList<GelarOperasi>(getGelarOperasiList()));
		gelarOperasiLainListbox.setItemRenderer(getGelarOperasiListitemRenderer());
	}

	private ListitemRenderer<GelarOperasi> getGelarOperasiListitemRenderer() {
		
		return new ListitemRenderer<GelarOperasi>() {
			
			@Override
			public void render(Listitem item, GelarOperasi gelarOperasi, int index) throws Exception {
				Listcell lc;
				
				// ID
				lc = new Listcell(gelarOperasi.getSerialNumber().getSerialComp());
				lc.setParent(item);
				
				// TW Awal-Akhir
				lc = new Listcell(
						// twAwal
						getLocalDateTimeString(asLocalDateTime(gelarOperasi.getTwAwalOps()), "YYYY")+"/"+
						getLocalDateTimeString(asLocalDateTime(gelarOperasi.getTwAwalOps()), "MM")+
						getLocalDateTimeString(asLocalDateTime(gelarOperasi.getTwAwalOps()), "dd")+"."+
						getLocalDateTimeString(asLocalDateTime(gelarOperasi.getTwAwalOps()), "HH")+
						getLocalDateTimeString(asLocalDateTime(gelarOperasi.getTwAwalOps()), "mm")+
						" - "+
						getLocalDateTimeString(asLocalDateTime(gelarOperasi.getTwAkhirOps()), "YYYY")+"/"+
						getLocalDateTimeString(asLocalDateTime(gelarOperasi.getTwAkhirOps()), "MM")+
						getLocalDateTimeString(asLocalDateTime(gelarOperasi.getTwAkhirOps()), "dd")+"."+
						getLocalDateTimeString(asLocalDateTime(gelarOperasi.getTwAkhirOps()), "HH")+
						getLocalDateTimeString(asLocalDateTime(gelarOperasi.getTwAkhirOps()), "mm")
						);
				lc.setParent(item);
				
				// Kotamops
				GelarOperasi gelarOperasiKotamaopsByProxy = 
						getGelarOperasiDao().findGelarOperasiKotamaopsByProxy(gelarOperasi.getId());
				lc = new Listcell(gelarOperasiKotamaopsByProxy.getKotamaops().getKotamaopsName());
				lc.setParent(item);
				
				// Disposisi
				lc = new Listcell(gelarOperasi.getDisposisi());
				lc.setParent(item);
				
				// Satuan
				lc = new Listcell(gelarOperasi.getSatuan());
				lc.setParent(item);
				
				// Brigade
				lc = new Listcell(gelarOperasi.getBrigade());
				lc.setParent(item);
				
				// Batalyon
				lc = new Listcell(gelarOperasi.getBatalyon());
				lc.setParent(item);
				
				// Komandan
				lc = new Listcell(gelarOperasi.getKomandan());
				lc.setParent(item);

				// edit
				lc = initEdit(new Listcell(), gelarOperasi);
				lc.setParent(item);

				item.setValue(gelarOperasi);
			}

			private Listcell initEdit(Listcell listcell, GelarOperasi gelarOperasi) {
				Button editButton = new Button();
				editButton.setLabel("...");
				editButton.setSclass("listinfoEditButton");
				editButton.setParent(listcell);
				editButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						GelarOperasiData gelarOperasiData = new GelarOperasiData();
						gelarOperasiData.setGelarOperasi(gelarOperasi);
						gelarOperasiData.setKotamaops(getKotamaops());
						
						Map<String, GelarOperasiData> args = Collections.singletonMap("gelarOperasiData", gelarOperasiData);
						Window gelarOperasiDialogWin = 
								(Window) Executions.createComponents("/gelar/GelarOperasiDialog.zul", gelarOperasiListInfoWin, args);
						gelarOperasiDialogWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								GelarOperasi gelarOperasi = (GelarOperasi) event.getData();
								
								// save
								getGelarOperasiDao().update(gelarOperasi);
								
								// load
								loadGelarOperasiList();
								
								// re-list
								displayGelarOperasiListInfo();
							}
						});
						
						gelarOperasiDialogWin.doModal();

						
						
					}
				});
				
				return listcell;
			}
		};
	}

	public void onAfterRender$gelarOperasiLainListbox(Event event) throws Exception {
		infoResultlabel.setValue("Total: "+gelarOperasiLainListbox.getItemCount()+" gelar operasi");
	}
	
	public void onClick$newButton(Event event) throws Exception {
		GelarOperasiData gelarOperasiData = new GelarOperasiData();
		gelarOperasiData.setGelarOperasi(new GelarOperasi());
		gelarOperasiData.setKotamaops(getKotamaops());
		
		Map<String, GelarOperasiData> args = Collections.singletonMap("gelarOperasiData", gelarOperasiData);
		Window gelarOperasiDialogWin = 
				(Window) Executions.createComponents("/gelar/GelarOperasiDialog.zul", gelarOperasiListInfoWin, args);
		gelarOperasiDialogWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				GelarOperasi gelarOperasi = (GelarOperasi) event.getData();
				
				// save
				getGelarOperasiDao().save(gelarOperasi);
				
				// load
				loadGelarOperasiList();
				
				// re-list
				displayGelarOperasiListInfo();
			}
		});
		
		gelarOperasiDialogWin.doModal();
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

	public List<GelarOperasi> getGelarOperasiList() {
		return gelarOperasiList;
	}

	public void setGelarOperasiList(List<GelarOperasi> gelarOperasiList) {
		this.gelarOperasiList = gelarOperasiList;
	}

	public GelarOperasiDao getGelarOperasiDao() {
		return gelarOperasiDao;
	}

	public void setGelarOperasiDao(GelarOperasiDao gelarOperasiDao) {
		this.gelarOperasiDao = gelarOperasiDao;
	}
}
