package mil.pusdalops.webui.main;

import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Include;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;

import mil.pusdalops.domain.authorization.User;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.persistence.user.dao.UserDao;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.security.UserSecurityDetails;

public class Main extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 813995766549816616L;

	private UserDao userDao;

	private Menuitem userNameMenuitem;
	private Menu sinkronisasiMenu, settingsMenu;
	private Include mainInclude;
	
	private UserSecurityDetails userSecurityDetails;
	
	public void onCreate$mainDiv(Event event) throws Exception {
		setUserSecurityDetails(
				(UserSecurityDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		
		User loginUser = getUserSecurityDetails().getLoginUser();
		User loginUserByProxy = getUserDao().findUserKotamaopsByProxy(loginUser.getId());
		
		Kotamaops kotamaops = loginUserByProxy.getKotamaops();	
		String username = getUserSecurityDetails().getLoginUser().getUserName();
		
		// check whether kotamaops PUSAT
		settingsMenu.setVisible(
				kotamaops.getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0);

		// for Kotamaops ONLY
		sinkronisasiMenu.setVisible(
				kotamaops.getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)!=0); 
				
		// set userNameMenuitem with the username and kotamaops
		userNameMenuitem.setLabel(username + " ["+kotamaops.getKotamaopsName()+"]");
		
		mainInclude.setSrc("/home/HomeInfo.zul");
	}
	
	/*
	 * Home Menu
	 */
	
	public void onClick$homeMenuitem(Event event) throws Exception {

		mainInclude.setSrc("/home/HomeInfo.zul");
	}
	
	/*
	 * Data-Input Menu
	 */
	
	public void onClick$inputKejadianMenonjolMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/kejadian/KejadianMenonjolListInfo.zul");
	}

	public void onClick$inputLaporanLainMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/laporanlain/LaporanLainListInfo.zul");
	}
	
	public void onClick$inputPejabatSiagaMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/siaga/PejabatSiagaListInfo.zul");
	}
	
	public void onClick$inputGelarOperasiMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/gelar/GelarOperasiListInfo.zul");
	}

	public void onClick$kronologisKejadianMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/kronologis/KejadianKronologisListInfo.zul");
	}
	
	public void onClick$inputDataPejabatMenuitem(Event event) throws Exception {

		mainInclude.setSrc("/pejabat/PejabatListInfo.zul");
	}
	
	/*
	 * Sinkronisasi
	 */
	
	public void onClick$syncToCloudKejadianMenuItem(Event event) throws Exception {
		// for Kotamaops
		mainInclude.setSrc("/sinkronisasi/SinkronisasiToCloudListInfo.zul");
	}
		
	/*
	 * Rekapitulasi Menu
	 */
	
	public void onClick$rekapitulasiKerugianMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/rekap/KerugianRekapInfo.zul");
	}

	public void onClick$rekapitulasiKejadianMotifMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/rekap/KejadianMotifRekapInfo.zul");
	}
	
	public void onClick$rekapitulasiPelakuSasaranMenuitem(Event event) throws Exception {

		mainInclude.setSrc("/rekap/KejadianPelakuRekapInfo.zul");
	}
	
	public void onClick$intensitasKejadianMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/rekap/KejadianIntensitasInfo.zul");
	}
	
	/*
	 * Laporan Menu
	 */
	
	public void onClick$laporanCetakLaporanRutinMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/laporanrutin/LaporanRutinInfo.zul");
	}
	
	/*
	 * Profil Menu
	 */
	
	public void onClick$userNameMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/profile/UserProfile.zul");
	}

	public void onClick$dbconnectMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/profile/DBConnect.zul");
	}
	
	/*
	 * Settings Menu
	 */
	
	public void onClick$kotamaopsMenuitem(Event event) throws Exception {
		// Kotamaops
		mainInclude.setSrc("/settings/kotamaops/KotamaopsListInfo.zul");
	}
	
	public void onClick$kotamaopsSettingsMenuitem(Event event) throws Exception {
		// Kotamaops-Peruntukan
		mainInclude.setSrc("/settings/authorized/KotamaopsAuthorized.zul");
	}
	
	public void onClick$kotamaopsWilayahMenuitem(Event event) throws Exception {
		// Kotamaops-Propinsi
		mainInclude.setSrc("/settings/kotamaopsprops/KotamaopsWilayah.zul");
	}
	
	public void onClick$wilayahMenuitem(Event event) throws Exception {
		// Wilayah
		mainInclude.setSrc("/settings/Wilayah.zul");
	}
	
	public void onClick$kejadianMenuitem(Event event) throws Exception {
		// Kejadian-Kerugian
		mainInclude.setSrc("/settings/kejadian/KejadianSettings.zul");
	}
		
	public UserSecurityDetails getUserSecurityDetails() {
		return userSecurityDetails;
	}


	public void setUserSecurityDetails(UserSecurityDetails userSecurityDetails) {
		this.userSecurityDetails = userSecurityDetails;
	}


	public UserDao getUserDao() {
		return userDao;
	}


	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
