package mil.pusdalops.domain.pending;

public enum PendingStatus {
	Pending(0), Approved(1);
	
	int value;
	
	PendingStatus(int value) {
		this.value = value;
	}
	
	public String toString(int value) {
		switch (value) {
			case 0: return "Pending";
			case 1: return "Approved";
		default:
			return null;
		}
	}
	
	public String toStringInd(int value) {
		switch (value) {
			case 0: return "Tertunda";
			case 1: return "Setuju";
		default:
			return null;
		}		
	}
	
	public PendingStatus toPendingStatus (int value) {
		switch (value) {
			case 0: return Pending;
			case 1: return Approved;
			default:
				return null;
		}
	}
}
