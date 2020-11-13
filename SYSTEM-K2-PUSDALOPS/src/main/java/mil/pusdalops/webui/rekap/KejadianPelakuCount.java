package mil.pusdalops.webui.rekap;

import java.math.BigInteger;

import mil.pusdalops.domain.kejadian.KejadianPelaku;

public class KejadianPelakuCount {

	private KejadianPelaku kejadianPelaku;
	
	private BigInteger jumlah;
	
	private String sasaran;

	public KejadianPelaku getKejadianPelaku() {
		return kejadianPelaku;
	}

	public void setKejadianPelaku(KejadianPelaku kejadianPelaku) {
		this.kejadianPelaku = kejadianPelaku;
	}

	public BigInteger getJumlah() {
		return jumlah;
	}

	public void setJumlah(BigInteger jumlah) {
		this.jumlah = jumlah;
	}

	public String getSasaran() {
		return sasaran;
	}

	public void setSasaran(String sasaran) {
		this.sasaran = sasaran;
	}
	
}
