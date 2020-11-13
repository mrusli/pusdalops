package mil.pusdalops.webui.rekap;

import java.math.BigInteger;

import mil.pusdalops.domain.kejadian.KejadianJenis;

public class KejadianJenisCount {
	
	private KejadianJenis kejadianJenis;
	
	private BigInteger count;

	public KejadianJenis getKejadianJenis() {
		return kejadianJenis;
	}

	public void setKejadianJenis(KejadianJenis kejadianJenis) {
		this.kejadianJenis = kejadianJenis;
	}

	public BigInteger getCount() {
		return count;
	}

	public void setCount(BigInteger count) {
		this.count = count;
	}
	
}
