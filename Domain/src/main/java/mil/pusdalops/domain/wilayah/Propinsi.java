package mil.pusdalops.domain.wilayah;

import java.util.List;

import javax.persistence.CascadeType;
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
@Table(name = "propinsi", schema = SchemaUtil.SCHEMA_COMMON)
public class Propinsi extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -538730198136582478L;

	@Column(name = "nama_propinsi")
	private String namaPropinsi;

	@Column(name = "image_id_01_100")
	private String imageId_01_100;
	
	@Column(name = "image_id_01")
	private String imageId_01;
	
	@Column(name = "image_id_00")
	private String imageId_00;

	@Column(name = "img_display_width")
	private String imageDisplayWidth;
	
	@Override
	public String toString() {
		return "Propinsi [namaPropinsi=" + namaPropinsi + ", imageId_01_100=" + imageId_01_100 + ", imageId_01="
				+ imageId_01 + ", imageId_00=" + imageId_00 + ", imageDisplayWidth=" + imageDisplayWidth + "]";
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.LAZY)
	@JoinTable(
			name = "propinsi_join_kabupaten",
			joinColumns = @JoinColumn(name = "id_propinsi"),
			inverseJoinColumns = @JoinColumn(name = "id_kabupaten"))
	private List<Kabupaten_Kotamadya> kabupatenkotamadyas;
	
	public String getNamaPropinsi() {
		return namaPropinsi;
	}

	public void setNamaPropinsi(String namaPropinsi) {
		this.namaPropinsi = namaPropinsi;
	}

	public List<Kabupaten_Kotamadya> getKabupatenkotamadyas() {
		return kabupatenkotamadyas;
	}

	public void setKabupatenkotamadyas(List<Kabupaten_Kotamadya> kabupatenkotamadyas) {
		this.kabupatenkotamadyas = kabupatenkotamadyas;
	}

	public String getImageId_01_100() {
		return imageId_01_100;
	}

	public void setImageId_01_100(String imageId_01_100) {
		this.imageId_01_100 = imageId_01_100;
	}

	public String getImageId_01() {
		return imageId_01;
	}

	public void setImageId_01(String imageId_01) {
		this.imageId_01 = imageId_01;
	}

	public String getImageId_00() {
		return imageId_00;
	}

	public void setImageId_00(String imageId_00) {
		this.imageId_00 = imageId_00;
	}

	public String getImageDisplayWidth() {
		return imageDisplayWidth;
	}

	public void setImageDisplayWidth(String imageDisplayWidth) {
		this.imageDisplayWidth = imageDisplayWidth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((imageDisplayWidth == null) ? 0 : imageDisplayWidth.hashCode());
		result = prime * result + ((imageId_00 == null) ? 0 : imageId_00.hashCode());
		result = prime * result + ((imageId_01 == null) ? 0 : imageId_01.hashCode());
		result = prime * result + ((imageId_01_100 == null) ? 0 : imageId_01_100.hashCode());
		result = prime * result + ((namaPropinsi == null) ? 0 : namaPropinsi.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Propinsi other = (Propinsi) obj;
		if (imageDisplayWidth == null) {
			if (other.imageDisplayWidth != null)
				return false;
		} else if (!imageDisplayWidth.equals(other.imageDisplayWidth))
			return false;
		if (imageId_00 == null) {
			if (other.imageId_00 != null)
				return false;
		} else if (!imageId_00.equals(other.imageId_00))
			return false;
		if (imageId_01 == null) {
			if (other.imageId_01 != null)
				return false;
		} else if (!imageId_01.equals(other.imageId_01))
			return false;
		if (imageId_01_100 == null) {
			if (other.imageId_01_100 != null)
				return false;
		} else if (!imageId_01_100.equals(other.imageId_01_100))
			return false;
		if (namaPropinsi == null) {
			if (other.namaPropinsi != null)
				return false;
		} else if (!namaPropinsi.equals(other.namaPropinsi))
			return false;
		return true;
	}

}
