package com.param.adt.admission.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.OrganizationMaster;
import com.param.adt.master.global.model.UnitMaster;

@Entity
@Table(name="t_medico_legal",schema="adt")
@SequenceGenerator(name="medico_legal_seq",sequenceName="adt.medico_legal_seq",allocationSize=1)
public class MedicoLeagal 
{
	
	@Id
	@Column(name="medico_legal_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="medico_legal_seq")
	private int medicoLegalId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="priority_id")
	private Integer priorityId;
	
	@Column(name="accompanied_by")
	private String accompaniedBy;
	
	@Column(name="fir_no")
	private String firNo;
	
	@Column(name="police_buckle_no")
	private String policeBuckleNo;
	
	@Column(name="police_station_name")
	private String policeStationName;
	
	@Column(name="mlc_contact_no")
	private String mlcContactNo;
	
	@Column(name="mlc_relation_id")
	private Integer mlcRelationId;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, nullable = false, updatable = false)
	private OrganizationMaster organizationMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, nullable = false, updatable = false)
	private UnitMaster unitMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id", insertable = false, nullable = false, updatable = false)
	private Admission admission;
	
	@Column(name = "mlc_no")
	private String mlcNo;
	
	@Column(name = "modeof_arrival")
	private String modeOfArrival;
	
/*	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "priority_id", insertable = false, nullable = false, updatable = false)
	private PriorityMaster priorityMaster;*/

	public String getMlcNo() {
		return mlcNo;
	}

	public void setMlcNo(String mlcNo) {
		this.mlcNo = mlcNo;
	}

	public int getMedicoLegalId() {
		return medicoLegalId;
	}

	public void setMedicoLegalId(int medicoLegalId) {
		this.medicoLegalId = medicoLegalId;
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getAccompaniedBy() {
		return accompaniedBy;
	}

	public void setAccompaniedBy(String accompaniedBy) {
		this.accompaniedBy = accompaniedBy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}


	public String getPoliceBuckleNo() {
		return policeBuckleNo;
	}

	public String getPoliceStationName() {
		return policeStationName;
	}

	public String getMlcContactNo() {
		return mlcContactNo;
	}

	public Integer getMlcRelationId() {
		return mlcRelationId;
	}


	public void setPoliceBuckleNo(String policeBuckleNo) {
		this.policeBuckleNo = policeBuckleNo;
	}

	public void setPoliceStationName(String policeStationName) {
		this.policeStationName = policeStationName;
	}

	public void setMlcContactNo(String mlcContactNo) {
		this.mlcContactNo = mlcContactNo;
	}

	public void setMlcRelationId(Integer mlcRelationId) {
		this.mlcRelationId = mlcRelationId;
	}

	public String getFirNo() {
		return firNo;
	}

	public void setFirNo(String firNo) {
		this.firNo = firNo;
	}

	public String getModeOfArrival() {
		return modeOfArrival;
	}

	public void setModeOfArrival(String modeOfArrival) {
		this.modeOfArrival = modeOfArrival;
	}
		
}
