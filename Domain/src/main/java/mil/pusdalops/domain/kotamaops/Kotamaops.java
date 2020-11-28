package mil.pusdalops.domain.kotamaops;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import mil.pusdalops.domain.common.IdBasedObject;
import mil.pusdalops.domain.common.SchemaUtil;
import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.personel.Personel;
import mil.pusdalops.domain.wilayah.Propinsi;

@Entity
@Table(name = "kotamaops", schema = SchemaUtil.SCHEMA_COMMON)
public class Kotamaops extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6832943931715238645L;

	//  `type` int(11) DEFAULT NULL,
	@Column(name = "type")
	@Enumerated(EnumType.ORDINAL)
	private KotamaopsType kotamaopsType;
	
	//  `legal_name` varchar(255) DEFAULT NULL,
	@Column(name = "legal_name")
	private String kotamaopsName;
	
	//  `display_name` varchar(255) DEFAULT NULL,
	@Column(name = "display_name")
	private String kotamaopsDisplayName;
	
	@Column(name = "image_id")
	private String imagedId;

	//	`image_id_01` VARCHAR(225) NULL
	@Column(name = "image_id_01")
	private String imageId01;
	
	//  `address_01` varchar(255) DEFAULT NULL,
	@Column(name = "address_01")
	private String address01;
	
	//  `address_02` varchar(255) DEFAULT NULL,
	@Column(name = "address_02")
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
	
	//  `fax` varchar(255) DEFAULT NULL,
	@Column(name = "fax")
	private String fax;
	
	//  `timezone` int(11) DEFAULT NULL,
	@Column(name = "timezone")
	@Enumerated(EnumType.ORDINAL)
	private TimezoneInd timeZone;
	
	// `doc_code` varchar(4)
	@Column(name = "doc_code")
	private String documentCode;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	@JoinTable(
			name = "kotamaops_join_personel",
			joinColumns = @JoinColumn(name = "id_kotamaops"),
			inverseJoinColumns = @JoinColumn(name = "id_personel"))
	private List<Personel> personels;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=false, fetch=FetchType.LAZY)
	@JoinTable(
			name = "kotamaops_join_propinsi",
			joinColumns = @JoinColumn(name = "id_kotamaops"),
			inverseJoinColumns = @JoinColumn(name = "id_propinsi"))
	private List<Propinsi> propinsis;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
	@JoinTable(
			name = "kotamaops_join_kotamaops",
			joinColumns = @JoinColumn(name = "id_kotamaops_m"),
			inverseJoinColumns = @JoinColumn(name = "id_kotamaops_d"))
	private List<Kotamaops> kotamaops;
	
	@Override
	public String toString() {
		return "Kotamaops [kotamaopsType=" + kotamaopsType + ", kotamaopsName=" + kotamaopsName
				+ ", kotamaopsDisplayName=" + kotamaopsDisplayName + ", imagedId=" + imagedId + ", imageId01="
				+ imageId01 + ", address01=" + address01 + ", address02=" + address02 + ", city=" + city
				+ ", postalCode=" + postalCode + ", phone=" + phone + ", email=" + email + ", fax=" + fax
				+ ", timeZone=" + timeZone + ", documentCode=" + documentCode + "]";
	}

	/**
	 * @return the kotamaopsType
	 */
	public KotamaopsType getKotamaopsType() {
		return kotamaopsType;
	}

	/**
	 * @param kotamaopsType the kotamaopsType to set
	 */
	public void setKotamaopsType(KotamaopsType kotamaopsType) {
		this.kotamaopsType = kotamaopsType;
	}

	/**
	 * @return the kotamaopsName
	 */
	public String getKotamaopsName() {
		return kotamaopsName;
	}

	/**
	 * @param kotamaopsName the kotamaopsName to set
	 */
	public void setKotamaopsName(String kotamaopsName) {
		this.kotamaopsName = kotamaopsName;
	}

	/**
	 * @return the kotamaopsDisplayName
	 */
	public String getKotamaopsDisplayName() {
		return kotamaopsDisplayName;
	}

	/**
	 * @param kotamaopsDisplayName the kotamaopsDisplayName to set
	 */
	public void setKotamaopsDisplayName(String kotamaopsDisplayName) {
		this.kotamaopsDisplayName = kotamaopsDisplayName;
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
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	public List<Personel> getPersonels() {
		return personels;
	}

	public void setPersonels(List<Personel> personels) {
		this.personels = personels;
	}

	public List<Propinsi> getPropinsis() {
		return propinsis;
	}

	public void setPropinsis(List<Propinsi> propinsis) {
		this.propinsis = propinsis;
	}

	public String getImagedId() {
		return imagedId;
	}

	public void setImagedId(String imagedId) {
		this.imagedId = imagedId;
	}

	public TimezoneInd getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimezoneInd timeZone) {
		this.timeZone = timeZone;
	}

	public List<Kotamaops> getKotamaops() {
		return kotamaops;
	}

	public void setKotamaops(List<Kotamaops> kotamaops) {
		this.kotamaops = kotamaops;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((address01 == null) ? 0 : address01.hashCode());
		result = prime * result + ((address02 == null) ? 0 : address02.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result + ((imagedId == null) ? 0 : imagedId.hashCode());
		result = prime * result + ((kotamaopsDisplayName == null) ? 0 : kotamaopsDisplayName.hashCode());
		result = prime * result + ((kotamaopsName == null) ? 0 : kotamaopsName.hashCode());
		result = prime * result + ((kotamaopsType == null) ? 0 : kotamaopsType.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
		result = prime * result + ((timeZone == null) ? 0 : timeZone.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass())
			return false;
		Kotamaops other = (Kotamaops) obj;
		if (address01 == null) {
			if (other.address01 != null)
				return false;
		} else if (!address01.equals(other.address01))
			return false;
		if (address02 == null) {
			if (other.address02 != null)
				return false;
		} else if (!address02.equals(other.address02))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (kotamaopsName == null) {
			if (other.kotamaopsName != null)
				return false;
		} else if (!kotamaopsName.equals(other.kotamaopsName))
			return false;
		if (kotamaopsType != other.kotamaopsType)
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (postalCode == null) {
			if (other.postalCode != null)
				return false;
		} else if (!postalCode.equals(other.postalCode))
			return false;
		if (timeZone != other.timeZone)
			return false;
		return true;
	}

	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	public String getImageId01() {
		return imageId01;
	}

	public void setImageId01(String imageId01) {
		this.imageId01 = imageId01;
	}
	
}
