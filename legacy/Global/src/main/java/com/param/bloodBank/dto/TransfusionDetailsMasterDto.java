package com.param.bloodBank.dto;
import java.util.Date;
import java.util.List;


public class TransfusionDetailsMasterDto {
	private Integer transfusionDetailId;
	private Integer patientId;
	private Integer unitId;
	private Integer organizationId;
	private Integer bloodBagComponenId;
	private Integer bloodGroupId;
	private String bloodBagNo;
	private String segmentNo;
	private Integer issueBloodBagStatusId;
    private Integer issueBloodBagDetailsId;
	private Integer issueBloodBagId;
    private String bloodComponentDesc;
	private String bloodGroupName;
	private Integer vSiteId;
	private Integer ivAccessId;
    private Double donorTemperature;
    private Double donorPulse;
    private Double donorBPSystolic;
	private Double donorBpDiastolic;
	private Integer donarRespirationRate;
	private String startTime;
	private String incubationStartTime;
	private Date startDate;
	private Double pulse;
	private Double temperature;
	private Double bpDiastolic;
	private Double bpSystolic;
	private Integer respirationRate;
	private Integer createdBy;
	private String createdDate;
	private Integer updatedBy;
	private String updatedDate;
	private Integer transfusionId;
	private Integer terminationId; 
	private String expiryDate;
	private Character saveAsDraftFlag;
	private Character status;    
	
	private List<IntraTransfusionMonitoringMapper> intraTransfusionMonitoringMapperlist;

	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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
	
	public Integer getBloodGroupId() {
		return bloodGroupId;
	}
	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}
	public String getSegmentNo() {
		return segmentNo;
	}
	public void setSegmentNo(String segmentNo) {
		this.segmentNo = segmentNo;
	}
	public Integer getIssueBloodBagStatusId() {
		return issueBloodBagStatusId;
	}
	public void setIssueBloodBagStatusId(Integer issueBloodBagStatusId) {
		this.issueBloodBagStatusId = issueBloodBagStatusId;
	}
	public Integer getIssueBloodBagDetailsId() {
		return issueBloodBagDetailsId;
	}
	public void setIssueBloodBagDetailsId(Integer issueBloodBagDetailsId) {
		this.issueBloodBagDetailsId = issueBloodBagDetailsId;
	}
	public Integer getIssueBloodBagId() {
		return issueBloodBagId;
	}
	public void setIssueBloodBagId(Integer issueBloodBagId) {
		this.issueBloodBagId = issueBloodBagId;
	}
	public String getBloodComponentDesc() {
		return bloodComponentDesc;
	}
	public void setBloodComponentDesc(String bloodComponentDesc) {
		this.bloodComponentDesc = bloodComponentDesc;
	}
	public Integer getvSiteId() {
		return vSiteId;
	}
	public void setvSiteId(Integer vSiteId) {
		this.vSiteId = vSiteId;
	}
	public Integer getIvAccessId() {
		return ivAccessId;
	}
	public void setIvAccessId(Integer ivAccessId) {
		this.ivAccessId = ivAccessId;
	}
	public Double getDonorTemperature() {
		return donorTemperature;
	}
	public void setDonorTemperature(Double donorTemperature) {
		this.donorTemperature = donorTemperature;
	}
	public Double getDonorPulse() {
		return donorPulse;
	}
	public void setDonorPulse(Double donorPulse) {
		this.donorPulse = donorPulse;
	}
	public Double getDonorBPSystolic() {
		return donorBPSystolic;
	}
	public void setDonorBPSystolic(Double donorBPSystolic) {
		this.donorBPSystolic = donorBPSystolic;
	}
	public Double getDonorBpDiastolic() {
		return donorBpDiastolic;
	}
	public void setDonorBpDiastolic(Double donorBpDiastolic) {
		this.donorBpDiastolic = donorBpDiastolic;
	}
	public Integer getDonarRespirationRate() {
		return donarRespirationRate;
	}
	public void setDonarRespirationRate(Integer donarRespirationRate) {
		this.donarRespirationRate = donarRespirationRate;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getTransfusionId() {
		return transfusionId;
	}
	public void setTransfusionId(Integer transfusionId) {
		this.transfusionId = transfusionId;
	}
	public Integer getTerminationId() {
		return terminationId;
	}
	public void setTerminationId(Integer terminationId) {
		this.terminationId = terminationId;
	}
	public Integer getTransfusionDetailId() {
		return transfusionDetailId;
	}
	public void setTransfusionDetailId(Integer transfusionDetailId) {
		this.transfusionDetailId = transfusionDetailId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public List<IntraTransfusionMonitoringMapper> getIntraTransfusionMonitoringMapperlist() {
		return intraTransfusionMonitoringMapperlist;
	}
	public String getBloodBagNo() {
		return bloodBagNo;
	}
	public void setBloodBagNo(String bloodBagNo) {
		this.bloodBagNo = bloodBagNo;
	}
	public void setIntraTransfusionMonitoringMapperlist(
			List<IntraTransfusionMonitoringMapper> intraTransfusionMonitoringMapperlist) {
		this.intraTransfusionMonitoringMapperlist = intraTransfusionMonitoringMapperlist;
	}
	public String getBloodGroupName() {
		return bloodGroupName;
	}
	public void setBloodGroupName(String bloodGroupName) {
		this.bloodGroupName = bloodGroupName;
	}

	public Integer getBloodBagComponenId() {
		return bloodBagComponenId;
	}
	public void setBloodBagComponenId(Integer bloodBagComponenId) {
		this.bloodBagComponenId = bloodBagComponenId;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Character getSaveAsDraftFlag() {
		return saveAsDraftFlag;
	}
	public void setSaveAsDraftFlag(Character saveAsDraftFlag) {
		this.saveAsDraftFlag = saveAsDraftFlag;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getIncubationStartTime() {
		return incubationStartTime;
	}
	public void setIncubationStartTime(String incubationStartTime) {
		this.incubationStartTime = incubationStartTime;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getBpDiastolic() {
		return bpDiastolic;
	}
	public void setBpDiastolic(Double bpDiastolic) {
		this.bpDiastolic = bpDiastolic;
	}
	public Double getBpSystolic() {
		return bpSystolic;
	}
	public void setBpSystolic(Double bpSystolic) {
		this.bpSystolic = bpSystolic;
	}
	public Integer getRespirationRate() {
		return respirationRate;
	}
	public void setRespirationRate(Integer respirationRate) {
		this.respirationRate = respirationRate;
	}
	public Double getPulse() {
		return pulse;
	}
	public void setPulse(Double pulse) {
		this.pulse = pulse;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}

}
