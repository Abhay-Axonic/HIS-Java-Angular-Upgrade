package com.param.entity.lis.global;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_LAB_CONFIG_BY_ID", query ="select "
			+ "  labcon.t_lab_config_id as \"tLabConfigId\", "
			+ "  labcon.lab_speciality_id as \"labSpecialityId\", "
			+ "  spemst.speciality_name as \"specialityName\", "
			+ "  labcon.biochemistry_id as \"biochemistryId\", "
			+ "	 biochemspe.sub_speciality_name as \"biochemistryDept\", "
			+ "  labcon.hematology_id as \"hematologyId\", "
			+ "	 hematsupsp.sub_speciality_name as \"heamotologyDept\", "
			+ "  labcon.microbiology_id as \"microbiologyId\" , "
			+ "	 microbiosub.sub_speciality_name as \"microbioDept\", "
			+ "  labcon.histopathology_id as \"histopathologyId\", "
			+ "	 histo.sub_speciality_name as \"histopathDept\", "
			+ "  labcon.cystopathology_id as \"cystopathologyId\", "
			+ "	 cyto.sub_speciality_name as \"cytopathDept\", "
			+ "  labcon.serology_id as \"serologyId\", "
			+ "	 seriology.sub_speciality_name as \"seologyDept\", "
			+ "  labcon.is_prebarcode_generantion as \"isPrebarcodeGenerantion\", "
			+ "  labcon.clinical_pathology_id as \"clinicalPathologyId\", "
			+ "  labcon.status as \"status\", "
			+ "  labcon.is_pin_applicable as \"isPinApplicable\", "
			+ "  labcon.organisation_id as \"organisationId\", "
			+ "  labcon.is_send_to_cr_applicable as \"isSendToCrApplicable\", "
			+ "  labcon.unit_id as \"unitId\", "
			+ "  labcon.created_by as \"createdBy\" "
			+ "  from public.t_lab_configuration labcon "
			+ "  left join doctor.m_speciality_master spemst on "
			+ "	  labcon.lab_speciality_id = spemst.speciality_id "
			+ "  left join doctor.m_sub_speciality_master biochemspe on "
			+ "	  labcon.biochemistry_id = biochemspe.sub_speciality_id "
			+ "  left join doctor.m_sub_speciality_master hematsupsp on "
			+ "	  labcon.hematology_id = hematsupsp.sub_speciality_id "
			+ "  left join doctor.m_sub_speciality_master microbiosub on "
			+ "	  labcon.microbiology_id = microbiosub.sub_speciality_id "
			+ "  left join doctor.m_sub_speciality_master cyto on "
			+ "	  labcon.cystopathology_id = cyto.sub_speciality_id "
			+ "  left join doctor.m_sub_speciality_master histo on "
			+ "	  labcon.histopathology_id = microbiosub.sub_speciality_id "
			+ "  left join doctor.m_sub_speciality_master seriology on "
			+ "	  labcon.serology_id = seriology.sub_speciality_id "
			+ "  where labcon.t_lab_config_id = :tLabConfigId"),
	
	@NamedNativeQuery(name = "GET_LAB_CONFIG_BY_BY_ORG_UNIT", query ="select "
			+ "  coalesce(labcon.t_lab_config_id,0) as \"tLabConfigId\", "
			+ "  coalesce(labcon.lab_speciality_id,0) as \"labSpecialityId\", "
			+ "  coalesce(labcon.biochemistry_id,0) as \"biochemistryId\", "
			+ "  coalesce(labcon.hematology_id,0) as \"hematologyId\", "
			+ "  coalesce(labcon.microbiology_id,0) as \"microbiologyId\" , "
			+ "  coalesce(labcon.histopathology_id,0) as \"histopathologyId\", "
			+ "  coalesce(labcon.cystopathology_id,0) as \"cystopathologyId\", "
			+ "  coalesce(labcon.serology_id,0) as \"serologyId\", "
			+ "  coalesce(labcon.clinical_pathology_id,0) as \"clinicalPathologyId\", "
			+ "  labcon.is_prebarcode_generantion as \"isPrebarcodeGenerantion\" , "
			+ "  labcon.is_send_to_cr_applicable as \"isSendToCrApplicable\", "
			+ "  labcon.is_sample_acceptance_applicable as \"isSampleAcceptanceApplicable\", "
			+ "  labcon.is_acceptance_in_department as \"isAcceptanceInDepartment\", "
			+ "  labcon.is_worklist_department as \"isWorklistDepartment\", "
			+ "  labcon.is_report_recall as \"isReportRecall\", "
			+ "  labcon.is_pin_applicable as \"isPinApplicable\" ,"
			+ " labcon.is_validation_and_release as \"isValidationAndRelease\", "
			+ " labcon.is_report_release_pin as \"reportReleasePin\", "
			+ " labcon.photo_required as \"photoRequired\", "
			+ " labcon.consolidated_result_entry as \"consolidatedResultEntry\","
			+ " labcon.group_release as \"groupRelease\", "
			+ " labcon.trigger_release_mail as \"triggerReleaseMail\","
			+ "labcon.is_prn_print as \"isPRNPrint\" , labcon.prn_format as \"prnFormat\", labcon.prn_url as \"prnUrl\","
			+ "  labcon.collect_sample_on_opbilling as \"collectSampleOnOPBilling\" ,"
			+ " labcon.lab_sorting_id as \"labSortingId\", "
			+ " labcon.skip_validation as \"skipValidation\","
			+ " labcon.by_sample_type as \"bySampleType\","
			+ " labcon.by_department as \"byDepartment\","
			+ " labcon.by_container as \"byContainer\" ,"
			+ " labcon.send_mail_on_order_completion as \"sendMailOnOrderCompletion\", "
			+ " labcon.manual_sample_collection_time as \"manualSampleCollectionTime\", "
			+ " labcon.incubation_master_applicable as \"incubationMasterApplicable\", "
			+ " labcon.macroscopic_examination_applicable as \"macroscopicExaminationApplicable\", "
			+ " labcon.biochemical_testing_applicable as \"biochemicalTestingApplicable\", "
			+ " labcon.microscopic_examination_applicable as \"microscopicExaminationApplicable\" ,"
			+ " labcon.qr_code_required as \"qrCodeRequired\" ,"
			+ " labcon.outsource_lab_id as \"outsourceLabId\" ,"
			+ " labcon.print_lab_report_befor_release as \"printLabReportBeforRelease\" "
			+ "  from public.t_lab_configuration labcon "
			+ "	 where labcon.organisation_id = :orgId "
			+ "	 and labcon.unit_id =:unitId "
			+ "  order by t_lab_config_id desc limit 1"),
	
	
	@NamedNativeQuery(name = "GET_LAB_CONFIG_LIST", query ="select "
			+ "	labcon.t_lab_config_id as \"tLabConfigId\", "
			+ "	labcon.lab_speciality_id as \"labSpecialityId\", "
			+ "	labcon.biochemistry_id as \"biochemistryId\", "
			+ "	biochemspe.sub_speciality_name as \"biochemistryDept\", "
			+ "	labcon.hematology_id as \"hematologyId\", "
			+ "	hematsupsp.sub_speciality_name as \"heamotologyDept\", "
			+ "	labcon.microbiology_id as \"microbiologyId\" , "
			+ "	microbiosub.sub_speciality_name as \"microbioDept\", "
			+ "	labcon.histopathology_id as \"histopathologyId\", "
			+ "	histo.sub_speciality_name as \"histopathDept\", "
			+ "	labcon.cystopathology_id as \"cystopathologyId\", "
			+ "	cyto.sub_speciality_name as \"cytopathDept\", "
			+ " spemst.speciality_name as \"specialityName\", "
			+ "	labcon.serology_id as \"serologyId\", "
			+ "	seriology.sub_speciality_name as \"seologyDept\", "
			+ "	labcon.is_prebarcode_generantion as \"isPrebarcodeGenerantion\", "
			+ " labcon.is_pin_applicable as \"isPinApplicable\", "
			+ "	labcon.status as \"status\", "
			+ "	labcon.organisation_id as \"organisationId\", "
			+ "	labcon.unit_id as \"unitId\", "
			+ "	labcon.created_by as \"createdBy\" ,"
			+ " labcon.clinical_pathology_id as \"clinicalPathologyId\", "
			+ " labcon.is_send_to_cr_applicable as \"isSendToCrApplicable\", "
			+ " labcon.is_sample_acceptance_applicable as \"isSampleAcceptanceApplicable\", "
			+ " labcon.is_acceptance_in_department as \"isAcceptanceInDepartment\", "
			+ " labcon.is_worklist_department as \"isWorklistDepartment\", "
			+ "	clinical.sub_speciality_name as \"clinicalPathologyDept\", "
			+ " labcon.is_report_recall as \"isReportRecall\" ,"
			+ " labcon.is_validation_and_release as \"isValidationAndRelease\", "
			+ " labcon.is_report_release_pin as \"reportReleasePin\","
			+ " labcon.photo_required as \"photoRequired\","
			+ " labcon.consolidated_result_entry as \"consolidatedResultEntry\", "
			+ " labcon.group_release as \"groupRelease\" ,"
			+ " labcon.trigger_release_mail as \"triggerReleaseMail\", labcon.is_prn_print as \"isPRNPrint\", "
			+ "  labcon.collect_sample_on_opbilling as \"collectSampleOnOPBilling\","
			+ " labcon.lab_sorting_id as \"labSortingId\","
			+ " labcon.skip_validation as \"skipValidation\", "
			+ " labcon.by_sample_type as \"bySampleType\","
			+ " labcon.by_department as \"byDepartment\","
			+ " labcon.by_container as \"byContainer\","
			+ " labcon.send_mail_on_order_completion as \"sendMailOnOrderCompletion\", "
			+ " labcon.manual_sample_collection_time as \"manualSampleCollectionTime\","
			+ " labcon.incubation_master_applicable as \"incubationMasterApplicable\", "
			+ " labcon.macroscopic_examination_applicable as \"macroscopicExaminationApplicable\", "
			+ " labcon.biochemical_testing_applicable as \"biochemicalTestingApplicable\", "
			+ " labcon.microscopic_examination_applicable as \"microscopicExaminationApplicable\", "
			+ " labcon.qr_code_required as \"qrCodeRequired\" ,"
			+ " labcon.outsource_lab_id as \"outsourceLabId\" ,"
			+ " labcon.print_lab_report_befor_release as \"printLabReportBeforRelease\" "
			+ " from "
			+ "	public.t_lab_configuration labcon "
			+ "left join doctor.m_speciality_master spemst on "
			+ "	 labcon.lab_speciality_id = spemst.speciality_id "
			+ "left join doctor.m_sub_speciality_master biochemspe on "
			+ "	 labcon.biochemistry_id = biochemspe.sub_speciality_id "
			+ "left join doctor.m_sub_speciality_master hematsupsp on "
			+ "	 labcon.hematology_id = hematsupsp.sub_speciality_id "
			+ "left join doctor.m_sub_speciality_master microbiosub on "
			+ "	 labcon.microbiology_id = microbiosub.sub_speciality_id "
			+ "left join doctor.m_sub_speciality_master cyto on "
			+ "	   labcon.cystopathology_id = cyto.sub_speciality_id "
			+ "left join doctor.m_sub_speciality_master histo on "
			+ "	  labcon.histopathology_id = microbiosub.sub_speciality_id "
			+ "left join doctor.m_sub_speciality_master seriology on "
			+ "	 labcon.serology_id = seriology.sub_speciality_id "
			+ " left join doctor.m_sub_speciality_master clinical on "
			+ "	 labcon.clinical_pathology_id = clinical.sub_speciality_id "
			+  " where "
			+ "	labcon.organisation_id = :orgId "
			+ "	and labcon.unit_id =:unitId"),
	
	@NamedNativeQuery(name = "UPDATE_LAB_CONFIG_STATUS", query ="update public.t_lab_configuration set status =:status "
			+ "	where t_lab_config_id = :tLabConfigId"),
	
	
	@NamedNativeQuery(name = "CHECK_LAB_CONFIG_EXIST_OR_NOT", query ="select count(*) from public.t_lab_configuration con "
			+ "	where con.organisation_id =:orgId "
			+ "	and con.unit_id = :unitId "),
	@NamedNativeQuery(name = "CHECK_PIN_EXIST_OR_NOT", query ="select count(*)  from public.m_user_master um "
			+ "	where um.organization_id =:orgId "
			+ "	and um.unit_id =:unitId "),
	@NamedNativeQuery(name = "GET_USER_BY_PIN", query ="select um.user_id as \"userId\",um.user_name as \"userName\", um.password as \"password\" from public.m_user_master um "
			+ "	where um.organization_id =:orgId "
			//+ "	and um.unit_id =:unitId "
			+ " and um.lab_pin =:pin"),
	@NamedNativeQuery(name = "CHECK_PIN_EXIST_OR_NOT_GET_USERID", query ="select count(um.user_id) as \"userId\" from public.m_user_master um "
			+ "	where um.organization_id =:orgId "
			+ "	and um.unit_id =:unitId "),
	
})

