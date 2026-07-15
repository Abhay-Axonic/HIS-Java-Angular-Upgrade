package com.param.entity.lis.transaction;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.adt.master.global.model.UserMaster;
import com.param.entity.lis.common.LocalTimeConverter;
import com.param.entity.lis.unit.TestMaster;
import com.param.entity.model.master.OrderDetail;

@Entity
@Table(name = "t_sample_audit_trail", schema = "lab")
public class SampleAuditTrailMaster {

	@Id
	@Column(name = "audi_trail_id")
	private int tSampleAuditTrailPkey;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "order_details_id")
	private Integer orderDetailsId;

	@Column(name = "test_id")
	private Integer testId;

	@Column(name = "sample_dtls_id")
	private Integer labSampleDtlsId;
	
	@Column(name = "assign_unit")
	private Integer assignUnit;
	
	@Column(name = "performed_unit")
	private Integer performedUnit;

	@Column(name = "test_status_id")
	private Integer testStatusId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	@Convert(converter = LocalTimeConverter.class)
	private Long createdDate;
	
	@Column(name = "created_date",insertable = false, nullable = false, updatable = false)
//	@Convert(converter = LocalTimeConverter.class)
	private Date createdDateNew;

	@ManyToOne
	@JoinColumn(name = "order_details_id", insertable = false, nullable = false, updatable = false)
	private OrderDetail orderDetail;

	@ManyToOne
	@JoinColumn(name = "test_id", insertable = false, nullable = false, updatable = false)
	private TestMaster testMaster;

	@ManyToOne
	@JoinColumn(name = "test_status_id", insertable = false, nullable = false, updatable = false)
	private SampleStatusMaster sampleStatusMaster;

	@ManyToOne
	@JoinColumn(name = "created_by", insertable = false, nullable = false, updatable = false)
	private UserMaster user;

	public int gettSampleAuditTrailPkey() {
		return tSampleAuditTrailPkey;
	}

	public void settSampleAuditTrailPkey(int tSampleAuditTrailPkey) {
		this.tSampleAuditTrailPkey = tSampleAuditTrailPkey;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public Integer getTestStatusId() {
		return testStatusId;
	}

	public void setTestStatusId(Integer testStatusId) {
		this.testStatusId = testStatusId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public OrderDetail getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(OrderDetail orderDetail) {
		this.orderDetail = orderDetail;
	}

	public TestMaster getTestMaster() {
		return testMaster;
	}

	public void setTestMaster(TestMaster testMaster) {
		this.testMaster = testMaster;
	}

	public SampleStatusMaster getSampleStatusMaster() {
		return sampleStatusMaster;
	}

	public void setSampleStatusMaster(SampleStatusMaster sampleStatusMaster) {
		this.sampleStatusMaster = sampleStatusMaster;
	}

	public UserMaster getUser() {
		return user;
	}

	public void setUser(UserMaster user) {
		this.user = user;
	}

	public Integer getPerformedUnit() {
		return performedUnit;
	}

	public void setPerformedUnit(Integer performedUnit) {
		this.performedUnit = performedUnit;
	}

	public Integer getAssignUnit() {
		return assignUnit;
	}

	public void setAssignUnit(Integer assignUnit) {
		this.assignUnit = assignUnit;
	}

	public Date getCreatedDateNew() {
		return createdDateNew;
	}

	public void setCreatedDateNew(Date createdDateNew) {
		this.createdDateNew = createdDateNew;
	}

}
