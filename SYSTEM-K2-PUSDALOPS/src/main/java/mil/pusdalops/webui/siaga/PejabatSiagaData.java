package mil.pusdalops.webui.siaga;

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.siaga.PejabatSiaga;

public class PejabatSiagaData {

	private PejabatSiaga pejabatSiaga;
	
	private Kotamaops kotamaops;

	public PejabatSiaga getPejabatSiaga() {
		return pejabatSiaga;
	}

	public void setPejabatSiaga(PejabatSiaga pejabatSiaga) {
		this.pejabatSiaga = pejabatSiaga;
	}

	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}
	
}
