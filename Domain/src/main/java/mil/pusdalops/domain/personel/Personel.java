package mil.pusdalops.domain.personel;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import mil.pusdalops.domain.authorization.User;
import mil.pusdalops.domain.common.IdBasedObject;
import mil.pusdalops.domain.common.SchemaUtil;
import mil.pusdalops.domain.kotamaops.Kotamaops;

@Entity
@Table(name = "personel", schema = SchemaUtil.SCHEMA_COMMON)
public class Personel extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7423696007004257164L;

	//  `personel_type` int(11) NOT NULL,
	@Column(name = "personel_type")
	@Enumerated(EnumType.ORDINAL)
	private PersonelType personelType;	
	
	//  `active` char(1) DEFAULT NULL,
	@Column(name = "active")
	@Type(type = "true_false")
	private boolean active;
	
	//  `personel_admitted` date DEFAULT NULL,
	@Column(name = "personel_admitted")
	@Temporal(TemporalType.DATE)
	private Date admittedDate;
	
	//  `personel_discharged` date DEFAULT NULL,
	@Column(name = "personel_discharged")
	@Temporal(TemporalType.DATE)
	private Date dischargedDate;
	
	//  `name` varchar(255) DEFAULT NULL,
	@Column(name = "name")
	private String personelName;
	
	//  UNIQUE KEY `id_nrp_no` (`id`,`nrp_no`)
	//  `nrp_no` varchar(255) DEFAULT NULL,
	@Column(name = "nrp_no")
	private String nrp;
	
	//  `gender` int(11) DEFAULT NULL,
	@Column(name = "gender")
	@Enumerated(EnumType.ORDINAL)
	private PersonelGender gender;
	
	//  `religion` int(11) DEFAULT NULL,
	@Column(name = "religion")
	@Enumerated(EnumType.ORDINAL)
	private PersonelReligion religion;
	
	//  `address01` varchar(255) DEFAULT NULL,
	@Column(name = "address01")
	private String address01;
	
	//  `address02` varchar(255) DEFAULT NULL,
	@Column(name = "address02")
	private String address02;
	
	//  `city` varchar(255) DEFAULT NULL,
	@Column(name = "city")
	private String city;
	
	//  `postal_code` varchar(255) DEFAULT NULL,
	@Column(name = "postal_code")
	private String postalCode;
	
	//  `phone` varchar(255) DEFAULT NULL,
	@Column(name = "phone")
	private String phone;
	
	//  `email` varchar(255) DEFAULT NULL,
	@Column(name = "email")
	private String email;
	
	//  `note` varchar(255) DEFAULT NULL,
	@Column(name = "note")
	private String note;

	@ManyToOne
	@JoinTable(
			name = "kotamaops_join_personel",
			joinColumns = @JoinColumn(name = "id_personel"),
			inverseJoinColumns = @JoinColumn(name = "id_kotamaops"))
	private Kotamaops kotamaops;
	
	@OneToMany(cascade = { CascadeType.ALL }, orphanRemoval=false, fetch=FetchType.LAZY)
	@JoinTable(
			name = "personel_join_auth_user",
			joinColumns = @JoinColumn(name = "id_personel"),
			inverseJoinColumns = @JoinColumn(name = "id_auth_user"))
	private List<User> authorizedUsers;
	
	/**
	 * @return the personelType
	 */
	public PersonelType getPersonelType() {
		return personelType;
	}

	/**
	 * @param personelType the personelType to set
	 */
	public void setPersonelType(PersonelType personelType) {
		this.personelType = personelType;
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
	 * @return the admittedDate
	 */
	public Date getAdmittedDate() {
		return admittedDate;
	}

	/**
	 * @param admittedDate the admittedDate to set
	 */
	public void setAdmittedDate(Date admittedDate) {
		this.admittedDate = admittedDate;
	}

	/**
	 * @return the dischargedDate
	 */
	public Date getDischargedDate() {
		return dischargedDate;
	}

	/**
	 * @param dischargedDate the dischargedDate to set
	 */
	public void setDischargedDate(Date dischargedDate) {
		this.dischargedDate = dischargedDate;
	}

	/**
	 * @return the personelName
	 */
	public String getPersonelName() {
		return personelName;
	}

	/**
	 * @param personelName the personelName to set
	 */
	public void setPersonelName(String personelName) {
		this.personelName = personelName;
	}

	/**
	 * @return the nrp
	 */
	public String getNrp() {
		return nrp;
	}

	/**
	 * @param nrp the nrp to set
	 */
	public void setNrp(String nrp) {
		this.nrp = nrp;
	}

	/**
	 * @return the gender
	 */
	public PersonelGender getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(PersonelGender gender) {
		this.gender = gender;
	}

	/**
	 * @return the religion
	 */
	public PersonelReligion getReligion() {
		return religion;
	}

	/**
	 * @param religion the religion to set
	 */
	public void setReligion(PersonelReligion religion) {
		this.religion = religion;
	}

	/**
	 * @return the address01
	 */
	public String getAddress01() {
		return address01;
	}

	/**
	 * @param address01 the address01 to set
	 */
	public void setAddress01(String address01) {
		this.address01 = address01;
	}

	/**
	 * @return the address02
	 */
	public String getAddress02() {
		return address02;
	}

	/**
	 * @param address02 the address02 to set
	 */
	public void setAddress02(String address02) {
		this.address02 = address02;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	public List<User> getAuthorizedUsers() {
		return authorizedUsers;
	}

	public void setAuthorizedUsers(List<User> authorizedUsers) {
		this.authorizedUsers = authorizedUsers;
	}

	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}
}
