package mil.pusdalops.webui.kejadian;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.webui.common.GFCBaseController;

public class WilayahTextEntryDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 358017646259835957L;

	private Window wilayahTextEntryDialogWin;
	private Label nameLabel;
	private Checkbox noInfoCheckbox;
	private Textbox entryTextbox;
	
	private String name;
	
	private final String NO_INFO = "-Tidak Ada Info-";
	
	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.GenericForwardComposer#doAfterCompose(org.zkoss.zk.ui.Component)
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		name = (String) arg.get("name");
	}

	public void onCreate$wilayahTextEntryDialogWin(Event event) throws Exception {
		nameLabel.setValue(name);
	}
	
	public void onCheck$noInfoCheckbox(Event event) throws Exception {
		if (noInfoCheckbox.isChecked()) {
			entryTextbox.setValue(NO_INFO);
			entryTextbox.setReadonly(true);
		} else {
			entryTextbox.setValue("");
			entryTextbox.setReadonly(true);
		}
	}
	
	public void onClick$changeButton(Event event) throws Exception {
		// send event
		Events.sendEvent(Events.ON_CHANGE, wilayahTextEntryDialogWin, entryTextbox.getValue());
		
		wilayahTextEntryDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		// send event
		Events.sendEvent(Events.ON_CANCEL, wilayahTextEntryDialogWin, null);
		
		wilayahTextEntryDialogWin.detach();
	}
}
