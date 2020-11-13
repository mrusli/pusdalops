package mil.pusdalops.webui.login;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Window;

import mil.pusdalops.webui.common.GFCBaseController;

public class LoginController extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6225376022842016686L;
	
	private final Logger log = Logger.getLogger(LoginController.class);
	
	private Window mainLoginDiv;
	
	public void onCreate$mainLoginDiv(Event event) throws Exception {
		log.info("Creating login controller object");

		Properties mainProperties = new Properties();
		mainProperties.load(new FileInputStream("/pusdalops/application-back-end.properties"));
		log.info("Start the application: "+mainProperties.get("application.title"));

	}

	public void onClick$mainLoginDiv(Event event) throws Exception {
		Map<String,Object> arg = Collections.singletonMap("", null);
		Window loginWin = (Window) Executions.createComponents("/login/LoginWinDialog.zul", mainLoginDiv, arg);
		loginWin.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				log.info("Login Canceled");
				
			}
		});
		loginWin.doModal();
	}
}
