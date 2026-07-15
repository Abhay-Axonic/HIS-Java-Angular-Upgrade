package com.param.bloodBank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	
	@NamedQuery(name="GET_BLOOD_TEST_MASTER_LIST",
		query=" SELECT bloodTestMaster.bloodTestId AS bloodTestId, bloodTestMaster.bloodTestCode AS bloodTestCode,"
				+ " bloodTestMaster.bloodTestDesc AS bloodTestDesc,  bloodTestMaster.organizationId AS organizationId,"
				+ " bloodTestMaster.unitId AS unitId, bloodTestMaster.status AS status "
				+ " FROM BloodTestMaster bloodTestMaster"
				+ " WHERE bloodTestMaster.organizationId= :organizationId"
				+ " AND bloodTestMaster.unitId= :unitId"
				+ " ORDER BY bloodTestMaster.bloodTestId asc "
				),
	
	@NamedQuery(name="GET_BLOOD_TEST_MASTER_BY_ID",
		query=" SELECT bloodTestMaster.bloodTestId AS bloodTestId, bloodTestMaster.bloodTestCode AS bloodTestCode,"
				+ " bloodTestMaster.bloodTestDesc AS bloodTestDesc,  bloodTestMaster.organizationId AS organizationId,"
				+ " bloodTestMaster.unitId AS unitId, bloodTestMaster.status AS status "
				+ " FROM BloodTestMaster bloodTestMaster"
				+ " WHERE bloodTestMaster.bloodTestId= :bloodTestId"
				+ " AND bloodTestMaster.organizationId= :organizationId"
				+ " AND bloodTestMaster.unitId= :unitId"
				),
	
	@NamedQuery(name="GET_ACTIVE_BLOOD_TEST_MASTER",
		query=" SELECT bloodTestMaster.bloodTestId AS bloodTestId, bloodTestMaster.bloodTestCode AS bloodTestCode,"
				+ " bloodTestMaster.bloodTestDesc AS bloodTestDesc,  bloodTestMaster.organizationId AS organizationId,"
				+ " bloodTestMaster.unitId AS unitId, bloodTestMaster.status AS status "
				+ " FROM BloodTestMaster bloodTestMaster"
				+ " WHERE bloodTestMaster.status= 'A'"
				+ " AND bloodTestMaster.organizationId= :organizationId"
				+ " AND bloodTestMaster.unitId= :unitId"
				+ " ORDER BY bloodTestMaster.bloodTestId asc "
				),
	
	@NamedQuery(name="GET_BLOOD_TEST_MASTER_BY_NAME",
		query=" SELECT bloodTestMaster.bloodTestId AS bloodTestId "
				+ " FROM BloodTestMaster bloodTestMaster"
				+ " WHERE LOWER(bloodTestMaster.bloodTestDesc)= :bloodTestDesc"
				),
	
	@NamedQuery(name="GET_BLOOD_TEST_MASTER_BY_NAME_NOT_ID",
		query=" SELECT bloodTestMaster.bloodTestId AS bloodTestId "
				+ " FROM BloodTestMaster bloodTestMaster"
				+ " WHERE bloodTestMaster.bloodTestId!= :bloodTestId"
				+ " AND LOWER(bloodTestMaster.bloodTestDesc)= :bloodTestDesc"
				),
	
	
		
})
@Entity
@Table(name = "m_blood_test_master", schema = "blood_bank")
@SequenceGenerator(name = "seq_m_blood_test_master", sequenceName = "blood_bank.seq_m_blood_test_master", allocationSize = 1)
public class BloodTestMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_blood_test_master")
	
	@Column(name = "blood_test_id")
	private Integer bloodTestId;
	
	@Column(name = "blood_test_code")
	private String bloodTestCode;
	
	@Column(name = "blood_test_desc")
	private String bloodTestDesc;
	
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
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;

	public Integer getBloodTestId() {
		return bloodTestId;
	}

	public void setBloodTestId(Integer bloodTestId) {
		this.bloodTestId = bloodTestId;
	}

	public String getBloodTestCode() {
		return bloodTestCode;
	}

	public void setBloodTestCode(String bloodTestCode) {
		this.bloodTestCode = bloodTestCode;
	}

	public String getBloodTestDesc() {
		return bloodTestDesc;
	}

	public void setBloodTestDesc(String bloodTestDesc) {
		this.bloodTestDesc = bloodTestDesc;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	
	
}



