package com.param.global.model;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.billing.global.model.TPeriodicityServiceOrders;
import com.param.opd.model.ConsultDoctor;

@NamedQueries({
	
	@NamedQuery(name="GET_OUT_SIDE_HOSPITAL_MASTER_LIST",
		query="SELECT outsideHospitalMaster.outsideHospitalId AS outsideHospitalId, outsideHospitalMaster.outsideHospitalCode AS outsideHospitalCode, outsideHospitalMaster.outsideHospitalDesc AS outsideHospitalDesc,"
					+ " outsideHospitalMaster.organizationId AS organizationId, outsideHospitalMaster.unitId AS unitId, outsideHospitalMaster.status AS status"
					+ " FROM OutsideHospitalMaster outsideHospitalMaster"
					+ " WHERE  outsideHospitalMaster.organizationId= :organizationId "
					+ " ORDER BY outsideHospitalMaster.outsideHospitalId asc"
					),
	
	@NamedQuery(name="GET_OUT_SIDE_HOSPITAL_MASTER_BY_ID",
		query="SELECT outsideHospitalMaster.outsideHospitalId AS outsideHospitalId, outsideHospitalMaster.outsideHospitalCode AS outsideHospitalCode, outsideHospitalMaster.outsideHospitalDesc AS outsideHospitalDesc,"
					+ " outsideHospitalMaster.organizationId AS organizationId, outsideHospitalMaster.unitId AS unitId, outsideHospitalMaster.status AS status"
					+ " FROM OutsideHospitalMaster outsideHospitalMaster"
					+ " WHERE outsideHospitalMaster.outsideHospitalId= :outsideHospitalId"
					+ " AND outsideHospitalMaster.organizationId= :organizationId"
					+ " AND outsideHospitalMaster.unitId= :unitId"
					),
	
	@NamedQuery(name="GET_ACTIVE_OUT_SIDE_HOSPITAL_MASTER",
		query="SELECT outsideHospitalMaster.outsideHospitalId AS outsideHospitalId, outsideHospitalMaster.outsideHospitalCode AS outsideHospitalCode, outsideHospitalMaster.outsideHospitalDesc AS outsideHospitalDesc,"
					+ " outsideHospitalMaster.organizationId AS organizationId, outsideHospitalMaster.unitId AS unitId, outsideHospitalMaster.status AS status"
					+ " FROM OutsideHospitalMaster outsideHospitalMaster"
					+ " WHERE outsideHospitalMaster.status= 'A'"
					+ " AND outsideHospitalMaster.organizationId= :organizationId"
				    + " ORDER BY outsideHospitalMaster.outsideHospitalDesc ASC"
					),
	
	@NamedQuery(name="GET_OUT_SIDE_HOSPITAL_MASTER_BY_NAME",
		query="SELECT outsideHospitalMaster.outsideHospitalId AS outsideHospitalId "
					+ "	FROM OutsideHospitalMaster outsideHospitalMaster"
					+ " WHERE (LOWER(outsideHospitalMaster.outsideHospitalDesc)=:outsideHospitalDesc OR outsideHospitalMaster.outsideHospitalDesc=:outsideHospitalDesc) "
					+ " AND outsideHospitalMaster.unitId= :unitId"
					),
	
	@NamedQuery(name="GET_OUT_SIDE_HOSPITAL_MASTER_BY_NAME_NOT_ID",
		query="SELECT outsideHospitalMaster.outsideHospitalId AS outsideHospitalId"
					+ " FROM OutsideHospitalMaster outsideHospitalMaster"
					+ " WHERE (LOWER(outsideHospitalMaster.outsideHospitalDesc)=:outsideHospitalDesc OR outsideHospitalMaster.outsideHospitalDesc=:outsideHospitalDesc) "
					+ " AND outsideHospitalMaster.outsideHospitalId != :outsideHospitalId"
					+ " AND outsideHospitalMaster.unitId = :unitId"
					),
	})

@Entity
@Table(name = "m_outside_hospital_master", schema = "public")
@SequenceGenerator(name = "m_outside_hospital_master_seq", sequenceName = "public.m_outside_hospital_master_seq", allocationSize = 1)

public class OutsideHospitalMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_outside_hospital_master_seq")
	
	@Column(name = "outside_hospital_id")
	private Integer outsideHospitalId;
	
	@Column(name = "outside_hospital_code")
	private String outsideHospitalCode;
	
	@Column(name = "outside_hospital_desc")
	private String outsideHospitalDesc;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "outsideHospitalMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ConsultDoctor> outsideHospitalMasterList;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "outside_hospital_id", insertable = false, updatable = false)
	private OutsideHospitalDoctorMapper outsideHospitalDoctorMapper;
	
	public Integer getOutsideHospitalId() {
		return outsideHospitalId;
	}

	public void setOutsideHospitalId(Integer outsideHospitalId) {
		this.outsideHospitalId = outsideHospitalId;
	}

	public String getOutsideHospitalCode() {
		return outsideHospitalCode;
	}

	public void setOutsideHospitalCode(String outsideHospitalCode) {
		this.outsideHospitalCode = outsideHospitalCode;
	}

	public String getOutsideHospitalDesc() {
		return outsideHospitalDesc;
	}

	public void setOutsideHospitalDesc(String outsideHospitalDesc) {
		this.outsideHospitalDesc = outsideHospitalDesc;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
	
}
