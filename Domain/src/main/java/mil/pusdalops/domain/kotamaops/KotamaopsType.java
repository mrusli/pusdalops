package mil.pusdalops.domain.kotamaops;

public enum KotamaopsType {
	OTHERS(0), PUSDALOPS(1);
	
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
			case 0: return "OTHERS";
			case 1: return "PUSDALOPS";
			default:
				return null;
		}
	}
	
	public static KotamaopsType toKotamaopsType(int value) {
		switch (value) {
			case 0: return OTHERS;
			case 1: return PUSDALOPS;
			default:
				return null;
		}
	}
	
}
