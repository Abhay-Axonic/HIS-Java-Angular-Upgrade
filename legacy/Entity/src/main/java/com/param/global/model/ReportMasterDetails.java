package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	
	@NamedQuery(name="GET_REPORT_DETAILS_LIST",
		query="SELECT  distinct(rd.reportId) as reportId , rm.reportName as reportName,  rm.action as action , rm.reportGroupHeaderId as reportGroupHeaderId"
				+ " from ReportMasterDetails rd " 
				+ " INNER JOIN rd.reportMaster rm"
				+ " WHERE rd.organizationId= :organizationId "
				+ " AND rm.reportGroupHeaderId= :reportGroupHeaderId"
				+ " order by  rm.reportName asc"

			
					),
	
	
		
	
	@NamedQuery(name="GET_REPORT_DETAILS_BY_REPORT_ID",
	query="SELECT  rd.reportId as reportId , "
			+ "rm.reportName as reportName, "
			+ "rm.action as action, "
			+ "rd.lable as lable, "
			+ "rd.paramType as paramType, "
			+ "rd.parameterName as parameterName, "
			+ "rd.parameterId as parameterId , "
			+ "rd.controlName as controlName, "
			+ " rd.viewName as viewName  "
			+ " from ReportMasterDetails rd " 
			+ " INNER JOIN rd.reportMaster rm"
			+ " WHERE  rd.organizationId= :organizationId "
//			+ " AND rd.unitId= :unitId"
			+ " AND rd.reportId= :reportId"
			+ " order by rd.parameterId asc")
})

@Entity
@Table(name = "m_reports_details", schema = "public")
@SequenceGenerator(name="m_reports_details_seq", sequenceName="public.m_reports_details_seq", allocationSize=1)
public class ReportMasterDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_reports_details_seq")
	@Column(name = "report_detail_id")
	private int reportDetailId;
	
	@Column(name = "report_id")
	private Integer reportId;
	
	@Column(name = "parameter_name")
	private String parameterName;
	
	@Column(name = "parameter_id")
	private Integer parameterId;
	
	@Column(name = "control_name")
	private String controlName;
	
	@Column(name = "paramtype")
	private String paramType;
	
	@Column(name = "view_name")
	private String viewName;
	
	@Column(name = "is_compulsory")
	private String isCompulsory;
	
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
	
	@Column(name = "lable")
	private String lable;
	


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="report_id", insertable=false, updatable=false)
	private ReportMaster reportMaster;
	
	
	public int getReportDetailId() {
		return reportDetailId;
	}

	public void setReportDetailId(int reportDetailId) {
		this.reportDetailId = reportDetailId;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
 
	

	public Integer getParameterId() {
		return parameterId;
	}

	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}

	public ReportMaster getReportMaster() {
		return reportMaster;
	}

	public void setReportMaster(ReportMaster reportMaster) {
		this.reportMaster = reportMaster;
	}

	public String getControlName() {
		return controlName;
	}

	public void setControlName(String controlName) {
		this.controlName = controlName;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getIsCompulsory() {
		return isCompulsory;
	}

	public void setIsCompulsory(String isCompulsory) {
		this.isCompulsory = isCompulsory;
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

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	

	
	
}
