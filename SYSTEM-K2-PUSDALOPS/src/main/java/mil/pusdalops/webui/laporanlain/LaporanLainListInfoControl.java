package mil.pusdalops.webui.laporanlain;

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

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.domain.laporanlain.LaporanLain;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.persistence.laporanlain.dao.LaporanLainDao;
import mil.pusdalops.persistence.settings.dao.SettingsDao;
import mil.pusdalops.webui.common.GFCBaseController;

public class LaporanLainListInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8638989431093502315L;

	private SettingsDao settingsDao;
	private LaporanLainDao laporanLainDao;
	
	private Window laporanLainListInfoWin;
	private Label formTitleLabel, infoResultlabel;
	private Listbox laporanLainListbox;
	
	private Settings settings;
	private Kotamaops kotamaops;
	private List<LaporanLain> laporanLainList;
	
	private final long SETTINGS_DEFAULT_ID = 1L;

	public void onCreate$laporanLainListInfoWin(Event event) throws Exception {
		setSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		
		setKotamaops(
				getSettings().getSelectedKotamaops());
		
		formTitleLabel.setValue("Data Input | Laporan Lain - Kotamaops: "+
				getKotamaops().getKotamaopsName());

		loadLaporanLainList();
		
		displayLaporanLainListInfo();
	}

	private void loadLaporanLainList() throws Exception {
		if (getKotamaops().getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0) {
			setLaporanLainList(
					getLaporanLainDao().findAllLaporanLain());			
		} else {
			setLaporanLainList(
					getLaporanLainDao().findAllLaporanLainByKotamaops(getKotamaops()));
		}
	}

	private void displayLaporanLainListInfo() {
		laporanLainListbox.setModel(
				new ListModelList<LaporanLain>(getLaporanLainList()));
		laporanLainListbox.setItemRenderer(
				getLaporanLainListitemRenderer());
	}

	private ListitemRenderer<LaporanLain> getLaporanLainListitemRenderer() {

		return new ListitemRenderer<LaporanLain>() {
			
			@Override
			public void render(Listitem item, LaporanLain laporanLain, int index) throws Exception {
				Listcell lc;
				
				// ID
				lc = new Listcell(laporanLain.getSerialNumber().getSerialComp());
				lc.setParent(item);
				
				// TW
				lc = new Listcell(laporanLain.getTwPembuatanDateTime().toString()+" "+
						laporanLain.getTwPembuatanTimezone().toString());
				lc.setParent(item);
				
				// Kotamops
				LaporanLain laporanLainKotamaopsByProxy = 
						getLaporanLainDao().findLaporanLainKotamaopsByProxy(laporanLain.getId());
				lc = new Listcell(laporanLainKotamaopsByProxy.getKotamaops().getKotamaopsName());
				lc.setParent(item);
				
				// Judul / Topik
				lc = new Listcell(laporanLain.getJudulLaporan());
				lc.setStyle("white-space: nowrap;");
				lc.setParent(item);
				
				// Laporan
				lc = new Listcell(laporanLain.getIsiLaporan());
				lc.setStyle("white-space: nowrap;");
				lc.setParent(item);

				// edit
				lc = initEdit(new Listcell(), laporanLain);
				lc.setParent(item);

				item.setValue(laporanLain);
			}

			private Listcell initEdit(Listcell listcell, LaporanLain laporanLain) {
				Button editButton = new Button();
				editButton.setLabel("...");
				editButton.setSclass("listinfoEditButton");
				editButton.setParent(listcell);
				editButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						LaporanLainData laporanLainData = new LaporanLainData();
						laporanLainData.setLaporanLain(laporanLain);
						laporanLainData.setSettingsKotamaops(getKotamaops());
						Map<String, LaporanLainData> arg = Collections.singletonMap("laporanLainData", laporanLainData);
						Window laporanLainDialogWin = 
								(Window) Executions.createComponents("/laporanlain/LaporanLainDialog.zul", laporanLainListInfoWin, arg);
						laporanLainDialogWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								LaporanLain laporanLain = (LaporanLain) event.getData();
								
								// update
								getLaporanLainDao().update(laporanLain);
								
								// re-load
								loadLaporanLainList();
								
								// re-display
								displayLaporanLainListInfo();
							}
						});
						laporanLainDialogWin.doModal();
					}
				});
				
				return listcell;
			}
		};
	}

	public void onAfterRender$laporanLainListbox(Event event) throws Exception {
		infoResultlabel.setValue("Total: "+laporanLainListbox.getItemCount()+" Laporan Lain");
	}
	
	public void onClick$newButton(Event event) throws Exception {
		LaporanLainData laporanLainData = new LaporanLainData();
		laporanLainData.setLaporanLain(new LaporanLain());
		laporanLainData.setSettingsKotamaops(getKotamaops());
		Map<String, LaporanLainData> args = Collections.singletonMap("laporanLainData", laporanLainData);
		
		Window laporanLainDialogWin = 
				(Window) Executions.createComponents("/laporanlain/LaporanLainDialog.zul", laporanLainListInfoWin, args);
		laporanLainDialogWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				LaporanLain laporanLain = (LaporanLain) event.getData();
				
				// save
				getLaporanLainDao().save(laporanLain);
				
				// re-load
				loadLaporanLainList();
				
				// re-display
				displayLaporanLainListInfo();
			}
		});
		laporanLainDialogWin.doModal();
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

	public SettingsDao getSettingsDao() {
		return settingsDao;
	}

	public void setSettingsDao(SettingsDao settingsDao) {
		this.settingsDao = settingsDao;
	}

	public List<LaporanLain> getLaporanLainList() {
		return laporanLainList;
	}

	public void setLaporanLainList(List<LaporanLain> laporanLainList) {
		this.laporanLainList = laporanLainList;
	}

	public LaporanLainDao getLaporanLainDao() {
		return laporanLainDao;
	}

	public void setLaporanLainDao(LaporanLainDao laporanLainDao) {
		this.laporanLainDao = laporanLainDao;
	}
}
