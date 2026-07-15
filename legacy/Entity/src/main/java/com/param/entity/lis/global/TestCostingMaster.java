package com.param.entity.lis.global;

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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.ServiceMaster;
import com.param.global.model.SpecialityMaster;
import com.param.global.model.SubSpecialityMaster;
@NamedQueries({
	@NamedQuery(name="GET_TEST_HEAD_COSTING_BY_ID",
			query="SELECT costMstr.testCostingMasterId as testCostingMasterId ,costMstr.specialityId as specialityId,costMstr.subSpecialityId as subSpecialityId,"
					+ " costMstr.serviceId as serviceId,spMstr.specialityName as specialityName,subSpMstr.subSpecialityMasterName as subSpecialityMasterName, srMstr.serviceLongDescripton as serviceLongDescripton "
					+ " FROM TestCostingMaster costMstr INNER JOIN costMstr.specialityMaster spMstr "
					+ " INNER JOIN costMstr.subSpecialityMaster subSpMstr "
					+ " INNER JOIN costMstr.serviceMaster srMstr "
//					+ " INNER JOIN costMstr.listTestCostingMapper lstMpper "
					+ " WHERE costMstr.testCostingMasterId=:testCostingId "
					+ " AND costMstr.unitId=:unitId "
					+ " AND costMstr.organizationId=:orgId"),
			@NamedQuery(name="GET_TEST_HEAD_COSTING_LIST",
			query="SELECT costMstr.testCostingMasterId as testCostingMasterId ,costMstr.specialityId as specialityId,costMstr.subSpecialityId as subSpecialityId,"
					+ " costMstr.serviceId as serviceId,spMstr.specialityName as specialityName,subSpMstr.subSpecialityMasterName as subSpecialityMasterName, srMstr.serviceLongDescripton as serviceLongDescripton,costMstr.status as status "
					+ " FROM TestCostingMaster costMstr "
					+ " INNER JOIN costMstr.specialityMaster spMstr "
					+ " INNER JOIN costMstr.subSpecialityMaster subSpMstr "
					+ " INNER JOIN costMstr.serviceMaster srMstr "
					+ " WHERE costMstr.organizationId=:orgId "
					+ " AND costMstr.unitId=:unitId")
	
})


@Entity
@Table(name="m_test_costing_master",schema="lab")
@SequenceGenerator(name="m_test_costing_master_seq",sequenceName="lab.m_test_costing_master_seq",allocationSize=1)
public class TestCostingMaster {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "m_test_costing_master_seq")
	@Column(name="test_costing_master_id")
	private Integer testCostingMasterId;
	
	@Column(name="speciality_id")
	private Integer specialityId;
	
	@Column(name="sub_speciality_id")
	private Integer subSpecialityId;
	
	@Column(name="service_id")
	private Integer serviceId;
	
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
	@JoinColumn(name = "speciality_id" , insertable = false , updatable = false , nullable = false)
	private SpecialityMaster specialityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_speciality_id" , insertable = false , updatable = false , nullable = false)
	private SubSpecialityMaster subSpecialityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id" , insertable = false , updatable = false , nullable = false)
	private ServiceMaster serviceMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "testCostingMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TestCostingMapper> listTestCostingMapper;

	public Integer getTestCostingMasterId() {
		return testCostingMasterId;
	}

	public void setTestCostingMasterId(Integer testCostingMasterId) {
		this.testCostingMasterId = testCostingMasterId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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