@Entity
@Table(name="t_lab_configuration",schema="public")
@SequenceGenerator(name="seq_t_lab_configuration",sequenceName="public.seq_t_lab_configuration",allocationSize=1)
public class LabConfigurationMaster {
	
	@Id
	@Column(name="t_lab_config_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_t_lab_configuration")
	private Integer  tLabConfigId;
	
	@Column(name="lab_speciality_id")
	private Integer labSpecialityId;
	
	@Column(name="biochemistry_id")
	private Integer biochemistryId;
	
	@Column(name="hematology_id")
	private Integer hematologyId;
	
	@Column(name="microbiology_id")
	private Integer microbiologyId;
	
	@Column(name="histopathology_id")
	private Integer histopathologyId;
	
	@Column(name="cystopathology_id")
	private Integer cystopathologyId;
	
	@Column(name="serology_id")
	private Integer serologyId;
	
	@Column(name="is_prebarcode_generantion")
	private char isPrebarcodeGenerantion;
	
	@Column(name="status")
	private char status;
	
	@Column(name="is_send_to_cr_applicable")
	private char isSendToCrApplicable;
	
	@Column(name="organisation_id")
	private Integer organisationId;
	
	@Column(name="unit_id")
	private Integer unitId;

	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="is_pin_applicable")
	private Boolean isPinApplicable;
	
	@Column(name="clinical_pathology_id")
	private Integer clinicalPathologyId;
	
	@Column(name="is_sample_acceptance_applicable")
	private char isSampleAcceptanceApplicable;
	
	@Column(name="is_acceptance_in_department")
	private char isAcceptanceInDepartment;
	
	@Column(name="is_worklist_department")
	private char isWorklistDepartment;
	
	@Column(name="is_report_recall")
	private char isReportRecall;
	
	@Column(name="is_validation_and_release")
	private char isValidationAndRelease;

	@Column(name="is_report_release_pin")
	private char reportReleasePin; 
	
	@Column(name="photo_required")
	private char photoRequired; 
	
	@Column(name="consolidated_result_entry")
	private char consolidatedResultEntry; 
	
	@Column(name="group_release")
	private char groupRelease; 
	
	@Column(name="trigger_release_mail")
	private char triggerReleaseMail; 
	
	@Column(name="is_prn_print")
	private char isPRNPrint; 
	
	/*@Column(name="prn_format")
	private String prnFormat;*/
	
	@Column(name="collect_sample_on_opbilling")
	private char collectSampleOnOPBilling; 
	
	@Column(name="lab_sorting_id")
	private Integer labSortingId;
	
	@Column(name="skip_validation")
	private char skipValidation; 
	
	@Column(name="by_sample_type")
	private char bySampleType; 
	
	@Column(name="by_department")
	private char byDepartment; 
	
	@Column(name="by_container")
	private char byContainer; 
	
	@Column(name="send_mail_on_order_completion")
	private char sendMailOnOrderCompletion;
	
	@Column(name="manual_sample_collection_time")
	private char manualSampleCollectionTime; 
	
	@Column(name="incubation_master_applicable")
	private char incubationMasterApplicable;
	
	@Column(name="macroscopic_examination_applicable")
	private char macroscopicExaminationApplicable;
	
	@Column(name="biochemical_testing_applicable")
	private char biochemicalTestingApplicable;
	
	@Column(name="microscopic_examination_applicable")
	private char microscopicExaminationApplicable;
	
	@Column(name="qr_code_required")
	private char qrCodeRequired;
	
	@Column(name="print_lab_report_befor_release")
	private char printLabReportBeforRelease;
	
	@Column(name="outsource_lab_id")
	private Integer outsourceLabId;
	
	public Integer gettLabConfigId() {
		return tLabConfigId;
	}

	public void settLabConfigId(Integer tLabConfigId) {
		this.tLabConfigId = tLabConfigId;
	}

	public Integer getLabSpecialityId() {
		return labSpecialityId;
	}

	public void setLabSpecialityId(Integer labSpecialityId) {
		this.labSpecialityId = labSpecialityId;
	}

	public Integer getHematologyId() {
		return hematologyId;
	}

	public void setHematologyId(Integer hematologyId) {
		this.hematologyId = hematologyId;
	}

	public Integer getHistopathologyId() {
		return histopathologyId;
	}

	public void setHistopathologyId(Integer histopathologyId) {
		this.histopathologyId = histopathologyId;
	}

	public Integer getCystopathologyId() {
		return cystopathologyId;
	}

	public void setCystopathologyId(Integer cystopathologyId) {
		this.cystopathologyId = cystopathologyId;
	}

	public Integer getSerologyId() {
		return serologyId;
	}

	public void setSerologyId(Integer serologyId) {
		this.serologyId = serologyId;
	}

	public char getIsPrebarcodeGenerantion() {
		return isPrebarcodeGenerantion;
	}

	public void setIsPrebarcodeGenerantion(char isPrebarcodeGenerantion) {
		this.isPrebarcodeGenerantion = (isPrebarcodeGenerantion=='\u0000')?'N':isPrebarcodeGenerantion;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getBiochemistryId() {
		return biochemistryId;
	}

	public void setBiochemistryId(Integer biochemistryId) {
		this.biochemistryId = biochemistryId;
	}

	public Integer getMicrobiologyId() {
		return microbiologyId;
	}

	public void setMicrobiologyId(Integer microbiologyId) {
		this.microbiologyId = microbiologyId;
	}

	public Boolean getIsPinApplicable() {
		return isPinApplicable;
	}

	public void setIsPinApplicable(Boolean isPinApplicable) {
		this.isPinApplicable = isPinApplicable;
	}

	public Integer getClinicalPathologyId() {
		return clinicalPathologyId;
	}

	public void setClinicalPathologyId(Integer clinicalPathologyId) {
		this.clinicalPathologyId = clinicalPathologyId;
	}

	public char getIsSendToCrApplicable() {
		return isSendToCrApplicable;
	}

	public void setIsSendToCrApplicable(char isSendToCrApplicable) {
		this.isSendToCrApplicable = (isSendToCrApplicable == '\u0000') ? 'N' : isSendToCrApplicable;
	}

	public char getIsSampleAcceptanceApplicable() {
		return isSampleAcceptanceApplicable;
	}

	public void setIsSampleAcceptanceApplicable(char isSampleAcceptanceApplicable) {
		this.isSampleAcceptanceApplicable = isSampleAcceptanceApplicable;
		this.isSampleAcceptanceApplicable = (isSampleAcceptanceApplicable == '\u0000') ? 'N' : isSampleAcceptanceApplicable;
	}

	public char getIsAcceptanceInDepartment() {
		return isAcceptanceInDepartment;
	}

	public void setIsAcceptanceInDepartment(char isAcceptanceInDepartment) {
		this.isAcceptanceInDepartment = (isAcceptanceInDepartment == '\u0000') ? 'N' : isAcceptanceInDepartment;
	}

	public char getIsWorklistDepartment() {
		return isWorklistDepartment;
	}

	public void setIsWorklistDepartment(char isWorklistDepartment) {
		this.isWorklistDepartment = (isWorklistDepartment == '\u0000') ? 'N' : isWorklistDepartment;
	}

	public char getIsReportRecall() {
		return isReportRecall;
	}

	public void setIsReportRecall(char isReportRecall) {
		this.isReportRecall = (isReportRecall == '\u0000') ? 'N' : isReportRecall;
	}

	public char getIsValidationAndRelease() {
		return isValidationAndRelease;
	}

	public void setIsValidationAndRelease(char isValidationAndRelease) {
		this.isValidationAndRelease = (isValidationAndRelease == '\u0000') ? 'N' : isValidationAndRelease;
	}

	public char getReportReleasePin() {
		return reportReleasePin;
	}

	public void setReportReleasePin(char reportReleasePin) {
		this.reportReleasePin = reportReleasePin;
	}

	public char getPhotoRequired() {
		return photoRequired;
	}

	public void setPhotoRequired(char photoRequired) {
		this.photoRequired = photoRequired;
	}

	public char getConsolidatedResultEntry() {
		return consolidatedResultEntry;
	}

	public void setConsolidatedResultEntry(char consolidatedResultEntry) {
		this.consolidatedResultEntry = consolidatedResultEntry;
	}

	public char getGroupRelease() {
		return groupRelease;
	}

	public void setGroupRelease(char groupRelease) {
		this.groupRelease = groupRelease;
	}

	public char getTriggerReleaseMail() {
		return triggerReleaseMail;
	}

	public void setTriggerReleaseMail(char triggerReleaseMail) {
		this.triggerReleaseMail = triggerReleaseMail;
	}

	public char getIsPRNPrint() {
		return isPRNPrint;
	}

	public void setIsPRNPrint(char isPRNPrint) {
//		this.isPRNPrint = isPRNPrint;
		this.isPRNPrint = (isPRNPrint == '\u0000') ? 'N' : isPRNPrint;
	}

	/*public String getPrnFormat() {
		return prnFormat;
	}

	public void setPrnFormat(String prnFormat) {
		this.prnFormat = prnFormat;
	}*/

	public char getCollectSampleOnOPBilling() {
		return collectSampleOnOPBilling;
	}

	public void setCollectSampleOnOPBilling(char collectSampleOnOPBilling) {
		this.collectSampleOnOPBilling = collectSampleOnOPBilling;
	}

	public Integer getLabSortingId() {
		return labSortingId;
	}

	public void setLabSortingId(Integer labSortingId) {
		this.labSortingId = labSortingId;
	}

	public char getSkipValidation() {
		return skipValidation;
	}

	public void setSkipValidation(char skipValidation) {
		this.skipValidation = skipValidation;
	}

	public char getBySampleType() {
		return bySampleType;
	}

	public void setBySampleType(char bySampleType) {
		this.bySampleType = bySampleType;
	}

	public char getByDepartment() {
		return byDepartment;
	}

	public void setByDepartment(char byDepartment) {
		this.byDepartment = byDepartment;
	}

	public char getByContainer() {
		return byContainer;
	}

	public void setByContainer(char byContainer) {
		this.byContainer = byContainer;
	}

	public char getSendMailOnOrderCompletion() {
		return sendMailOnOrderCompletion;
	}

	public void setSendMailOnOrderCompletion(char sendMailOnOrderCompletion) {
		this.sendMailOnOrderCompletion = sendMailOnOrderCompletion;
	}

	public char getManualSampleCollectionTime() {
		return manualSampleCollectionTime;
	}

	public void setManualSampleCollectionTime(char manualSampleCollectionTime) {
		this.manualSampleCollectionTime = manualSampleCollectionTime;
	}

	public char getIncubationMasterApplicable() {
		return incubationMasterApplicable;
	}

	public void setIncubationMasterApplicable(char incubationMasterApplicable) {
		this.incubationMasterApplicable = incubationMasterApplicable;
	}

	public char getMacroscopicExaminationApplicable() {
		return macroscopicExaminationApplicable;
	}

	public void setMacroscopicExaminationApplicable(char macroscopicExaminationApplicable) {
		this.macroscopicExaminationApplicable = macroscopicExaminationApplicable;
	}

	public char getBiochemicalTestingApplicable() {
		return biochemicalTestingApplicable;
	}

	public void setBiochemicalTestingApplicable(char biochemicalTestingApplicable) {
		this.biochemicalTestingApplicable = biochemicalTestingApplicable;
	}

	public char getMicroscopicExaminationApplicable() {
		return microscopicExaminationApplicable;
	}

	public void setMicroscopicExaminationApplicable(char microscopicExaminationApplicable) {
		this.microscopicExaminationApplicable = microscopicExaminationApplicable;
	}

	public char getQrCodeRequired() {
		return qrCodeRequired;
	}

	public void setQrCodeRequired(char qrCodeRequired) {
		this.qrCodeRequired = qrCodeRequired;
	}

	public char getPrintLabReportBeforRelease() {
		return printLabReportBeforRelease;
	}

	public void setPrintLabReportBeforRelease(char printLabReportBeforRelease) {
		this.printLabReportBeforRelease = printLabReportBeforRelease;
	}

	public Integer getOutsourceLabId() {
		return outsourceLabId;
	}

	public void setOutsourceLabId(Integer outsourceLabId) {
		this.outsourceLabId = outsourceLabId;
	}
	
	
}
