package mil.pusdalops.webui.home;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

import mil.pusdalops.webui.common.GFCBaseController;

public class HomeInfoControler extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1687599737827611238L;

	private Label mainTitleLabel;
	private Image mainImage;
	
	public void onCreate$homeInfoWin(Event event) throws Exception {
		Properties mainProperties = new Properties();
		mainProperties.load(new FileInputStream("/pusdalops/application-back-end.properties"));

		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(mainProperties.get("application.home.img").toString()));
		} catch (IOException e) {
			throw e;
		}
		mainImage.setContent(img);
		mainTitleLabel.setValue(mainProperties.get("application.title").toString());
	}
}
