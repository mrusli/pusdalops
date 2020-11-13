package mil.pusdalops.domain.authorization;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import mil.pusdalops.domain.common.IdBasedObject;
import mil.pusdalops.domain.common.SchemaUtil;
import mil.pusdalops.domain.kotamaops.Kotamaops;

@Entity
@Table(name = "auth_user",  schema = SchemaUtil.SCHEMA_COMMON)
public class User extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9032047941701878062L;

	//  `user_name` varchar(255) DEFAULT NULL,
	//  UNIQUE KEY `user_name` (`user_name`),
	@Column(name = "user_name")
	private String userName;
	
	//  `user_password` varchar(255) DEFAULT NULL,
	@Column(name = "user_password")
	private String userPassword;
	
	//  `enabled` char(1) DEFAULT NULL,
	@Column(name = "enabled")
	@Type(type="true_false")	
	private boolean active;
	
	//  `kotamaops_id_fk` bigint(20) DEFAULT NULL,
	//  KEY `key_kotamaops_id_01` (`kotamaops_id_fk`),
	//  CONSTRAINT `fk_kotamaops_01` FOREIGN KEY (`kotamaops_id_fk`) REFERENCES `kotamaops` (`id`)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kotamaops_id_fk")
	private Kotamaops kotamaops;
		
	// IMPORTANT: set orphanRemoval to false, otherwise when this user removes a role,
	// the role in the UserRole table will be removed as well !!!
	// IMPORTANT: cascade is to CascadeType.MERGE, CascadeType.PERSIST so that when this user
	// is deleted, the role associated with this user is NOT deleted
	// ref: https://stackoverflow.com/questions/20980759/remove-parent-entity-without-removing-children-jpa
	@OneToMany(cascade={ CascadeType.MERGE, CascadeType.PERSIST }, orphanRemoval=false, fetch=FetchType.EAGER)	
	@JoinTable(
			name="auth_user_join_role",
			joinColumns = @JoinColumn(name="id_user"),
			inverseJoinColumns = @JoinColumn(name="id_role"))	
	private Set<UserRole> userRoles;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [userName=" + userName + 
				", userPassword=" + userPassword + 
				", active=" + active + 
				// ", createDate=" + createDate + 
				// ", lastEditDate=" + lastEditDate + 
				", userRoles=" + userRoles + "]";
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userPassword
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * @param userPassword the userPassword to set
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the kotamaops
	 */
	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	/**
	 * @param kotamaops the kotamaops to set
	 */
	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	
}
