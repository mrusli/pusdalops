package mil.pusdalops.webui.login;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Window;

import mil.pusdalops.webui.common.GFCBaseController;

public class LoginDialogController extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2241509324974157802L;

	private Window loginDialogWin;
	
	private static final Logger log = Logger.getLogger(LoginDialogController.class);
	
	public void onCreate$loginDialogWin(Event event) throws Exception {
		Properties mainProperties = new Properties();
		mainProperties.load(new FileInputStream("/pusdalops/application-back-end.properties"));
		log.info("Login to: "+mainProperties.get("application.title"));
		
		loginDialogWin.setTitle(mainProperties.get("application.title").toString());
	}
}
