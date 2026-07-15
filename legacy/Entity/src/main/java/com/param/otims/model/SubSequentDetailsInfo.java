package com.param.otims.model;

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

import com.param.global.model.ServiceMaster;
@NamedQueries({
	
	@NamedQuery(name="GET_SUBSEQUENT_DETAILS_INFO_LIST_BY_DETAILS_ID",
			query="SELECT "
					+ " info.subSequentDetailsInfoId AS subSequentDetailsInfoId, "
					+ " info.subSequentDetailsId AS subSequentDetailsId,"
					+ " info.serviceId AS serviceId,"
					+ " info.discPercentage AS discPercentage, "
					+ " info.status AS status, "
					+ " info.createdBy AS createdBy, "
					+ " info.updatedBy AS updatedBy, "
					+ " to_char(info.createdDate, 'dd-MM-yyyy HH:MI:SS') AS createdDate, "
					+ " to_char(info.updatedDate,'dd-MM-yyyy HH:MI:SS') AS updatedDate, "
					+ " info.orgId AS orgId, "
					+ " serviceMaster.serviceStandardName AS serviceName, "
					+ " info.unitId AS unitId "
					+ " FROM SubSequentDetailsInfo info "
					+ " INNER JOIN info.serviceMaster serviceMaster "
					+ " WHERE info.unitId=:unitId "
					+ " AND info.orgId=:orgId "
					+ " AND info.subSequentDetailsId = :subSequentDetailsId "
					+ " AND info.status='A' "),
})
@Entity
@Table(name = "m_sub_sequent_details_info" , schema = "otims")
@SequenceGenerator(name = "m_sub_sequent_details_info_seq" , sequenceName = "otims.m_sub_sequent_details_info_seq" , allocationSize = 1)
public class SubSequentDetailsInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_sub_sequent_details_info_seq")
	@Column(name = "sub_sequent_details_info_id")
	private Integer subSequentDetailsInfoId;
	
	@Column(name = "sub_sequent_details_id")
	private Integer subSequentDetailsId;
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="disc_percentage")
	private Integer discPercentage;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", insertable = false, nullable = false, updatable = false)
	private ServiceMaster serviceMaster;

	public Integer getSubSequentDetailsInfoId() {
		return subSequentDetailsInfoId;
	}

	public void setSubSequentDetailsInfoId(Integer subSequentDetailsInfoId) {
		this.subSequentDetailsInfoId = subSequentDetailsInfoId;
	}

	public Integer getSubSequentDetailsId() {
		return subSequentDetailsId;
	}

	public void setSubSequentDetailsId(Integer subSequentDetailsId) {
		this.subSequentDetailsId = subSequentDetailsId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getDiscPercentage() {
		return discPercentage;
	}

	public void setDiscPercentage(Integer discPercentage) {
		this.discPercentage = discPercentage;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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
