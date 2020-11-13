package mil.pusdalops.domain.kerugian;

public enum Pihak {
	KITA(0), MUSUH(1), LAIN_LAIN(2);
	
	private int value;
	
	Pihak(int value) {
		setValue(value);
	}

	public String toString(int value) {
		switch (value) {
			case 0: return "KITA";
			case 1: return "MUSUH";	
			case 2: return "LAIN-LAIN";
			default:
				return null;
		}
	}
	
	public Pihak toPihak(int value) {
		switch (value) {
			case 0: return KITA;
			case 1: return MUSUH;
			case 2: return LAIN_LAIN;
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
