package mil.pusdalops.webui.profile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import mil.pusdalops.domain.authorization.User;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.persistence.settings.dao.SettingsDao;
import mil.pusdalops.persistence.user.dao.UserDao;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.security.UserSecurityDetails;

public class UserProfileControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7760924470074495037L;

	private SettingsDao settingsDao;
	private UserDao userDao;
	
	private Label formTitleLabel;
	private Textbox kotamaopsTextbox;
	private Image kotamaopsLogoImage;
	
	private UserSecurityDetails userSecurityDetails;	
	private String namaLoginUser, namaLoginKotamaops;
	
	private Settings settings;
	private Kotamaops loginKotamaops;
	
	private final long SETTINGS_DEFAULT_ID = 1L;
	
	public void onCreate$userProfileWin(Event event) throws Exception {
		setSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		
		setUserSecurityDetails(
				(UserSecurityDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		
		User loginUser = getUserSecurityDetails().getLoginUser();
		User loginUserByProxy = getUserDao().findUserKotamaopsByProxy(loginUser.getId());

		setNamaLoginUser(
				loginUser.getUserName());
		setLoginKotamaops(
				loginUserByProxy.getKotamaops());
		setNamaLoginKotamaops(
				getLoginKotamaops().getKotamaopsName());
		
		formTitleLabel.setValue("Profil Login User : "+getNamaLoginUser()+" ["+getNamaLoginKotamaops()+"]");
		
		// display info
		displayUserProfileInfo();
	}

	private void displayUserProfileInfo() throws IOException {
		Kotamaops settingsKotamaops = getSettings().getSelectedKotamaops();
		
		BufferedImage buffImg = null;
		
		try {
		    buffImg = ImageIO.read(new File("/pusdalops/img/logo/"+settingsKotamaops.getImageId01()));
		} catch (IOException e) {
			throw e;
		}		
		
		kotamaopsLogoImage.setContent(buffImg);
		// kotamaopsLogoImage.setSrc("/img/logo/"+getKotamaops().getImageId01());
		kotamaopsTextbox.setValue(settingsKotamaops.getKotamaopsName());
		// userNameTextbox.setValue(getNamaLoginUser());
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public UserSecurityDetails getUserSecurityDetails() {
		return userSecurityDetails;
	}

	public void setUserSecurityDetails(UserSecurityDetails userSecurityDetails) {
		this.userSecurityDetails = userSecurityDetails;
	}

	public String getNamaLoginUser() {
		return namaLoginUser;
	}

	public void setNamaLoginUser(String namaLoginUser) {
		this.namaLoginUser = namaLoginUser;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public SettingsDao getSettingsDao() {
		return settingsDao;
	}

	public void setSettingsDao(SettingsDao settingsDao) {
		this.settingsDao = settingsDao;
	}

	public String getNamaLoginKotamaops() {
		return namaLoginKotamaops;
	}

	public void setNamaLoginKotamaops(String namaLoginKotamaops) {
		this.namaLoginKotamaops = namaLoginKotamaops;
	}

	public Kotamaops getLoginKotamaops() {
		return loginKotamaops;
	}

	public void setLoginKotamaops(Kotamaops loginKotamaops) {
		this.loginKotamaops = loginKotamaops;
	}
}
