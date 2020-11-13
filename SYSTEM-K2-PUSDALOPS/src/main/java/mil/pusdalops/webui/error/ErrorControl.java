package mil.pusdalops.webui.error;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import mil.pusdalops.webui.common.GFCBaseController;

public class ErrorControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5225639215905786925L;
	
	private Window errorWin;
	private Label errorMessageLabel;
	
	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.select.SelectorComposer#doAfterCompose(org.zkoss.zk.ui.Component)
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// System.out.println("doAfterCompose...");
		
        //via execution.getAttribute()
        Execution execution = Executions.getCurrent();
        Exception ex1 = (Exception) execution.getAttribute("javax.servlet.error.exception");
        System.out.println(ex1.getMessage()+" - "+ex1.getLocalizedMessage());
 
        //via requestScope map
        Exception ex2 = (Exception) requestScope.get("javax.servlet.error.exception");
        System.out.println(ex2.getMessage()+" - "+ex2.getLocalizedMessage());
        
        errorMessageLabel.setValue(ex2.getMessage().substring(20));
	}
	
	public void onClick$closeButton(Event event) throws Exception {
		errorWin.detach();
	}
}
