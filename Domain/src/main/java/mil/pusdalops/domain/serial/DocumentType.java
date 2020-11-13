package mil.pusdalops.domain.serial;

public enum DocumentType {
	KOARMABAR(0), 
	KOARMATIM(1),
	KODAM_I_BUKITBARISAN(2),
	KODAM_II_SRIWIJAYA(3),
	KODAM_III_SILIWANGI(4),
	KODAM_ISKANDARMUDA(5),
	KODAM_IV_DIPONEGORO(6),
	KODAM_IX_UDAYANA(7),
	KODAM_JAYA(8),
	KODAM_LUARNEGERI(9),
	KODAM_V_BRAWIJAYA(10),
	KODAM_VI_MULAWARMAN(11),
	KODAM_VII_WIRABUANA(12),
	KODAM_XII_TANJUNGPURA(13),
	KODAM_XVI_PATTIMURA(14),
	KODAM_XVII_CENDERAWASIH(15),
	KODAM_XIV_HASANUDDIN(16),
	PUSDALOPS_PUSAT(17),
	KOOPSAU_I(18),
	KOOPSAU_II(19),
	KOSTRAD(20),
	KOLINLAMIL(21),
	KOHANUDNAS(22),
	KOPASSUS(23),
	KORPSMAR(24),
	MABESAD(25);

	private int value;
	
	DocumentType(int value) {
		setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public String toCode(int value) {
		switch (value) {
			case 0: return "KRMB";
			case 1: return "KRMT";
			case 2: return "BKBS";
			case 3: return "SWJY";			
			case 4: return "SLWG";
			case 5: return "ISKM";
			case 6: return "DPGR";
			case 7: return "UDYN";
			case 8: return "JAYA";			
			case 9: return "LRNG";
			case 10: return "BWJY";
			case 11: return "MLWR";
			case 12: return "WRBN";
			case 13: return "TJPR";			
			case 14: return "PTMR";
			case 15: return "CDRH";
			case 16: return "HSND";
			case 17: return "OPSP";
			case 18: return "KAU1";			
			case 19: return "KAU2";
			case 20: return "KTRD";
			case 21: return "KLLL";
			case 22: return "KHNS";
			case 23: return "KPSS";			
			case 24: return "KRPM";
			case 25: return "MBSD";
			default:
				return null;
		}
	}
	
	public String toString(int value) {
		switch (value) {
			case 0: return "KOARMABAR";
			case 1: return "KOARMATIM";
			case 2: return "KODAM_I_BUKITBARISAN";
			case 3: return "KODAM_II_SRIWIJAYA";			
			case 4: return "KODAM_III_SILIWANGI";
			case 5: return "KODAM_ISKANDARMUDA";
			case 6: return "KODAM_IV_DIPONEGORO";
			case 7: return "KODAM_IX_UDAYANA";
			case 8: return "KODAM_JAYA";			
			case 9: return "KODAM_LUARNEGERI";
			case 10: return "KODAM_V_BRAWIJAYA";
			case 11: return "KODAM_VI_MULAWARMAN";
			case 12: return "KODAM_VII_WIRABUANA";
			case 13: return "KODAM_XII_TANJUNGPURA";			
			case 14: return "KODAM_XVI_PATTIMURA";
			case 15: return "KODAM_XVII_CENDERAWASIH";
			case 16: return "KODAM_XIV_HASANUDDIN";
			case 17: return "PUSDALOPS_PUSAT";
			case 18: return "KOOPSAU_I";			
			case 19: return "KOOPSAU_II";
			case 20: return "KOSTRAD";
			case 21: return "KOLINLAMIL";
			case 22: return "KOHANUDNAS";
			case 23: return "KOPASSUS";			
			case 24: return "KORPSMAR";
			case 25: return "MABESAD";
			default:
				return null;
		}
	}
	
	public DocumentType toDocumentType(int value) {
		switch(value) {
			case 0: return KOARMABAR;
			case 1: return KOARMATIM;
			case 2: return KODAM_I_BUKITBARISAN;
			case 3: return KODAM_II_SRIWIJAYA;
			case 4: return KODAM_III_SILIWANGI;
			case 5: return KODAM_ISKANDARMUDA;
			case 6: return KODAM_IV_DIPONEGORO;
			case 7: return KODAM_IX_UDAYANA;
			case 8: return KODAM_JAYA;
			case 9: return KODAM_LUARNEGERI;
			case 10: return KODAM_V_BRAWIJAYA;
			case 11: return KODAM_VI_MULAWARMAN;
			case 12: return KODAM_VII_WIRABUANA;
			case 13: return KODAM_XII_TANJUNGPURA;
			case 14: return KODAM_XVI_PATTIMURA;
			case 15: return KODAM_XVII_CENDERAWASIH;
			case 16: return KODAM_XIV_HASANUDDIN;
			case 17: return PUSDALOPS_PUSAT;
			case 18: return KOOPSAU_I;
			case 19: return KOOPSAU_II;
			case 20: return KOSTRAD;
			case 21: return KOLINLAMIL;
			case 22: return KOHANUDNAS;
			case 23: return KOPASSUS;
			case 24: return KORPSMAR;
			case 25: return MABESAD;
			default:
				return null;
		}
	}
}
