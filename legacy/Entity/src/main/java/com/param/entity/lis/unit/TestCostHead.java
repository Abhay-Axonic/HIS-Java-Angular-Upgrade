package com.param.entity.lis.unit;

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
	
	@NamedQuery(name="GET_TEST_CODE_HEAD_LIST",
			query="SELECT tch.testCosHeadId as testCosHeadId,tch.testCostHeadCode as testCostHeadCode,"
					+ " tch.testCostHeadDesc as testCostHeadDesc,tch.status as status "
					+ " FROM TestCostHead tch "
					+ " WHERE tch.organizationId=:orgId "
					+ " order by tch.testCosHeadId  desc"),
	@NamedQuery(name="GET_TEST_CODE_HEAD_LIST_BY_ID",
			query="SELECT tch.testCosHeadId as testCosHeadId,tch.testCostHeadCode as testCostHeadCode,"
					+ " tch.testCostHeadDesc as testCostHeadDesc,tch.status as status "
					+ " FROM TestCostHead tch "
					+ " WHERE tch.organizationId=:orgId AND tch.testCosHeadId=:id"),
	@NamedQuery(name="GET_ACTIVE_TEST_CODE_HEAD_LIST",
	query="SELECT tch.testCosHeadId as testCosHeadId,tch.testCostHeadCode as testCostHeadCode,"
			+ " tch.testCostHeadDesc as testCostHeadDesc,tch.status as status "
			+ " FROM TestCostHead tch "
			+ " WHERE tch.organizationId=:orgId AND tch.status='A' "),
	
	@NamedQuery(name="GET_TEST_COST_HEAD_BY_CODE",
	query="SELECT tch.testCosHeadId as testCosHeadId,tch.testCostHeadCode as testCostHeadCode,"
			+ " tch.testCostHeadDesc as testCostHeadDesc,tch.status as status "
			+ " FROM TestCostHead tch "
			+ " WHERE tch.organizationId=:organizationId "
			+ " AND lower(tch.testCostHeadCode) = lower(:testCostHeadCode) "),
	
	@NamedQuery(name="GET_TEST_COST_HEAD_BY_DESCRIPTION",
	query="SELECT tch.testCosHeadId as testCosHeadId,tch.testCostHeadCode as testCostHeadCode,"
			+ " tch.testCostHeadDesc as testCostHeadDesc,tch.status as status "
			+ " FROM TestCostHead tch "
			+ " WHERE tch.organizationId=:organizationId "
			+ " AND lower(tch.testCostHeadDesc) = lower(:testCostHeadDesc) "),
	
	@NamedQuery(name="GET_UPDATE_TEST_COST_HEAD_BY_CODE",
	query="SELECT tch.testCosHeadId as testCosHeadId,tch.testCostHeadCode as testCostHeadCode,"
			+ " tch.testCostHeadDesc as testCostHeadDesc,tch.status as status "
			+ " FROM TestCostHead tch "
			+ " WHERE tch.organizationId=:organizationId "
			+ " AND lower(tch.testCostHeadCode) = lower(:testCostHeadCode) "
			+ " AND tch.testCosHeadId <> :testCosHeadId"),
	
	@NamedQuery(name="GET_UPDATE_TEST_COST_HEAD_BY_DESCRIPTION",
	query="SELECT tch.testCosHeadId as testCosHeadId,tch.testCostHeadCode as testCostHeadCode,"
			+ " tch.testCostHeadDesc as testCostHeadDesc,tch.status as status "
			+ " FROM TestCostHead tch "
			+ " WHERE tch.organizationId=:organizationId "
			+ " AND lower(tch.testCostHeadDesc) = lower(:testCostHeadDesc) "
			+ " AND tch.testCosHeadId <> :testCosHeadId"),
})

@Entity
@Table(name="m_test_cost_head",schema="lab")
@SequenceGenerator(name="test_cost_head_seq",sequenceName="lab.test_cost_head_seq",allocationSize=1)
public class TestCostHead {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "test_cost_head_seq")
	@Column(name="test_cost_head_id")
	private Integer testCosHeadId;
	
	@Column(name = "test_cost_head_code")
	private String testCostHeadCode;
	
	@Column(name = "test_cost_head_desc")
	private String testCostHeadDesc;
	

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

//	@Column(name = "unit_id")
//	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;

	public Integer getTestCosHeadId() {
		return testCosHeadId;
	}

	public void setTestCosHeadId(Integer testCosHeadId) {
		this.testCosHeadId = testCosHeadId;
	}

	public String getTestCostHeadCode() {
		return testCostHeadCode;
	}

	public void setTestCostHeadCode(String testCostHeadCode) {
		this.testCostHeadCode = testCostHeadCode;
	}

	public String getTestCostHeadDesc() {
		return testCostHeadDesc;
	}

	public void setTestCostHeadDesc(String testCostHeadDesc) {
		this.testCostHeadDesc = testCostHeadDesc;
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

	/*public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}*/

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	
}
