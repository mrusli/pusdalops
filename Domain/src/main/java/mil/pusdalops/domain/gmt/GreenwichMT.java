package mil.pusdalops.domain.gmt;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import mil.pusdalops.domain.common.IdBasedObject;
import mil.pusdalops.domain.common.SchemaUtil;

/**
 * 
 * @author rusli
 *
 */
@Entity
@Table(name = "gmt", schema = SchemaUtil.SCHEMA_COMMON)
public class GreenwichMT extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3805370972237769296L;

	// `name` varchar(255) DEFAULT NULL,
	@Column(name = "name")
	private String name;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "gmt_join_gmt_timezone",
			joinColumns = @JoinColumn(name = "id_gmt"),
			inverseJoinColumns = @JoinColumn(name = "id_gmt_timezone"))
	private List<Timezone> timezones;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Timezone> getTimezones() {
		return timezones;
	}

	public void setTimezones(List<Timezone> timezones) {
		this.timezones = timezones;
	}
}
