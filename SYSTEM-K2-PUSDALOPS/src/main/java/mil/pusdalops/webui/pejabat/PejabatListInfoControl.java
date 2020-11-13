package mil.pusdalops.webui.pejabat;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.domain.pejabat.Pejabat;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.persistence.pejabat.dao.PejabatDao;
import mil.pusdalops.persistence.settings.dao.SettingsDao;
import mil.pusdalops.webui.common.GFCBaseController;

public class PejabatListInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7473259578786980905L;

	private SettingsDao settingsDao;
	private PejabatDao pejabatDao;
	
	private Window pejabatListInfoWin;
	private Label formTitleLabel, infoResultlabel;
	private Listbox pejabatListbox;
	
	private Settings settings;
	private Kotamaops kotamaops;
	
	private List<Pejabat> pejabatList;
	
	private static final Logger log = Logger.getLogger(PejabatListInfoControl.class);
	private final long SETTINGS_DEFAULT_ID = 1L;
	
	public void onCreate$pejabatListInfoWin(Event event) throws Exception {
		setSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		setKotamaops(
				getSettings().getSelectedKotamaops());
		log.info("Creating Pejabat ListInfo for Kotamaops: "+getKotamaops().getKotamaopsName());
		
		formTitleLabel.setValue("Data Input | Data Pejabat - Kotamaops: "+
				getKotamaops().getKotamaopsName());
		
		loadDataPejabat();
		
		displayDataPejabatListInfo();
	}

	private void loadDataPejabat() throws Exception {
		if (getKotamaops().getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0) {
			// load all data pejabat
			setPejabatList(getPejabatDao().findAllPejabat(true));
			
		} else {
			// load data pejabat for the kotamaops only
			setPejabatList(getPejabatDao().findAllPejabatByKotamaops(true, getKotamaops()));
		}
	}

	private void displayDataPejabatListInfo() {
		pejabatListbox.setModel(new ListModelList<Pejabat>(getPejabatList()));
		pejabatListbox.setItemRenderer(getPejabatListitemRenderer());
	}

	private ListitemRenderer<Pejabat> getPejabatListitemRenderer() {
		
		return new ListitemRenderer<Pejabat>() {
			
			@Override
			public void render(Listitem item, Pejabat pejabat, int index) throws Exception {
				Listcell lc;
				
				// Kotamops
				Pejabat pejabatKotamaopsByProxy = 
						getPejabatDao().findPejabatKotamaopsByProxy(pejabat.getId());
				lc = new Listcell(
						pejabatKotamaopsByProxy.getKotamaops().getKotamaopsName());
				lc.setParent(item);
				
				// Nama
				lc = new Listcell(pejabat.getNama());
				lc.setParent(item);
				
				// Pangkat
				lc = new Listcell(pejabat.getPangkat());
				lc.setParent(item);
				
				// Jabatan
				lc = new Listcell(pejabat.getJabatan());
				lc.setParent(item);
				
				// NRP
				lc = new Listcell(pejabat.getNrp());
				lc.setParent(item);

				// edit
				lc = initEdit(new Listcell(), pejabat);
				lc.setParent(item);

				item.setValue(pejabat);
			}

			private Listcell initEdit(Listcell listcell, Pejabat pejabat) {
				Button editButton = new Button();
				editButton.setLabel("...");
				editButton.setSclass("listinfoEditButton");
				editButton.setParent(listcell);
				editButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						PejabatData pejabatData = new PejabatData();
						pejabatData.setPejabat(pejabat);
						pejabatData.setKotamaops(getKotamaops());
						
						Map<String, PejabatData> arg = Collections.singletonMap("pejabatData", pejabatData);
						Window pejabatDialogWin = 
								(Window) Executions.createComponents("/pejabat/PejabatDialog.zul", pejabatListInfoWin, arg);
						pejabatDialogWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								Pejabat pejabat = (Pejabat) event.getData();
								
								// save
								getPejabatDao().update(pejabat);
								
								// load
								loadDataPejabat();
								
								// display
								displayDataPejabatListInfo();
							}
						});
						
						pejabatDialogWin.doModal();						
					}
				});
				
				return listcell;
			}
		};
	}

	public void onAfterRender$pejabatListbox(Event event) throws Exception {
		infoResultlabel.setValue("Total: "+pejabatListbox.getItemCount()+" pejabat");
	}
	
	public void onClick$newButton(Event event) throws Exception {
		PejabatData pejabatData = new PejabatData();
		pejabatData.setPejabat(new Pejabat());
		pejabatData.setKotamaops(getKotamaops());
		
		Map<String, PejabatData> arg = Collections.singletonMap("pejabatData", pejabatData);
		Window pejabatDialogWin = 
				(Window) Executions.createComponents("/pejabat/PejabatDialog.zul", pejabatListInfoWin, arg);
		pejabatDialogWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Pejabat pejabat = (Pejabat) event.getData();
				
				// save
				getPejabatDao().save(pejabat);
				
				// load
				loadDataPejabat();
				
				// display
				displayDataPejabatListInfo();
			}
		});
		
		pejabatDialogWin.doModal();
	}
	
	public PejabatDao getPejabatDao() {
		return pejabatDao;
	}

	public void setPejabatDao(PejabatDao pejabatDao) {
		this.pejabatDao = pejabatDao;
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

	public List<Pejabat> getPejabatList() {
		return pejabatList;
	}

	public void setPejabatList(List<Pejabat> pejabatList) {
		this.pejabatList = pejabatList;
	}
	
}
