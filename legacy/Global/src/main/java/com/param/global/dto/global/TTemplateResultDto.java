package com.param.global.dto.global;

public class TTemplateResultDto {

	private Integer templateResId;

	private Integer orgId;
	private Integer orgUnitId;

	private Integer tSubSpecimanId;
	private Integer tBlockId;
	private Integer labTemplateId;

	private String reportNo;

	private Integer stainingId;

	private Integer labSampleDtlsId;
	
	private Integer reportReleaseBy;

	private Integer createdBy;

	private Long createdDate;

	private Integer updatedBy;

	private Long updatedDate;

	private Character isDeleted;

	private Integer sampleStatusId;

	private Integer orderDetailsId;

	private String templateResult;
	
	private String conversationId;
	
	private String postbackURL;

    private Boolean triggerReleaseMail;
    private Boolean isReleased;
    
    private Integer doctorId;
     
    private Integer patientId;
    private String templateUrl;
    private String testDesc;
	private String formData;
	
    private int tGrossId;
    private String grossNo;
    private String blockNo;
    private String slideNo;
    private int  tSlideId;
    private int tMicroExaId;
    private String grossDesc;
    private String blockDesc;
    private String impressionDesc;
    private Character isNad;
    private String slideDesc1;
	private String slideCaptureNote;
	private String captureNote;
	private String subSpecimanName;
	
	public Integer getTemplateResId() {
		return templateResId;
	}

	public void setTemplateResId(Integer templateResId) {
		this.templateResId = templateResId;
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

	public Integer gettSubSpecimanId() {
		return tSubSpecimanId;
	}

	public void settSubSpecimanId(Integer tSubSpecimanId) {
		this.tSubSpecimanId = tSubSpecimanId;
	}

	public Integer getLabTemplateId() {
		return labTemplateId;
	}

	public void setLabTemplateId(Integer labTemplateId) {
		this.labTemplateId = labTemplateId;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public Integer getStainingId() {
		return stainingId;
	}

	public void setStainingId(Integer stainingId) {
		this.stainingId = stainingId;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
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

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = (isDeleted == null) ? 'N' : isDeleted;
	}

	public String getTemplateResult() {
		return templateResult;
	}

	public void setTemplateResult(String templateResult) {
		this.templateResult = templateResult;
	}

	public Integer getSampleStatusId() {
		return sampleStatusId;
	}

	public void setSampleStatusId(Integer sampleStatusId) {
		this.sampleStatusId = sampleStatusId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getReportReleaseBy() {
		return reportReleaseBy;
	}

	public void setReportReleaseBy(Integer reportReleaseBy) {
		this.reportReleaseBy = reportReleaseBy;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	public String getPostbackURL() {
		return postbackURL;
	}

	public void setPostbackURL(String postbackURL) {
		this.postbackURL = postbackURL;
	}

	public Boolean getTriggerReleaseMail() {
		return triggerReleaseMail;
	}

	public void setTriggerReleaseMail(Boolean triggerReleaseMail) {
		this.triggerReleaseMail = triggerReleaseMail;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getTestDesc() {
		return testDesc;
	}

	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}

	public String getFormData() {
		return formData;
	}

	public void setFormData(String formData) {
		this.formData = formData;
	}

	public String getTemplateUrl() {
		return templateUrl;
	}

	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}

	public int gettGrossId() {
		return tGrossId;
	}

	public void settGrossId(int tGrossId) {
		this.tGrossId = tGrossId;
	}

	public int gettBlockId() {
		return tBlockId;
	}

	public void settBlockId(int tBlockId) {
		this.tBlockId = tBlockId;
	}

	public int gettMicroExaId() {
		return tMicroExaId;
	}

	public void settMicroExaId(int tMicroExaId) {
		this.tMicroExaId = tMicroExaId;
	}

	public String getGrossDesc() {
		return grossDesc;
	}

	public void setGrossDesc(String grossDesc) {
		this.grossDesc = grossDesc;
	}

	public String getBlockDesc() {
		return blockDesc;
	}

	public void setBlockDesc(String blockDesc) {
		this.blockDesc = blockDesc;
	}

	public String getImpressionDesc() {
		return impressionDesc;
	}

	public void setImpressionDesc(String impressionDesc) {
		this.impressionDesc = impressionDesc;
	}

	public Character getIsNad() {
		return isNad;
	}

	public void setIsNad(Character isNad) {
		this.isNad = isNad;
	}

	public String getSlideDesc1() {
		return slideDesc1;
	}

	public void setSlideDesc1(String slideDesc1) {
		this.slideDesc1 = slideDesc1;
	}

	public String getSlideCaptureNote() {
		return slideCaptureNote;
	}

	public void setSlideCaptureNote(String slideCaptureNote) {
		this.slideCaptureNote = slideCaptureNote;
	}

	public String getCaptureNote() {
		return captureNote;
	}

	public void setCaptureNote(String captureNote) {
		this.captureNote = captureNote;
	}

	public String getSubSpecimanName() {
		return subSpecimanName;
	}

	public void setSubSpecimanName(String subSpecimanName) {
		this.subSpecimanName = subSpecimanName;
	}

	public int gettSlideId() {
		return tSlideId;
	}

	public void settSlideId(int tSlideId) {
		this.tSlideId = tSlideId;
	}

	public String getGrossNo() {
		return grossNo;
	}

	public void setGrossNo(String grossNo) {
		this.grossNo = grossNo;
	}

	public String getBlockNo() {
		return blockNo;
	}

	public void setBlockNo(String blockNo) {
		this.blockNo = blockNo;
	}

	public String getSlideNo() {
		return slideNo;
	}

	public void setSlideNo(String slideNo) {
		this.slideNo = slideNo;
	}

	public void settBlockId(Integer tBlockId) {
		this.tBlockId = tBlockId;
	}

	public Boolean getIsReleased() {
		return isReleased;
	}

	public void setIsReleased(Boolean isReleased) {
		this.isReleased = isReleased;
	}

	
	
}
