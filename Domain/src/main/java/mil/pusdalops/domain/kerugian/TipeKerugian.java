package mil.pusdalops.domain.kerugian;

public enum TipeKerugian {
	Personil(0), Material(1);
	
	int value;
	
	TipeKerugian(int value) {
		this.value = value;
	}
	
	public String toString(int value) {
		switch (value) {
		case 0: return "Personil";
		case 1: return "Material";
		default:
			return null;
		}
	}
	
	public TipeKerugian toTipeKerugian(int value) {
		switch (value) {
		case 0: return Personil;
		case 1: return Material;
		default:
			return null;
		}		
	}
}
