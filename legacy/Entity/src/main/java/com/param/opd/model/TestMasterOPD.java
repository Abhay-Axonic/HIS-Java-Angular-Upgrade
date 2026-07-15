package com.param.opd.model;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.ServiceMaster;
import com.param.global.model.SubSpecialityMaster;

/*Test Master*/


@Entity
@Table(name = "m_test_master", schema = "lab")
@SequenceGenerator(name = "m_seq_test_master", sequenceName = "lab.m_seq_test_master", allocationSize = 1)
public class TestMasterOPD {
	@Id
	@Column(name = "test_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_test_master")
	private int testId;

	@Column(name = "test_code")
	private String testCode;

	@Column(name = "test_desc")
	private String testDesc;

	@Column(name = "test_alies")
	private String testAlies;

	@Column(name = "dept_id")
	private Integer deptId;

	@Column(name = "sample_id")
	private Integer sampleId;

	@Column(name = "no_of_sample_req")
	private Integer noOfSampleReq;

	@Column(name = "sample_volume")
	private Double sampleVolume;

	@Column(name = "container_id")
	private Integer containerId;

	@Column(name = "report_type_id")
	private Integer reportTypeId;

	@Column(name = "technique_id")
	private Integer techniqueId;

	@Column(name = "identification_no")
	private String identificationNo;

	@Column(name = "prerequisits_id")
	private Integer prerequisitsId;

	@Column(name = "pro_release")
	private Character proRelease;

	@Column(name = "test_status")
	private Character testStatus;

	@Column(name = "is_outsourced")
	private Character isOutsourced;

	@Column(name = "foot_note")
	private String footNote;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Long createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Long updatedDate;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "norml_tat_time")
	private Integer normlTatTime;

	@Column(name = "norml_tat_id")
	private Integer normlTatId;

	@Column(name = "urgent_tat_time")
	private Integer urgentTatTime;

	@Column(name = "urgent_tat_id")
	private Integer urgentTatId;

	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "test_type")
	private Integer testType;

	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "is_centrifugation_requried")
	private Character isCentrifugationRequried;
	
	@Column(name = "is_histo_cyto")
	private Character isHistoCyto;
	
	@Column(name = "speciman_id")
	private Integer specimanId;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_id", insertable = false, nullable = false, updatable = false)
	private SubSpecialityMaster subSpecialityMaster;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", insertable = false, nullable = false, updatable = false)
    private ServiceMaster serviceMaster;

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public String getTestCode() {
		return testCode;
	}

	public void setTestCode(String testCode) {
		this.testCode = testCode;
	}

	public String getTestDesc() {
		return testDesc;
	}

	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}

	public String getTestAlies() {
		return testAlies;
	}

	public void setTestAlies(String testAlies) {
		this.testAlies = testAlies;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getSampleId() {
		return sampleId;
	}

	public void setSampleId(Integer sampleId) {
		this.sampleId = sampleId;
	}

	public Integer getNoOfSampleReq() {
		return noOfSampleReq;
	}

	public void setNoOfSampleReq(Integer noOfSampleReq) {
		this.noOfSampleReq = noOfSampleReq;
	}

	public Double getSampleVolume() {
		return sampleVolume;
	}

	public void setSampleVolume(Double sampleVolume) {
		this.sampleVolume = sampleVolume;
	}

	public Integer getContainerId() {
		return containerId;
	}

	public void setContainerId(Integer containerId) {
		this.containerId = containerId;
	}

	public Integer getReportTypeId() {
		return reportTypeId;
	}

	public void setReportTypeId(Integer reportTypeId) {
		this.reportTypeId = reportTypeId;
	}

	public Integer getTechniqueId() {
		return techniqueId;
	}

	public void setTechniqueId(Integer techniqueId) {
		this.techniqueId = techniqueId;
	}

	public String getIdentificationNo() {
		return identificationNo;
	}

	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	public Integer getPrerequisitsId() {
		return prerequisitsId;
	}

	public void setPrerequisitsId(Integer prerequisitsId) {
		this.prerequisitsId = prerequisitsId;
	}

	public Character getProRelease() {
		return proRelease;
	}

	public void setProRelease(Character proRelease) {
		this.proRelease = proRelease;
	}

	public Character getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(Character testStatus) {
		this.testStatus = testStatus;
	}

	public Character getIsOutsourced() {
		return isOutsourced;
	}

	public void setIsOutsourced(Character isOutsourced) {
		this.isOutsourced = isOutsourced;
	}

	public String getFootNote() {
		return footNote;
	}

	public void setFootNote(String footNote) {
		this.footNote = footNote;
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
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

	public Integer getNormlTatTime() {
		return normlTatTime;
	}

	public void setNormlTatTime(Integer normlTatTime) {
		this.normlTatTime = normlTatTime;
	}

	public Integer getNormlTatId() {
		return normlTatId;
	}

	public void setNormlTatId(Integer normlTatId) {
		this.normlTatId = normlTatId;
	}

	public Integer getUrgentTatTime() {
		return urgentTatTime;
	}

	public void setUrgentTatTime(Integer urgentTatTime) {
		this.urgentTatTime = urgentTatTime;
	}

	public Integer getUrgentTatId() {
		return urgentTatId;
	}

	public void setUrgentTatId(Integer urgentTatId) {
		this.urgentTatId = urgentTatId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getTestType() {
		return testType;
	}

	public void setTestType(Integer testType) {
		this.testType = testType;
	}


	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public SubSpecialityMaster getSubSpecialityMaster() {
		return subSpecialityMaster;
	}

	public void setSubSpecialityMaster(SubSpecialityMaster subSpecialityMaster) {
		this.subSpecialityMaster = subSpecialityMaster;
	}

	public Character getIsCentrifugationRequried() {
		return isCentrifugationRequried;
	}

	public void setIsCentrifugationRequried(Character isCentrifugationRequried) {
		this.isCentrifugationRequried = isCentrifugationRequried;
	}

	public Character getIsHistoCyto() {
		return isHistoCyto;
	}

	public void setIsHistoCyto(Character isHistoCyto) {
		this.isHistoCyto = isHistoCyto;
	}

	public Integer getSpecimanId() {
		return specimanId;
	}

	public void setSpecimanId(Integer specimanId) {
		this.specimanId = specimanId;
	}

	  public ServiceMaster getServiceMaster() {
	    return serviceMaster;
	  }
	
	  public void setServiceMaster(ServiceMaster serviceMaster) {
	    this.serviceMaster = serviceMaster;
	  }
	
	
	
}
