package mil.pusdalops.webui.profile;

import java.io.FileInputStream;
import java.util.Properties;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import mil.pusdalops.webui.common.GFCBaseController;

public class DBConnectControl extends GFCBaseController { 

	private static final long serialVersionUID = 1L;

	private Label formTitleLabel;
	private Textbox driverTextbox, urlTextbox, mysqlDialectTextbox, 
		indexBaseTextbox, cloudDriverTextbox, cloudUrlTextbox, cloudMysqlDialectTextbox, 
		cloudIndexBaseTextbox;
	private Properties mainProperties;
	
	public void onCreate$dbConnectWin(Event event) throws Exception {
		formTitleLabel.setValue("Profil | Database Lokal");
		
		// setup the properties
		mainProperties = new Properties();
		mainProperties.load(new FileInputStream("/pusdalops/hibernate.properties"));
		
		// local db
		displayLocalDBConnectionData();
		
		// cloud db
		displayCloudDBConnectionData();
	}


	private void displayLocalDBConnectionData() {
		driverTextbox.setValue(mainProperties.get("hibernate.connection.driver_class").toString());
		urlTextbox.setValue(mainProperties.get("hibernate.connection.url").toString());
		mysqlDialectTextbox.setValue(mainProperties.get("hibernate.dialect").toString());
		indexBaseTextbox.setValue(mainProperties.get("hibernate.search.default.indexBase").toString());
	}
	
	private void displayCloudDBConnectionData() {
		cloudDriverTextbox.setValue(mainProperties.get("hibernate.connection.driver_class").toString()); 
		cloudUrlTextbox.setValue(mainProperties.get("hibernate.connection.url").toString());
		cloudMysqlDialectTextbox.setValue(mainProperties.get("hibernate.dialect").toString());
		cloudIndexBaseTextbox.setValue(mainProperties.get("hibernate.search.default.indexBase").toString());
	}
	
}
