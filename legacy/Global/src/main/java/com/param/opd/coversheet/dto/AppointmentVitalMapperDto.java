package com.param.opd.coversheet.dto;

import java.util.Date;
import java.util.List;


public class AppointmentVitalMapperDto {

	private Integer appointmentVitalMapperId;
	private Integer vitalId;
	private Integer appointmentId;
	private Integer patientId;
	private String value;
	private char status;
	private char isSelfTriage;
	private Integer createdBy;
	private String createdDate;
	private Date createdDateNew;
	private Integer updatedBy;
	private String updatedDate;
	private Date updatedDateNew;
	private Integer unitId;
	private Integer organizationId;
	private String vitalName;
	private String fromDate;
	private Date dateFrom;
	private Date dateTo;
	private String localUhid;
	private String toDate;
	private Integer encounterId;
	private Integer admissionId;
	private Integer roleId;
	private char isReviewedFlag;
	private char isAdmissionFlag;
	private String unit;
	private Integer vitalOrderOpd;
	private String unitDesc;
	private String userName;
	private String createdTime;
	private Date createdTimeNew;
	private String updatedTime;
	private Date updatedTimeNew;
	private String doctorName;
	private String employeeName;
	private List<Integer> listOfSelectedVitals;
	private List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto;
	private char flag;
	private Integer reasonId;
	private Character isEnteredInError;
	private List<ListAppointmentVitalMapperDto> listAppointmentVitalMapperDtos;
	private String reasonDesc;
	private char isPostFacto;
	private String postFactoTime;
	private String postFactoDate;
	private String creatDate;
	private Integer covTemplateId;
	private AppointmentVitalMapperDto listOfSelectedQualifiers;
//	private Integer vitalQualifierId;
//	private List<AppointmentVitalMapperDto> vitalMethodList;
	private Integer vitalQualifireId;
	private String vitalQualifireDesc;
	private String vitalCategoryDesc;
	private Integer dialysisEventId;
	private Integer patientDialysisRecId;
    private Boolean isIcu;

	private String sortedDateTime;

	private Integer hospitalId;
	private String uuid;

