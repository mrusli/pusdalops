package mil.pusdalops.domain.personel;

public enum PersonelType {
	KADIS(0), KABAG(1), SES(2), WAKA(3), KAPUS(4);
	
	private int value;
	
	private PersonelType(int value) {
		setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public String toString(int value) {
		switch (value) {
			case 0: return "KADIS";
			case 1: return "KABAG";
			case 2: return "SES";
			case 3: return "WAKA";
			case 4: return "KAPUS";
			default:
				return null;
		}
	}
	
	public PersonelType toPersonelType(int value) {
		switch (value) {
			case 0: return PersonelType.KADIS; 
			case 1: return PersonelType.KABAG;
			case 2: return PersonelType.SES;
			case 3: return PersonelType.WAKA;
			case 4: return PersonelType.KAPUS;
		default:
			return null;
		}
	}
	
}
