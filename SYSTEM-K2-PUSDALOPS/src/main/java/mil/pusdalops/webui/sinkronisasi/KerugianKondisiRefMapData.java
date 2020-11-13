package mil.pusdalops.webui.sinkronisasi;

import mil.pusdalops.domain.kerugian.KerugianKondisi;

public class KerugianKondisiRefMapData {

	private KerugianKondisi kerugianKondisi;
	
	private int rowIndex;

	public KerugianKondisi getKerugianKondisi() {
		return kerugianKondisi;
	}

	public void setKerugianKondisi(KerugianKondisi kerugianKondisi) {
		this.kerugianKondisi = kerugianKondisi;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	
}
