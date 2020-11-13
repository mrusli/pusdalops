package mil.pusdalops.domain.gmt;

import java.time.ZoneId;

public enum TimezoneInd {
	WIB(0), WITA(1), WIT(2);
	
	private int value;
	
	TimezoneInd(int value) {
		this.setValue(value);
	}
	
	public String toString(int value) {
		switch (value) {
			case 0: return "WIB";
			case 1: return "WITA";
			case 2: return "WIT";
			default:
				return null;
		}
	}
	
	public TimezoneInd toTimezoneInd(int value) {
		switch (value) {
			case 0: return WIB;
			case 1: return WITA;
			case 2: return WIT;
			default:
				return null;
		}	
	}
	
	public String toStringZoneId(int value) {
		switch (value) {
			case 0: return "Asia/Jakarta";
			case 1: return "Asia/Ujung_Pandang";
			case 2: return "Asia/Jayapura";
			default:
				return null;
		}	
	}

	public ZoneId toZoneId(int value) {
		switch (value) {
			case 0: return ZoneId.of("Asia/Jakarta");
			case 1: return ZoneId.of("Asia/Ujung_Pandang");
			case 2: return ZoneId.of("Asia/Jayapura");
			default:
				return null;
	}	
		
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
