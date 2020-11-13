package mil.pusdalops.webui.sinkronisasi;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Label;

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.persistence.settings.dao.SettingsDao;
import mil.pusdalops.webui.common.GFCBaseController;

public class SinkronisasiFromCloudListInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4753448294861437505L;
	
	private SettingsDao settingsDao;
	
	private Label formTitleLabel;
	
	private Settings settings;
	private Kotamaops kotamaops;
	
	private final long SETTINGS_DEFAULT_ID = 1L;
	
	public void onCreate$sinkronisasiFromCloudlListInfoWin(Event event) throws Exception {
		setSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		
		setKotamaops(
				getSettings().getSelectedKotamaops());
		
		formTitleLabel.setValue("Sinkronisasi | Dari Kotamaops");
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

}
