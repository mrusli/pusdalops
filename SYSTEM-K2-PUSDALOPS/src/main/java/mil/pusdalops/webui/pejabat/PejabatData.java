package mil.pusdalops.webui.pejabat;

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.pejabat.Pejabat;

public class PejabatData {

	private Pejabat pejabat;
	
	private Kotamaops kotamaops;

	public Pejabat getPejabat() {
		return pejabat;
	}

	public void setPejabat(Pejabat pejabat) {
		this.pejabat = pejabat;
	}

	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}
	
}
