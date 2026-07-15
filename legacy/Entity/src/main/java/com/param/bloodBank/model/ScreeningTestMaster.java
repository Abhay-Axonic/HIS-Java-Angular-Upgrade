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
	
	@NamedQuery(name="GET_SCREENING_TEST_MASTER_LIST",
		query=" SELECT screeningTestMaster.screeningTestId AS screeningTestId, screeningTestMaster.screeningTestCode AS screeningTestCode,"
				+ " screeningTestMaster.screeningTestDesc AS screeningTestDesc, screeningTestMaster.testId AS testId, screeningTestMaster.organizationId AS organizationId,"
				+ " screeningTestMaster.unitId AS unitId, screeningTestMaster.status AS status "
				+ " FROM ScreeningTestMaster screeningTestMaster"
				+ " WHERE screeningTestMaster.organizationId= :organizationId"
				+ " AND screeningTestMaster.unitId= :unitId"
				+ " ORDER BY screeningTestMaster.screeningTestId asc "
				),
	
	@NamedQuery(name="GET_SCREENING_TEST_MASTER_BY_ID",
		query=" SELECT screeningTestMaster.screeningTestId AS screeningTestId, screeningTestMaster.screeningTestCode AS screeningTestCode,"
				+ " screeningTestMaster.screeningTestDesc AS screeningTestDesc, screeningTestMaster.testId AS testId, screeningTestMaster.organizationId AS organizationId,"
				+ " screeningTestMaster.unitId AS unitId, screeningTestMaster.status AS status "
				+ " FROM ScreeningTestMaster screeningTestMaster"
				+ " WHERE screeningTestMaster.screeningTestId= :screeningTestId"
				+ " AND screeningTestMaster.organizationId= :organizationId"
				+ " AND screeningTestMaster.unitId= :unitId"
				),
	
	@NamedQuery(name="GET_ACTIVE_SCREENING_TEST_MASTER",
		query=" SELECT screeningTestMaster.screeningTestId AS screeningTestId, screeningTestMaster.screeningTestCode AS screeningTestCode,"
				+ " screeningTestMaster.screeningTestDesc AS screeningTestDesc, screeningTestMaster.testId AS testId, screeningTestMaster.organizationId AS organizationId,"
				+ " screeningTestMaster.unitId AS unitId, screeningTestMaster.status AS status "
				+ " FROM ScreeningTestMaster screeningTestMaster"
				+ " WHERE screeningTestMaster.status= 'A'"
				+ " AND screeningTestMaster.organizationId= :organizationId"
				+ " AND screeningTestMaster.unitId= :unitId"
				+ " ORDER BY screeningTestMaster.screeningTestId asc "
				),
	
	@NamedQuery(name="GET_SCREENING_TEST_MASTER_BY_NAME",
		query=" SELECT screeningTestMaster.screeningTestId AS screeningTestId "
				+ " FROM ScreeningTestMaster screeningTestMaster"
				+ " WHERE LOWER(screeningTestMaster.screeningTestDesc)= :screeningTestDesc"
				),
	
	@NamedQuery(name="GET_SCREENING_TEST_MASTER_BY_NAME_NOT_ID",
		query=" SELECT screeningTestMaster.screeningTestId AS screeningTestId "
				+ " FROM ScreeningTestMaster screeningTestMaster"
				+ " WHERE screeningTestMaster.screeningTestId!= :screeningTestId"
				+ " AND LOWER(screeningTestMaster.screeningTestDesc)= :screeningTestDesc"
				),
	
	
		
})
@Entity
@Table(name = "m_screening_test_master", schema = "blood_bank")
@SequenceGenerator(name = "seq_m_screening_test_master", sequenceName = "blood_bank.seq_m_screening_test_master", allocationSize = 1)
public class ScreeningTestMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_screening_test_master")
	
	@Column(name = "screening_test_id")
	private Integer screeningTestId;
	
	@Column(name = "screening_test_code")
	private String screeningTestCode;
	
	@Column(name = "screening_test_desc")
	private String screeningTestDesc;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "test_id")
	private Integer testId;
	
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

	public Integer getScreeningTestId() {
		return screeningTestId;
	}

	public void setScreeningTestId(Integer screeningTestId) {
		this.screeningTestId = screeningTestId;
	}

	public String getScreeningTestCode() {
		return screeningTestCode;
	}

	public void setScreeningTestCode(String screeningTestCode) {
		this.screeningTestCode = screeningTestCode;
	}

	public String getScreeningTestDesc() {
		return screeningTestDesc;
	}

	public void setScreeningTestDesc(String screeningTestDesc) {
		this.screeningTestDesc = screeningTestDesc;
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

	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}
}
