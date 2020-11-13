package mil.pusdalops.webui.kejadian;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kerugian.TipeKerugian;
import mil.pusdalops.webui.common.GFCBaseController;

public class KerugianJenisTextEntryDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3819041129860934482L;

	private Window kerugianJenisTextEntryDialogWin;
	private Textbox jenisKerugianTextbox;
	private Combobox tipeKerugianCombobox;
	private Checkbox noInfoCheckbox;
	
	public void onCreate$kerugianJenisTextEntryDialogWin(Event event) throws Exception {
		
		loadTipeKerugianCombobox();
		
		tipeKerugianCombobox.setSelectedIndex(0);
	}
	
	private void loadTipeKerugianCombobox() {
		Comboitem comboitem;
		for (TipeKerugian tipeKerugian : TipeKerugian.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(tipeKerugian.toString());
			comboitem.setValue(tipeKerugian);
			comboitem.setParent(tipeKerugianCombobox);
		}
	}
	
	public void onCheck$noInfoCheckbox(Event event) throws Exception {
		if (noInfoCheckbox.isChecked()) {
			// put '-' into the textbox
			jenisKerugianTextbox.setValue("-");
			jenisKerugianTextbox.setDisabled(true);
		} else {
			jenisKerugianTextbox.setValue("");
			jenisKerugianTextbox.setDisabled(false);
		}
	}

	public void onClick$changeButton(Event event) throws Exception {
		KerugianJenisData kerugianJenisData = new KerugianJenisData();
		kerugianJenisData.setNamaKerugianJenis(jenisKerugianTextbox.getValue());
		kerugianJenisData.setTipeKerugian(tipeKerugianCombobox.getSelectedItem().getValue());

		// send event
		Events.sendEvent(Events.ON_CHANGE, kerugianJenisTextEntryDialogWin, kerugianJenisData);
		
		kerugianJenisTextEntryDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		// send event
		Events.sendEvent(Events.ON_CANCEL, kerugianJenisTextEntryDialogWin, null);
		
		kerugianJenisTextEntryDialogWin.detach();
	}
	
}
