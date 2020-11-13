package mil.pusdalops.domain.personel;

public enum PersonelGender {
	Pria(0), Wanita(1);

	private int value;
	
	private PersonelGender(int value) {
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
			case 0:	return "Pria";
			case 1: return "Wanita";
	
			default:
				return null;
		}
	}
	
	public PersonelGender toEmployeeGender(int value) {
		switch (value) {
			case 0:	return PersonelGender.Pria;
			case 1: return PersonelGender.Wanita;
	
			default:
				return null;
		}		
	}
	
}
