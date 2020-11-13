package mil.pusdalops.webui.common.dialogs;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.webui.common.GFCBaseController;

public class TextEntryDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7957547468429205120L;

	private Window textEntryDialogWin;
	private Label nameLabel;
	private Textbox entryTextbox;
	
	private String name;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		name = (String) arg.get("name");
	}
	
	public void onCreate$textEntryDialogWin(Event event) throws Exception {
		nameLabel.setValue(name);
	}

	public void onClick$changeButton(Event event) throws Exception {
		Events.sendEvent(Events.ON_CHANGE, textEntryDialogWin, entryTextbox.getValue());
		
		textEntryDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		textEntryDialogWin.detach();
	}
	
}