	public List<AppointmentVitalMapperDto> getListAppointmentVitalMapperDto() {
		return listAppointmentVitalMapperDto;
	}
	public void setListAppointmentVitalMapperDto(List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto) {
		this.listAppointmentVitalMapperDto = listAppointmentVitalMapperDto;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public char getIsReviewedFlag() {
		return isReviewedFlag;
	}
	public void setIsReviewedFlag(char isReviewedFlag) {
		this.isReviewedFlag = isReviewedFlag;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getVitalName() {
		return vitalName;
	}
	public void setVitalName(String vitalName) {
		this.vitalName = vitalName;
	}
	public List<ListAppointmentVitalMapperDto> getListAppointmentVitalMapperDtos() {
		return listAppointmentVitalMapperDtos;
	}
	public void setListAppointmentVitalMapperDtos(
			List<ListAppointmentVitalMapperDto> listAppointmentVitalMapperDtos) {
		this.listAppointmentVitalMapperDtos = listAppointmentVitalMapperDtos;
	}
	public Integer getAppointmentVitalMapperId() {
		return appointmentVitalMapperId;
	}
	public void setAppointmentVitalMapperId(Integer appointmentVitalMapperId) {
		this.appointmentVitalMapperId = appointmentVitalMapperId;
	}
	public Integer getVitalId() {
		return vitalId;
	}
	public void setVitalId(Integer vitalId) {
		this.vitalId = vitalId;
	}
	public Integer getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public char getIsSelfTriage() {
		return isSelfTriage;
	}
	public void setIsSelfTriage(char isSelfTriage) {
		this.isSelfTriage = isSelfTriage;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
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
	public char getIsAdmissionFlag() {
		return isAdmissionFlag;
	}
	public void setIsAdmissionFlag(char isAdmissionFlag) {
		this.isAdmissionFlag = isAdmissionFlag;
	}
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Date getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}
	public Date getDateTo() {
		return dateTo;
	}
	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
	public String getLocalUhid() {
		return localUhid;
	}
	public void setLocalUhid(String localUhid) {
		this.localUhid = localUhid;
	}
	public Integer getVitalOrderOpd() {
		return vitalOrderOpd;
	}
	public void setVitalOrderOpd(Integer vitalOrderOpd) {
		this.vitalOrderOpd = vitalOrderOpd;
	}
	public String getUnitDesc() {
		return unitDesc;
	}
	public void setUnitDesc(String unitDesc) {
		this.unitDesc = unitDesc;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public List<Integer> getListOfSelectedVitals() {
		return listOfSelectedVitals;
	}
	public void setListOfSelectedVitals(List<Integer> listOfSelectedVitals) {
		this.listOfSelectedVitals = listOfSelectedVitals;
	}
	public char getFlag() {
		return flag;
	}
	public void setFlag(char flag) {
		this.flag = flag;
	}
	public Integer getReasonId() {
		return reasonId;
	}
	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}
	public Character getIsEnteredInError() {
		return isEnteredInError;
	}
	public void setIsEnteredInError(Character isEnteredInError) {
		this.isEnteredInError = isEnteredInError;
	}
	public String getReasonDesc() {
		return reasonDesc;
	}
	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}
	public char getIsPostFacto() {
		return isPostFacto;
	}
	public void setIsPostFacto(char isPostFacto) {
		this.isPostFacto = isPostFacto;
	}
	public String getPostFactoTime() {
		return postFactoTime;
	}
	public void setPostFactoTime(String postFactoTime) {
		this.postFactoTime = postFactoTime;
	}
	public String getPostFactoDate() {
		return postFactoDate;
	}
	public void setPostFactoDate(String postFactoDate) {
		this.postFactoDate = postFactoDate;
	}
	public String getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	public Integer getVitalQualifireId() {
		return vitalQualifireId;
	}
	public void setVitalQualifireId(Integer vitalQualifireId) {
		this.vitalQualifireId = vitalQualifireId;
	}
	/*public List<AppointmentVitalMapperDto> getVitalMethodList() {
		return vitalMethodList;
	}
	public void setVitalMethodList(List<AppointmentVitalMapperDto> vitalMethodList) {
		this.vitalMethodList = vitalMethodList;
	}*/
	public AppointmentVitalMapperDto getListOfSelectedQualifiers() {
		return listOfSelectedQualifiers;
	}
	public void setListOfSelectedQualifiers(AppointmentVitalMapperDto listOfSelectedQualifiers) {
		this.listOfSelectedQualifiers = listOfSelectedQualifiers;
	}
	public String getVitalQualifireDesc() {
		return vitalQualifireDesc;
	}
	public void setVitalQualifireDesc(String vitalQualifireDesc) {
		this.vitalQualifireDesc = vitalQualifireDesc;
	}
	public String getVitalCategoryDesc() {
		return vitalCategoryDesc;
	}
	public void setVitalCategoryDesc(String vitalCategoryDesc) {
		this.vitalCategoryDesc = vitalCategoryDesc;
	}
	public Integer getDialysisEventId() {
		return dialysisEventId;
	}
	public void setDialysisEventId(Integer dialysisEventId) {
		this.dialysisEventId = dialysisEventId;
	}
	public Integer getPatientDialysisRecId() {
		return patientDialysisRecId;
	}
	public void setPatientDialysisRecId(Integer patientDialysisRecId) {
		this.patientDialysisRecId = patientDialysisRecId;
	}
	public String getCreatDate() {
		return creatDate;
	}
	public void setCreatDate(String creatDate) {
		this.creatDate = creatDate;
	}
	public Integer getCovTemplateId() {
		return covTemplateId;
	}
	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}
	public Date getCreatedTimeNew() {
		return createdTimeNew;
	}
	public void setCreatedTimeNew(Date createdTimeNew) {
		this.createdTimeNew = createdTimeNew;
	}
	public Date getUpdatedTimeNew() {
		return updatedTimeNew;
	}
	public void setUpdatedTimeNew(Date updatedTimeNew) {
		this.updatedTimeNew = updatedTimeNew;
	}
	public Date getCreatedDateNew() {
		return createdDateNew;
	}
	public void setCreatedDateNew(Date createdDateNew) {
		this.createdDateNew = createdDateNew;
	}
	public Date getUpdatedDateNew() {
		return updatedDateNew;
	}
	public void setUpdatedDateNew(Date updatedDateNew) {
		this.updatedDateNew = updatedDateNew;
	}
	
    public Boolean getIsIcu() {
        return isIcu;
    }

    public void setIsIcuu(Boolean isIcu) {
        this.isIcu = isIcu;
    }

	public String getSortedDateTime() {
		return sortedDateTime;
	}

	public void setSortedDateTime(String sortedDateTime) {
		this.sortedDateTime = sortedDateTime;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
