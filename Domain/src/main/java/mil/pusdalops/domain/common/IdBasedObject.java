package mil.pusdalops.domain.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Based object for all objects created with unique id and version
 * matching table columns.
 * 
 * @author rusli
 *
 */
@MappedSuperclass
public class IdBasedObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1287317348193019334L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false, unique=true)	
	private Long id = Long.MIN_VALUE;

	@Column(name="version")	
	private Integer version = 1;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name = "updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date editedAt;	
	
	/**
	 * unique id of an object.  If not created yet, the id is Long.MIN_VALUE.
	 * - auto generate (GenerationType.IDENTITY)
	 * - not null (nullable=false)
	 * - always unique (unique=true)
	 * 
	 * @return Long
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the editedAt
	 */
	public Date getEditedAt() {
		return editedAt;
	}

	/**
	 * @param editedAt the editedAt to set
	 */
	public void setEditedAt(Date editedAt) {
		this.editedAt = editedAt;
	}


}
