package mil.pusdalops.webui.dialogs;

import java.util.List;

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;

public class KotamaopsListDialogData {

	private KotamaopsType kotamaopsType;
	
	private List<Kotamaops> kotamaopsToExclude;

	public KotamaopsType getKotamaopsType() {
		return kotamaopsType;
	}

	public void setKotamaopsType(KotamaopsType kotamaopsType) {
		this.kotamaopsType = kotamaopsType;
	}

	public List<Kotamaops> getKotamaopsToExclude() {
		return kotamaopsToExclude;
	}

	public void setKotamaopsToExclude(List<Kotamaops> kotamaopsToExclude) {
		this.kotamaopsToExclude = kotamaopsToExclude;
	}
}
