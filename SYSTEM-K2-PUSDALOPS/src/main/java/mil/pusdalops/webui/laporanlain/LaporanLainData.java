package mil.pusdalops.webui.laporanlain;

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.laporanlain.LaporanLain;

public class LaporanLainData {

	private LaporanLain laporanLain;
	
	private Kotamaops settingsKotamaops;

	public LaporanLain getLaporanLain() {
		return laporanLain;
	}

	public void setLaporanLain(LaporanLain laporanLain) {
		this.laporanLain = laporanLain;
	}

	public Kotamaops getSettingsKotamaops() {
		return settingsKotamaops;
	}

	public void setSettingsKotamaops(Kotamaops settingsKotamaops) {
		this.settingsKotamaops = settingsKotamaops;
	}
	
}
