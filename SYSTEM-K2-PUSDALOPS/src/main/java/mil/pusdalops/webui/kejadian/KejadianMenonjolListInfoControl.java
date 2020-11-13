package mil.pusdalops.webui.kejadian;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kerugian.Pihak;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.persistence.kejadian.dao.KejadianDao;
import mil.pusdalops.persistence.settings.dao.SettingsDao;
import mil.pusdalops.webui.common.GFCBaseController;

public class KejadianMenonjolListInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8339162966385303228L;

	private SettingsDao settingsDao;
	private KejadianDao kejadianDao;
	
	private Window kejadianMenonjolListInfoWin;
	private Label formTitleLabel, infoResultlabel;
	private Listbox kejadianListbox;
	
	private Settings settings;
	private Kotamaops kotamaops;
	private List<Kejadian> kejadianList;
	
	private final long SETTINGS_DEFAULT_ID = 1L;
	
	public void onCreate$kejadianMenonjolListInfoWin(Event event) throws Exception {
		setSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		
		setKotamaops(
				getSettings().getSelectedKotamaops());
		
		formTitleLabel.setValue("Data Input | Kejadian Menonjol - Kotamaops: "+
				getKotamaops().getKotamaopsName());

		// load kejadian list
		loadKejadianList();
		
		displayKejadianListInfo();
	}
	
	private void loadKejadianList() throws Exception {
		if (getKotamaops().getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0) {
			// ascending order, newest kejadian first in the list
			setKejadianList(
					getKejadianDao().findAllKejadian(true));
		} else {
			// display kejadian for this kotamaops only
			// by ascending order, newest kejadian first in the list
			setKejadianList(
					getKejadianDao().findAllKejadianByKotamaops(getKotamaops(), true));
		}
	}

	private void displayKejadianListInfo() {
		kejadianListbox.setModel(
				new ListModelList<Kejadian>(getKejadianList()));
		kejadianListbox.setItemRenderer(getKejadianListitemRenderer());
		
	}

	private ListitemRenderer<Kejadian> getKejadianListitemRenderer() {
		
		return new ListitemRenderer<Kejadian>() {
			
			@Override
			public void render(Listitem item, Kejadian kejadian, int index) throws Exception {
				Listcell lc;
				
				// ID
				lc = new Listcell(kejadian.getSerialNumber().getSerialComp());
				lc.setParent(item);
				
				// TW
				lc = new Listcell(kejadian.getTwPembuatanDateTime().toString()+" "+kejadian.getTwPembuatanTimezone().toString());
				lc.setParent(item);
				
				// Kotamops
				Kejadian kejadianByProxy = getKejadianDao().findKejadianKotamaopsByProxy(kejadian.getId());
				lc = new Listcell(kejadianByProxy.getKotamaops().getKotamaopsName());
				lc.setParent(item);
				
				// Jenis
				String namaJenisKejadian = kejadian.getJenisKejadian()==null ? 
						"" :  kejadian.getJenisKejadian().getNamaJenis();
				lc = new Listcell(namaJenisKejadian);
				lc.setParent(item);

				// Motif
				String namaMotifKejadian = kejadian.getMotifKejadian()==null ?
						"" : kejadian.getMotifKejadian().getNamaMotif();
				lc = new Listcell(namaMotifKejadian);
				lc.setParent(item);
								
				// edit
				lc = initEdit(new Listcell(), kejadian);
				lc.setParent(item);

				// Kerugian
				lc = initKerugian(new Listcell(), kejadian);
				lc.setParent(item);
				
				// Sinkronisasi
				lc = new Listcell(kejadian.getSynchAt()==null? " " : kejadian.getSynchAt().toString());
				lc.setParent(item);
				
				item.setValue(kejadian);
			}

			private Listcell initKerugian(Listcell listcell, Kejadian kejadian) {
				Hlayout hlayout = new Hlayout();
				// PIHAK_KITA
				Button phkKitaButton = new Button();
				phkKitaButton.setLabel("Pihak Kita");
				phkKitaButton.setClass("phkKitaEditButton");
				phkKitaButton.setParent(hlayout);
				phkKitaButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						KejadianData kejadianData = new KejadianData();
						kejadianData.setParaPihak(Pihak.KITA);
						kejadianData.setKejadian(kejadian);
						
						Map<String, KejadianData> args = Collections.singletonMap("kejadianData", kejadianData);
						
						Window kerugianDialogWin = 
								(Window) Executions.createComponents(
										"/kejadian/KerugianListInfo.zul", kejadianMenonjolListInfoWin, args);
						
						kerugianDialogWin.doModal();
					}

				});
				// PIHAK MUSUH
				Button phkMusuhButton = new Button();
				phkMusuhButton.setLabel("Pihak Musuh");
				phkMusuhButton.setClass("phkMusuhEditButton");
				phkMusuhButton.setParent(hlayout);
				phkMusuhButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						KejadianData kejadianData = new KejadianData();
						kejadianData.setParaPihak(Pihak.MUSUH);
						kejadianData.setKejadian(kejadian);
						
						Map<String, KejadianData> args = Collections.singletonMap("kejadianData", kejadianData);
						
						Window kerugianDialogWin = 
								(Window) Executions.createComponents(
										"/kejadian/KerugianListInfo.zul", kejadianMenonjolListInfoWin, args);
						
						kerugianDialogWin.doModal();
						
					}
				});
				// PIHAK LAIN-LAIN
				Button phkLainButton = new Button();
				phkLainButton.setLabel("Pihak Lain");
				phkLainButton.setClass("phkLainEditButton");
				phkLainButton.setParent(hlayout);
				phkLainButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						KejadianData kejadianData = new KejadianData();
						kejadianData.setParaPihak(Pihak.LAIN_LAIN);
						kejadianData.setKejadian(kejadian);
						
						Map<String, KejadianData> args = Collections.singletonMap("kejadianData", kejadianData);
						
						Window kerugianDialogWin = 
								(Window) Executions.createComponents(
										"/kejadian/KerugianListInfo.zul", kejadianMenonjolListInfoWin, args);
						
						kerugianDialogWin.doModal();
						
					}
				});

				hlayout.setParent(listcell);
				
				return listcell;
			}

			private Listcell initEdit(Listcell listcell, Kejadian kejadian) {
				Button editButton = new Button();
				editButton.setLabel("...");
				editButton.setClass("listinfoEditButton");
				editButton.setParent(listcell);
				editButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						KejadianData kejadianData = new KejadianData();
						kejadianData.setKejadian(kejadian);
						kejadianData.setSettingsKotamaops(getKotamaops());
						Map<String, KejadianData> args = Collections.singletonMap("kejadianData", kejadianData);
						Window kejadianDialogWin = (Window) Executions.createComponents(
								"/kejadian/KejadianMenonjolDialog.zul", kejadianMenonjolListInfoWin, args);
						kejadianDialogWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								Kejadian kejadian = (Kejadian) event.getData();
								
								// update
								getKejadianDao().update(kejadian);
								
								// re-index
								getKejadianDao().createIndexer();
								
								// load
								loadKejadianList();
								
								// display
								displayKejadianListInfo();
							}
						});
						
						kejadianDialogWin.doModal();
					}
				});
				
				return listcell;
			}
		};
	}

	public void onAfterRender$kejadianListbox(Event event) throws Exception {
		infoResultlabel.setValue("Total: " + kejadianListbox.getItemCount() + " kejadian");
	}
	
	public void onClick$newButton(Event event) throws Exception {
		KejadianData kejadianData = new KejadianData();
		kejadianData.setKejadian(new Kejadian());
		kejadianData.setSettingsKotamaops(getKotamaops());
		
		Map<String, KejadianData> args = Collections.singletonMap("kejadianData", kejadianData);
		Window kejadianDialogWin = (Window) Executions.createComponents(
				"/kejadian/KejadianMenonjolDialog.zul", kejadianMenonjolListInfoWin, args);
		kejadianDialogWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Kejadian kejadian = (Kejadian) event.getData();
				
				// save
				getKejadianDao().save(kejadian);
				
				// re-index
				getKejadianDao().createIndexer();
				
				// load
				loadKejadianList();
				
				// display
				displayKejadianListInfo();
			}
		});
		kejadianDialogWin.doModal();
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

	public KejadianDao getKejadianDao() {
		return kejadianDao;
	}

	public void setKejadianDao(KejadianDao kejadianDao) {
		this.kejadianDao = kejadianDao;
	}

	public List<Kejadian> getKejadianList() {
		return kejadianList;
	}

	public void setKejadianList(List<Kejadian> kejadianList) {
		this.kejadianList = kejadianList;
	}
}
