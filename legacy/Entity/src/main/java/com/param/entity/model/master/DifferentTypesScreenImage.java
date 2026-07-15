package com.param.entity.model.master;

import com.param.adt.admission.model.Admission;
import com.param.adt.master.global.model.OrganizationMaster;

import javax.persistence.*;
import java.util.Date;

@NamedQueries({
		@NamedQuery(name = "GET_IMAGE_BY_IMAGE_TYPE_ID",
				query = "SELECT differentTypesScreenImage.screenImageId as screenImageId, differentTypesScreenImage.image as image, differentTypesScreenImage.imageURL as imageURL, differentTypesScreenImage.imageTypeId as imageTypeId, differentTypesScreenImage.createdBy as createdBy, " +
						"differentTypesScreenImage.createdDate as createdDate, differentTypesScreenImage.status as status, differentTypesScreenImage.updatedBy as updatedBy, differentTypesScreenImage.updatedDate as updatedDate, differentTypesScreenImage.organizationId as organizationId " +
						"FROM DifferentTypesScreenImage differentTypesScreenImage " +
						"WHERE differentTypesScreenImage.imageTypeId = :imageTypeId "+
						"AND differentTypesScreenImage.organizationId = :organizationId " +
						"AND differentTypesScreenImage.status = :status "
						)
})

@Entity
@Table(name = "m_different_types_screen_image", schema = "public")
@SequenceGenerator(name = "seq_m_different_types_screen_image", sequenceName = "public.seq_m_different_types_screen_image", allocationSize = 1)
public class DifferentTypesScreenImage {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_different_types_screen_image")
	@Column(name = "screen_image_id")
	private Integer screenImageId;

	@Column(name = "image")
	private byte[] image;

	@Column(name = "image_url")
	private String imageURL;

	@Column(name = "image_type_id")
	private Integer imageTypeId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "status")
	private Character status;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "logo_name")
	private String logoName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, nullable = false, updatable = false)
	private OrganizationMaster organizationMaster;

	public Integer getScreenImageId() {
		return screenImageId;
	}

	public void setScreenImageId(Integer screenImageId) {
		this.screenImageId = screenImageId;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public Integer getImageTypeId() {
		return imageTypeId;
	}

	public void setImageTypeId(Integer imageTypeId) {
		this.imageTypeId = imageTypeId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}
}