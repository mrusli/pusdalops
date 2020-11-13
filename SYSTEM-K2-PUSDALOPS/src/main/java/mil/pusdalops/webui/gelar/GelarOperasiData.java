package mil.pusdalops.webui.gelar;

import mil.pusdalops.domain.gelarops.GelarOperasi;
import mil.pusdalops.domain.kotamaops.Kotamaops;

public class GelarOperasiData {

	private GelarOperasi gelarOperasi;
	
	private Kotamaops kotamaops;

	public GelarOperasi getGelarOperasi() {
		return gelarOperasi;
	}

	public void setGelarOperasi(GelarOperasi gelarOperasi) {
		this.gelarOperasi = gelarOperasi;
	}

	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}
	
}
