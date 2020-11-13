package mil.pusdalops.webui.rekap;

import java.math.BigInteger;

import mil.pusdalops.domain.kejadian.KejadianMotif;

public class KejadianMotifCount {

	private KejadianMotif kejadianMotif;
	
	private BigInteger count;

	public KejadianMotif getKejadianMotif() {
		return kejadianMotif;
	}

	public void setKejadianMotif(KejadianMotif kejadianMotif) {
		this.kejadianMotif = kejadianMotif;
	}

	public BigInteger getCount() {
		return count;
	}

	public void setCount(BigInteger count) {
		this.count = count;
	}
	
}
