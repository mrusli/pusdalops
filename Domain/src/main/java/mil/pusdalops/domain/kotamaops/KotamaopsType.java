package mil.pusdalops.domain.kotamaops;

public enum KotamaopsType {
	MATRA_DARAT(0), PUSDALOPS(1), MATRA_UDARA(2), MATRA_LAUT(3);
	
	private int value;
	
	KotamaopsType(int value) {
		setValue(value);
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public static String toString(int value) {
		switch (value) {
			case 0: return "MATRA_DARAT";
			case 1: return "PUSDALOPS";
			case 2: return "MATRA_UDARA";
			case 3: return "MATRA_LAUT";
			default:
				return null;
		}
	}
	
	public static KotamaopsType toKotamaopsType(int value) {
		switch (value) {
			case 0: return MATRA_DARAT;
			case 1: return PUSDALOPS;
			case 2: return MATRA_UDARA;
			case 3: return MATRA_LAUT;
			default:
				return null;
		}
	}
	
}
