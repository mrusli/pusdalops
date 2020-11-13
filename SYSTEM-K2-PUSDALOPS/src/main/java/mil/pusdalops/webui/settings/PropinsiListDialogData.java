package mil.pusdalops.webui.settings;

import java.util.List;

import mil.pusdalops.domain.wilayah.Propinsi;

public class PropinsiListDialogData {

	private List<Propinsi> propinsisToExclue;

	/**
	 * @return the propinsisToExclue
	 */
	public List<Propinsi> getPropinsisToExclue() {
		return propinsisToExclue;
	}

	/**
	 * @param propinsisToExclue the propinsisToExclue to set
	 */
	public void setPropinsisToExclue(List<Propinsi> propinsisToExclue) {
		this.propinsisToExclue = propinsisToExclue;
	}
}
