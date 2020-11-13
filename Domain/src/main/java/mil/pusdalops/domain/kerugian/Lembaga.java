package mil.pusdalops.domain.kerugian;

public enum Lembaga {
	TNI(0), POLRI(1), MASYARAKAT(2);
	
	int value;
	
	Lembaga(int value) {
		this.value = value;
	}
	
	public String toString(int value) {
		switch (value) {
			case 0: return "TNI";
			case 1: return "POLRI";
			case 2: return "Masyarakat";
			default: return null;
		}
	}
	
	public Lembaga toLembaga(int value) {
		switch (value) {
			case 0: return TNI;
			case 1: return POLRI;
			case 2: return MASYARAKAT;
			default: return null;
		}
		
	}
}
