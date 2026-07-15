package com.param.entity.lis.histo;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.*;
import com.param.entity.lis.global.StainigMaster;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.entity.lis.unit.LabTemplateMaster;
import com.param.entity.lis.common.LocalTimeConverter;



@NamedNativeQueries({
	
    @NamedNativeQuery(
            name = "GET_REPORT_NAME_BY_ID",
            query = "SELECT CASE WHEN ( COUNT(t) > 0 ) THEN true ELSE false END FROM lab.t_template_result t " +
                    "WHERE (LOWER(t.report_no)=:reportNo OR t.report_no=:reportNo) " +
                    "AND t.org_unit_id=:orgUnitId " +
                    "AND t.org_id=:orgId " +
                    "AND t.is_deleted ='N'; "
             
        ),
    

    
})


@Entity
@Table(name = "t_template_result", schema = "lab")
@SequenceGenerator(name = "t_seq_template_result", sequenceName = "lab.t_seq_template_result", allocationSize = 1)
public class TTemplateResult {
	
	@Id
	@Column(name = "template_res_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_template_result")
	private int templateResId;
	
	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "org_unit_id")
	private Integer orgUnitId;
	
	@Column(name = "t_sub_speciman_id")
	private Integer tSubSpecimanId;
	
	@Column(name = "lab_template_id")
	private Integer labTemplateId;
	
	@Column(name = "report_no")
	private String reportNo;
	
	@Column(name = "staining_id")
	private Integer stainingId;
	
	@Column(name = "lab_sample_dtls_id")
	private Integer labSampleDtlsId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	@Convert(converter = LocalTimeConverter.class)
	private Long createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	@Convert(converter = LocalTimeConverter.class)
	private Long updatedDate;
	
	@Column(name = "is_deleted")
	private Character isDeleted;
	
	@Column(name = "template_result")
	private String templateResult;

	@Column(name = "report_release_by")
	private Integer reportReleaseBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lab_sample_dtls_id", insertable = false, nullable = false, updatable = false)
	private LabSampleDetailsMaster labSampleDetailsMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "staining_id", insertable = false, nullable = false, updatable = false)
	private StainigMaster stainigMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "t_sub_speciman_id", insertable = false, nullable = false, updatable = false)
	private TSubSpecimanMaster tSubSpecimanMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lab_template_id", insertable = false, nullable = false, updatable = false)
	private LabTemplateMaster labTemplateMaster;

	@Column(name = "conversation_id")
	private String conversationId;
	
	@Column(name = "postback_url")
	private String postbackURL;
	
	@Column(name = "t_gross_id")
	private int tgrossid;
	
	@Column(name = "gross_capture_note")
	private String grossCaptureNote;
	
	@Column(name = "t_block_id")
	private int tblockid;
	
	@Column(name = "block_capture_note")
	private String blockCaptureNote;
	
	@Column(name = "t_slide_id")
	private int tslideid;
	
	@Column(name = "micro_capture_note")
	private String microCaptureNote;
	
	@Column(name = "micro_impression")
	private String microImpression;
	
	@Transient
	private Integer sampleStatusId;
	
	public int getTemplateResId() {
		return templateResId;
	}

	public void setTemplateResId(int templateResId) {
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
		this.isDeleted = isDeleted;
	}

	public LabSampleDetailsMaster getLabSampleDetailsMaster() {
		return labSampleDetailsMaster;
	}

	public void setLabSampleDetailsMaster(LabSampleDetailsMaster labSampleDetailsMaster) {
		this.labSampleDetailsMaster = labSampleDetailsMaster;
	}

	public StainigMaster getStainigMaster() {
		return stainigMaster;
	}

	public void setStainigMaster(StainigMaster stainigMaster) {
		this.stainigMaster = stainigMaster;
	}

	public LabTemplateMaster getLabTemplateMaster() {
		return labTemplateMaster;
	}

	public void setLabTemplateMaster(LabTemplateMaster labTemplateMaster) {
		this.labTemplateMaster = labTemplateMaster;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer gettSubSpecimanId() {
		return tSubSpecimanId;
	}

	public void settSubSpecimanId(Integer tSubSpecimanId) {
		this.tSubSpecimanId = tSubSpecimanId;
	}

	public TSubSpecimanMaster gettSubSpecimanMaster() {
		return tSubSpecimanMaster;
	}

	public void settSubSpecimanMaster(TSubSpecimanMaster tSubSpecimanMaster) {
		this.tSubSpecimanMaster = tSubSpecimanMaster;
	}

	public String getTemplateResult() {
		return templateResult;
	}

	public void setTemplateResult(String templateResult) {
		this.templateResult = templateResult;
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

	public int getTgrossid() {
		return tgrossid;
	}

	public void setTgrossid(int tgrossid) {
		this.tgrossid = tgrossid;
	}

	public String getGrossCaptureNote() {
		return grossCaptureNote;
	}

	public void setGrossCaptureNote(String grossCaptureNote) {
		this.grossCaptureNote = grossCaptureNote;
	}

	public int getTblockid() {
		return tblockid;
	}

	public void setTblockid(int tblockid) {
		this.tblockid = tblockid;
	}

	public String getBlockCaptureNote() {
		return blockCaptureNote;
	}

	public void setBlockCaptureNote(String blockCaptureNote) {
		this.blockCaptureNote = blockCaptureNote;
	}

	public int getTslideid() {
		return tslideid;
	}

	public void setTslideid(int tslideid) {
		this.tslideid = tslideid;
	}

	public String getMicroCaptureNote() {
		return microCaptureNote;
	}

	public void setMicroCaptureNote(String microCaptureNote) {
		this.microCaptureNote = microCaptureNote;
	}

	public String getMicroImpression() {
		return microImpression;
	}

	public void setMicroImpression(String microImpression) {
		this.microImpression = microImpression;
	}

	public Integer getSampleStatusId() {
		return sampleStatusId;
	}

	public void setSampleStatusId(Integer sampleStatusId) {
		this.sampleStatusId = sampleStatusId;
	}
	
	
}
