package mil.pusdalops.domain.settings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import mil.pusdalops.domain.common.IdBasedObject;
import mil.pusdalops.domain.common.SchemaUtil;
import mil.pusdalops.domain.kotamaops.Kotamaops;

@Entity
@Table(name = "settings", schema = SchemaUtil.SCHEMA_COMMON)
public class Settings extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7409658438724679372L;

	//  `name` varchar(255) DEFAULT NULL,
	@Column(name = "name")
	private String settingName;
	
	//  `kotamaops_id_fk` bigint(20) DEFAULT NULL,
	//  KEY `key_kotamaops_id_02` (`kotamaops_id_fk`),
	//  CONSTRAINT `fk_kotamaops_02` FOREIGN KEY (`kotamaops_id_fk`) REFERENCES `kotamaops` (`id`)
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "kotamaops_id_fk")
	private Kotamaops selectedKotamaops;

	/**
	 * @return the settingName
	 */
	public String getSettingName() {
		return settingName;
	}

	/**
	 * @param settingName the settingName to set
	 */
	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	/**
	 * @return
	 */
	public Kotamaops getSelectedKotamaops() {
		return selectedKotamaops;
	}

	/**
	 * @param selectedKotamaops
	 */
	public void setSelectedKotamaops(Kotamaops selectedKotamaops) {
		this.selectedKotamaops = selectedKotamaops;
	}
}
