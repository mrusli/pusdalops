package mil.pusdalops.webui.sinkronisasi;

import java.time.LocalDateTime;

import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kotamaops.Kotamaops;

public class SinkronisasiData {
	
	private Kejadian kejadian;

	private LocalDateTime currentLocalDateTime;
	
	private TimezoneInd timezoneInd;
	
	private Kotamaops synchByKotamaops;
	
	public Kejadian getKejadian() {
		return kejadian;
	}

	public void setKejadian(Kejadian kejadian) {
		this.kejadian = kejadian;
	}

	public LocalDateTime getCurrentLocalDateTime() {
		return currentLocalDateTime;
	}

	public void setCurrentLocalDateTime(LocalDateTime currentLocalDateTime) {
		this.currentLocalDateTime = currentLocalDateTime;
	}

	public TimezoneInd getTimezoneInd() {
		return timezoneInd;
	}

	public void setTimezoneInd(TimezoneInd timezoneInd) {
		this.timezoneInd = timezoneInd;
	}

	public Kotamaops getSynchByKotamaops() {
		return synchByKotamaops;
	}

	public void setSynchByKotamaops(Kotamaops synchByKotamaops) {
		this.synchByKotamaops = synchByKotamaops;
	}
	
}
