package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQuery(name="GET_REPORT_GROUP_HEADER_MASTER_LIST",
query="SELECT rghm.reportGroupHeaderId as reportGroupHeaderId , rghm.reportGroupHeaderName as reportGroupHeaderName , rghm.organizationId as organizationId "
		+ " from ReportGroupHeaderMaster rghm " 
		+ " WHERE rghm.organizationId= :organizationId "
		+ " order by rghm.reportGroupHeaderId asc"
)
@Entity
@Table(name = "m_reportgroupheader_master", schema = "public")
@SequenceGenerator(name="m_reportgroupheader_master_seq", sequenceName="public.m_reportgroupheader_master_seq", allocationSize=1)
public class ReportGroupHeaderMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_reportgroupheader_master_seq")
	@Column(name = "reportgroupheader_id")
	private int reportGroupHeaderId;
	
	@Column(name = "reportgroupheader_name")
	private String reportGroupHeaderName;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "reportGroupHeaderMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReportMasterDetails> listReportMasterDetails;*/

	public int getReportGroupHeaderId() {
		return reportGroupHeaderId;
	}

	public void setReportGroupHeaderId(int reportGroupHeaderId) {
		this.reportGroupHeaderId = reportGroupHeaderId;
	}

	public String getReportGroupHeaderName() {
		return reportGroupHeaderName;
	}

	public void setReportGroupHeaderName(String reportGroupHeaderName) {
		this.reportGroupHeaderName = reportGroupHeaderName;
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


	
}
