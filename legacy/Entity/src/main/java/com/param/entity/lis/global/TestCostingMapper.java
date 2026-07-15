package com.param.entity.lis.global;

import java.util.Date;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.lis.unit.TestCostHead;
@NamedQueries({
	@NamedQuery(name="GET_TEST_HEAD_COST_BY_COSTING_MSTR_ID",
			query="select mpper.testCostingMapperId as testCostingMapperId ,mpper.testCostHeadId as testCostHeadId,mpper.cost as cost "
					+ " from TestCostingMapper mpper "
					+ " where mpper.status='A' and mpper.testCostingMasterId=:testCostingId")
})
@Entity
@SequenceGenerator(name="m_test_costing_mapper_seq",sequenceName="lab.m_test_costing_mapper_seq",allocationSize=1)
@Table(name="m_test_costing_mapper",schema="lab")
public class TestCostingMapper {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "m_test_costing_mapper_seq")
	@Column(name="test_costing_mapper_id")
	private Integer testCostingMapperId;
	
	@Column(name="test_costing_master_id")
	private Integer testCostingMasterId;
	
	@Column(name="test_cost_head_id")
	private Integer testCostHeadId;
	
	@Column(name="cost")
	private Double cost;
	
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

	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_cost_head_id" , insertable = false , updatable = false , nullable = false)
	private TestCostHead testCostHead;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_costing_master_id" , insertable = false , updatable = false , nullable = false)
	private TestCostingMaster testCostingMaster;

	public Integer getTestCostingMapperId() {
		return testCostingMapperId;
	}

	public void setTestCostingMapperId(Integer testCostingMapperId) {
		this.testCostingMapperId = testCostingMapperId;
	}

	public Integer getTestCostingMasterId() {
		return testCostingMasterId;
	}

	public void setTestCostingMasterId(Integer testCostingMasterId) {
		this.testCostingMasterId = testCostingMasterId;
	}

	public Integer getTestCostHeadId() {
		return testCostHeadId;
	}

	public void setTestCostHeadId(Integer testCostHeadId) {
		this.testCostHeadId = testCostHeadId;
	}
	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public TestCostHead getTestCostHead() {
		return testCostHead;
	}

	public void setTestCostHead(TestCostHead testCostHead) {
		this.testCostHead = testCostHead;
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
