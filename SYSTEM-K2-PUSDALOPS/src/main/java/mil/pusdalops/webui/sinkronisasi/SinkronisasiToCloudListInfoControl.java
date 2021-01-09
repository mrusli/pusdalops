package mil.pusdalops.webui.sinkronisasi;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.persistence.kejadian.dao.KejadianDao;
import mil.pusdalops.persistence.settings.dao.SettingsDao;
import mil.pusdalops.webui.common.GFCBaseController;

public class SinkronisasiToCloudListInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4955340462065022486L;

	private SettingsDao settingsDao;
	private KejadianDao kejadianDao;

	private Window sinkronisasiToCloudlListInfoWin;
	private Label formTitleLabel, infoResultlabel;
	private Listbox kejadianListbox;
	private Combobox statusCombobox;
	
	private Settings settings;
	private Kotamaops kotamaops;
	private LocalDateTime currentDateTime;
	private TimezoneInd timezoneInd;
	private List<Kejadian> kejadianListToSynchToCloud;
	
	private final long SETTINGS_DEFAULT_ID = 1L;
	
	public void onCreate$sinkronisasiToCloudlListInfoWin(Event event) throws Exception {
		setSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		
		setKotamaops(
				getSettings().getSelectedKotamaops());
		
		// set current datetime from kotamaops settings
		setTimezoneInd(
				getSettings().getSelectedKotamaops().getTimeZone());
		
		int timezoneIndOrdinal = timezoneInd.ordinal();
		// set current time
		setCurrentDateTime(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal)));
		
		formTitleLabel.setValue("Sinkronisasi | Ke Pusdalops - Kotamaops : " +
				getKotamaops().getKotamaopsName());
				
		// load status combobox
		loadStatusCombobox();

		// load non-synch kejadian
		loadNonSynchronizedKejadian();
		
		displayKejadianToSyncToCloud();
	}

	private void loadStatusCombobox() {
		String[] synchStatus = { "Pending" , "Selesai" };
		
		Comboitem comboitem;
		for (String status : synchStatus) {
			comboitem = new Comboitem();
			comboitem.setLabel(status);
			comboitem.setValue(status);
			comboitem.setParent(statusCombobox);
		}
		
		statusCombobox.setSelectedIndex(0);
	}

	public void onSelect$statusCombobox(Event event) throws Exception {
		boolean nonSynchData = statusCombobox.getSelectedItem().getValue().equals("Pending");
		
		setKejadianListToSynchToCloud(
				getKejadianDao().findKotamaopsNonSynchronizedKejadian(getKotamaops(), nonSynchData));		
		
		displayKejadianToSyncToCloud();
	}
	
	private void loadNonSynchronizedKejadian() throws Exception {
		boolean nonSynchData = true;
		
		// condition -- synchAt is null
		setKejadianListToSynchToCloud(
				getKejadianDao().findKotamaopsNonSynchronizedKejadian(getKotamaops(), nonSynchData));		
	}	
	
	private void displayKejadianToSyncToCloud() {
		kejadianListbox.setModel(
				new ListModelList<Kejadian>(getKejadianListToSynchToCloud()));
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
				lc = new Listcell(kejadian.getTwPembuatanDateTime().toString() + " " + 
						kejadian.getTwPembuatanTimezone().toString());
				lc.setParent(item);
				
				// Kotamops
				Kejadian kejadianByProxy = 
						getKejadianDao().findKejadianKotamaopsByProxy(kejadian.getId());
				lc = new Listcell(kejadianByProxy.getKotamaops().getKotamaopsName());
				lc.setParent(item);

				// Jenis
				lc = new Listcell(kejadian.getJenisKejadian().getNamaJenis());
				lc.setStyle("white-space:nowrap;");
				lc.setParent(item);

				// Motif
				lc = new Listcell(kejadian.getMotifKejadian().getNamaMotif());
				lc.setParent(item);
				
				// synchronize to cloud
				lc = initSychronizeToCloud(new Listcell(), kejadian);
				lc.setParent(item);
				
				// Sinkronisasi Pd Tgl.
				if (kejadian.getSynchAt()==null) {
					// pending to synch
					lc = new Listcell("");
				} else {
					// synch completed
					lc = new Listcell(getLocalDateTimeString(asLocalDateTime(kejadian.getSynchAt()), "dd-mm-yyyy"));
				}
				lc.setParent(item);
			}

			private Listcell initSychronizeToCloud(Listcell listcell, Kejadian kejadian) {
				Button synchToCloudButton = new Button();
				synchToCloudButton.setLabel("Sinkronisasi");
				synchToCloudButton.setClass("listinfoEditButton");
				synchToCloudButton.setParent(listcell);
				synchToCloudButton.setDisabled(kejadian.getSynchAt()!=null);
				synchToCloudButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						SinkronisasiData sinkronisasiData = new SinkronisasiData();
						sinkronisasiData.setKejadian(kejadian);
						sinkronisasiData.setCurrentLocalDateTime(getCurrentDateTime());
						sinkronisasiData.setTimezoneInd(getTimezoneInd());
						sinkronisasiData.setSynchByKotamaops(getKotamaops());
						
						Map<String, SinkronisasiData> arg = Collections.singletonMap("sinkronisasiData", sinkronisasiData);
						Window synchronizeToCloudDialogWin = 
								(Window) Executions.createComponents(
										"/sinkronisasi/SinkronisasiToCloudDialog.zul", sinkronisasiToCloudlListInfoWin, arg);
						synchronizeToCloudDialogWin.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								// re-display
								displayKejadianToSyncToCloud();
							}
						});
						
						synchronizeToCloudDialogWin.doModal();
					}
				});
				return listcell;
			}
		};
	}

	public void onAfterRender$kejadianListbox(Event event) throws Exception {
		infoResultlabel.setValue("Total: "+kejadianListbox.getItemCount()+" kejadian");
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

	public List<Kejadian> getKejadianListToSynchToCloud() {
		return kejadianListToSynchToCloud;
	}

	public void setKejadianListToSynchToCloud(List<Kejadian> kejadianListToSynchToCloud) {
		this.kejadianListToSynchToCloud = kejadianListToSynchToCloud;
	}

	public KejadianDao getKejadianDao() {
		return kejadianDao;
	}

	public void setKejadianDao(KejadianDao kejadianDao) {
		this.kejadianDao = kejadianDao;
	}

	public LocalDateTime getCurrentDateTime() {
		return currentDateTime;
	}

	public void setCurrentDateTime(LocalDateTime currentDateTime) {
		this.currentDateTime = currentDateTime;
	}

	public TimezoneInd getTimezoneInd() {
		return timezoneInd;
	}

	public void setTimezoneInd(TimezoneInd timezoneInd) {
		this.timezoneInd = timezoneInd;
	}
}
