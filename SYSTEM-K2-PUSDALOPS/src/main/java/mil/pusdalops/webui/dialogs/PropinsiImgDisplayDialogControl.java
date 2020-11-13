package mil.pusdalops.webui.dialogs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Image;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.webui.common.GFCBaseController;

public class PropinsiImgDisplayDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7398174681062574774L;

	private Window propinsiImgDisplayDialogWin;
	private Image propImg01;
	
	private Propinsi propinsi;
	private BufferedImage buffImg = null;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setPropinsi(
				(Propinsi) Executions.getCurrent().getArg().get("propinsi"));
		
	}

	public void onCreate$propinsiImgDisplayDialogWin(Event event) throws Exception {
		// adjust the width of the window
		propinsiImgDisplayDialogWin.setWidth(
				getPropinsi().getImageDisplayWidth());
		displayPropinsiImg();		
	}
	
	private void displayPropinsiImg() throws IOException {
		
		try {
		    buffImg = ImageIO.read(new File("/pusdalops/img/propinsi/"+getPropinsi().getImageId_01()));
		} catch (IOException e) {
			throw e;
		}				

		// propImg01.setSrc("/img/prop/"+propinsi.getImageId_01());
		propImg01.setContent(buffImg);
	}
	
	public void onClick$closeButton(Event event) throws Exception {
		propinsiImgDisplayDialogWin.detach();
	}

	public Propinsi getPropinsi() {
		return propinsi;
	}

	public void setPropinsi(Propinsi propinsi) {
		this.propinsi = propinsi;
	}

	
	
	
}
