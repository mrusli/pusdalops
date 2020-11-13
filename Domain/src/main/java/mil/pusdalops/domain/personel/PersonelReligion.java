package mil.pusdalops.domain.personel;

public enum PersonelReligion {
	Islam(0), Kristen(1), Katolik(2), Buddha(3), Hindu(4), LainLain(5);

	private int value;

	private PersonelReligion(int value) {
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
			case 0: return "Islam";
			case 1: return "Kristen";
			case 2: return "Katolik";
			case 3: return "Buddha";
			case 4: return "Hindu";
			case 5: return "LainLain";
			default:
				return null;
		}
	}
	
	public PersonelReligion toEmployeeReligion(int value) {
		switch (value) {
			case 0: return PersonelReligion.Islam;
			case 1: return PersonelReligion.Kristen;
			case 2: return PersonelReligion.Katolik;
			case 3: return PersonelReligion.Buddha;
			case 4: return PersonelReligion.Hindu;
			case 5: return PersonelReligion.LainLain;
			default:
				return null;
		}		
	}
	
}
