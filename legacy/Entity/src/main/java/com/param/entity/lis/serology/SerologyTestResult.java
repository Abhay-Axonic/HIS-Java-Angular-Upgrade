package com.param.entity.lis.serology;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.lis.common.LocalTimeConverter;

@NamedNativeQueries({
	
    @NamedNativeQuery(name = "GET_SEROLOGY_RESULT_ENTRY_LIST", query = "SELECT "
    		+ "subspe_mst.sub_speciality_name as \"subSpecialityName\","
    		+ "    bill_mst.bill_number as \"billNumber\", " 
    		+ "  visit_type_master.visit_type_code AS \"visitType\", "
    		+ "  lbsampledtls.org_id AS \"orgId\", "
    		+ "  lbsampledtls.org_unit_id AS \"orgUnitId\", "
    		+ "  panel_master.panel_code AS \"panelCode\", "
    		+ "  panel_master.panel_id AS \"panelId\", "
    		+ "  lbsampledtls.gender_id AS \"genderId\", "
    		+ "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\", "
    		+ "  lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\", "
    		+ "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\", "
    		+ "  pati_mst.uhid_number AS uhid, "
    		+ "  pati_mst.image_path AS \"imagePath\", "
    		+ "  sample_mst.sample_id AS \"sampleNo\", "
    		+ "  test_mst.test_desc AS \"testDesc\", "
    		+ "  order_master.admission_id AS \"visitAdmId\", "
    		+ "  order_master.visit_type_id AS \"visitTypeId\", "
    		+ "  lbsampledtls.dept_id AS \"deptId\", "
    		+ "  lbsampledtls.test_id AS \"testId\", "
    		+ "  test_mst.test_type AS \"testType\", "
    		+ "  lbsampledtls.sub_dept_id AS \"subDeptId\", "
    		+ "  lbsampledtls.patient_id AS \"patientId\", "
    		+ "  lbsampledtls.order_details_id AS \"orderDetailsId\", "
            + "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || "
            + " calculate_age(pati_mst.dob)  || '/' || gen_mst.gender_code AS \"patientDetails\", "
    		+ "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\", "
    		+ "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\", "
    		+ "  lbsampledtls.sample_barcode AS \"sampleBarcode\", "
    		+ "  order_details.created_date AS \"orderDateTime\", "
    		+ "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\" , "
    		+ "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",  "
            + "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDatetimeString\",  "
    		+ "  lbsampledtls.is_on_hold_parameter as \"isOnHoldParameter\" , "
    		+ "  wardmast.ward_code AS \"wardCode\" , "
    		+ "  bedmst.bed_code AS \"bedNumber\", "
    		+ "  test_mst.test_code AS \"testCode\" , "
    		+ "  test_mst.pro_release AS \"proRelease\" , "
    		+ "  samp_mst.sample_desc AS \"sampleType\" , "
    		+" (case when order_master.encounter_id > 0 then " +
    		" ( tpsd.payment_entitlement_id ) " +
    		" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
    		+ " order_master.order_no AS \"orderNo\","
    		+ " order_master.order_id as \"orderId\","
    		+" (case when order_master.encounter_id > 0 then " +
    		" ( tpsd.payee_id ) " +
    		" else ( tpsd2.payee_id ) end ) as \"payeeId\", "
    		+ " cast ( (coalesce (retest.retestCount ,0) ) as BIGINT) as \"retestCount\" ,"
    		 + " order_details.is_dummy_service as \"isDummyService\" "
			+ " FROM "
    		+ "  lab.t_lab_sample_dtls lbsampledtls "
    		+ "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id "
    		+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id "
    		+ "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id "
    		+ "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id "
    		+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id "
    		+ "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id "
    		+ "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id "
    		+ "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id "
    		+ "  INNER JOIN lab.m_test_master test_master ON test_master.test_id = lbsampledtls.test_id "
    		+ "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id "
    		+ "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_master.sample_id "
    		+ "  inner join public.t_lab_configuration lab_config on lab_config.unit_id = lbsampledtls.org_unit_id "
    		+ "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id "
    		+ "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id "
    		+ "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id "
    		+ " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
			+ " left join opd.t_patient_sponsor_details tpsd "
			+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
			+ " left join opd.t_patient_sponsor_details tpsd2 "
			+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
    		+ "   LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
    		+ "  and admission_dtls.status = 'A' "
    		+ "  and admission_dtls.nursing_acceptance = 'Y' "
    		+ "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id "
    		+ "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id "
    		+ "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id "
    		+ " left join " 
    		+ "	(select count(sample_dtls_id) retestCount  , sample_dtls_id from lab.t_sample_audit_trail sample_audit_trail "  
    		+ "	where test_status_id = 1 group by sample_dtls_id ) as retest on retest.sample_dtls_id = lbsampledtls.lab_sample_dtls_id "
    		+ " left join billing.t_billing_details bill_dtls on " 
    		+ " 	order_details.order_details_id = bill_dtls.order_details_id " 
    		+ " left join billing.m_billing_master bill_mst on " 
    		+ " 	bill_mst.billing_master_id = bill_dtls.billing_master_id " 
    		+ " left join billing.t_credit_note_detail tcnd on "
    		+ " tcnd.order_detail_id = order_details.order_details_id "
    		+ " and tcnd.status = 'A' "
    		+ " and tcnd.cancel_or_discount = 1 "
			+ " LEFT JOIN service.t_unit_service_master_details service_dtls on "
			+ " 	order_details.service_id = service_dtls.service_master_id "
			+ " 	and lbsampledtls.org_unit_id = service_dtls.unit_id "
			+ " 	and service_dtls.status = 'A' "
    		+ " WHERE "
    		+ "   lbsampledtls.org_id = :orgId "
    		+ "  AND lbsampledtls.org_unit_id = :orgUnitId "
    		+ "  AND lbsampledtls.is_outsourced = 'N'"
    		+ " and test_mst.is_histo_cyto = 'S'"
    		+ "  and order_details.ord_cancelled = 'N' "
    		+ "  and order_details.status = 'A' "
    		+ "  and order_details.status = 'A' "
    		+ " and (case when  order_master.visit_type_id not in(1) then (case when bill_mst.billing_master_id  is not null then  bill_mst.bill_cancelled = 'N' else bill_mst.billing_master_id  is null  end )  else bill_mst.billing_master_id  is not null and bill_mst.bill_cancelled = 'N' end)"
    		+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
      ),


    @NamedNativeQuery(name = "GET_SEROLOGY_RESULT_ENTRY_LIST_COUNT",
        query = "select "
        		+ "	count(*) "
        		+ "from "
        		+ "	lab.t_lab_sample_dtls lbsampledtls "
        		+ "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id "
        		+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id "
        		+ "inner join lab.m_test_master test_mst on "
        		+ "	lbsampledtls.test_id = test_mst.test_id "
        		+ "inner join public.t_lab_configuration lab_config on "
        		+ "	lab_config.unit_id = lbsampledtls.org_unit_id "
        		+ "where "
        		+ "	 test_mst.is_outsourced = 'N' "
        		+ "	and lbsampledtls.org_id = :orgId "
        		+ "	and lbsampledtls.org_unit_id = :orgUnitId "
        		+ "	and lbsampledtls.dept_id = lab_config.lab_speciality_id "
        		+ "	and lbsampledtls.sub_dept_id = lab_config.serology_id "
        		+ "	and lbsampledtls.is_outsourced = 'N'"
        		+ "  and order_details.ord_cancelled = 'N' "
        		+ "  and order_details.status = 'A' "
        		+ " and test_mst.is_histo_cyto = 'S'"),
    
    @NamedNativeQuery(name = "GET_SERO_REPORT_ENTRY_DETAILS",
    query = " select "
    		+ "	tech_mst.technique_name as \"techniqueName\", "
    		+ "	test_result.lab_sample_dtls_id as \"labSampleDtlsId\", "
    		+ "	sero_test_mppr.serology_result_test_mppr_id as \"serologyResultTestMpprId\", "
    		+ "	para_mst.parameter_name as \"parameterName\", "
    		+ "	sero_test_mppr.serology_test_result_id as \"serologyTestResultId\", "
    		+ "	sero_test_mppr.first_finding as \"firstFinding\", "
    		+ "	sero_test_mppr.first_finding_desc as \"firstFindingDesc\", "
    		+ "	sero_test_mppr.finding_result_desc as \"desc\", "
    		+ " dtl.is_on_hold_parameter as \"isOnHoldParameter\", "
    		+ "	sero_test_mppr.technique_id as \"techniqueId\", "
    		+ "	emp_mst.first_name || ' ' || emp_mst.last_name as \"resultEntryUser\", "
    		+ "	para_mst.parameter_id as \"parameterId\", "
    		  + " dtl.internal_notes as \"internalNotes\", "
    		+ " test_result.org_id as \"orgId\","
    		+ " test_result.org_unit_id as \"orgUnitId\","
    		+ "	to_char(test_result.result_enter_date, 'DD-MM-YYYY') as \"resultEnterDate\", "
    		+ " sero_test_mppr.is_abnormal as \"isAbnormal\" "
    		+ "from "
    		+ "	lab.t_serology_test_result test_result "
    		+ " inner join lab.t_serology_result_test_mppr sero_test_mppr on "
    		+ "	test_result.serology_test_result_id = sero_test_mppr.serology_test_result_id and sero_test_mppr.status = 'A'"
    		+ " inner join lab.m_technique_master tech_mst on "
    		+ "	sero_test_mppr.technique_id = tech_mst.technique_id "
    		+ " inner join lab.m_parameter_master para_mst on "
    		+ "	sero_test_mppr.parameter_id = para_mst.parameter_id "
    		+ " inner join lab.t_lab_sample_dtls dtl on " + 
    		"	dtl.lab_sample_dtls_id = test_result.lab_sample_dtls_id "
    		+ "left join public.m_employee_master emp_mst on "
    		+ "	test_result.result_enter_by = emp_mst.user_id "
    		+ "where "
    		+ "	test_result.lab_sample_dtls_id =:labSampleDtlsId "
    		+ " and test_result.status ='A'"),
    
    @NamedNativeQuery(name = "GET_SERO_REPORT_VALIDATION_DETAILS",
    query =  " select "
    		+ "	tech_mst.technique_name as \"techniqueName\", "
    		+ "	sero_test_mppr.serology_result_test_mppr_id as \"serologyResultTestMpprId\", "
    		+ "	para_mst.parameter_name as \"parameterName\", "
    		+ "	sero_test_mppr.serology_test_result_id as \"serologyTestResultId\", "
    		+ "	sero_test_mppr.first_finding as \"firstFinding\", "
    		+ "	sero_test_mppr.first_finding_desc as \"firstFindingDesc\", "
    		+ "	sero_test_mppr.second_finding as \"secondFinding\", "
    		+ "	sero_test_mppr.second_finding_desc as \"secondFindingDesc\","
    		+ " sero_test_mppr.finding_result as \"findingResult\" ,"
    		+ " sero_test_mppr.finding_result_desc as \"findingResultDesc\", "
    		+ " dtl.internal_notes as \"internalNotes\", "
    		+ " dtl.is_on_hold_parameter as \"isOnHoldParameter\", "
    		+ "	emp_mst.first_name || ' ' || emp_mst.last_name as \"resultValidatedUser\", "
            + " para_mst.parameter_id as \"parameterId\", "
            + " test_result.org_id as \"orgId\","
            + " test_result.org_unit_id as \"orgUnitId\","
    		+ "	to_char(test_result.result_enter_date, 'DD-MM-YYYY') as \"resultValidatedDate\" "
    		+ "from "
    		+ "	lab.t_serology_test_result test_result "
    		+ " left join lab.t_serology_result_test_mppr sero_test_mppr on "
    		+ "	test_result.serology_test_result_id = sero_test_mppr.serology_test_result_id and sero_test_mppr.status = 'A'"
    		+ " left join lab.m_technique_master tech_mst on "
    		+ "	sero_test_mppr.technique_id = tech_mst.technique_id "
    		+ " inner join lab.m_parameter_master para_mst on "
    		+ "	sero_test_mppr.parameter_id = para_mst.parameter_id "
    		+ " inner join lab.t_lab_sample_dtls dtl on " + 
    		"	dtl.lab_sample_dtls_id = test_result.lab_sample_dtls_id "
    		+ "left join public.m_employee_master emp_mst on "
    		+ "	test_result.result_validated_by = emp_mst.user_id "
    		+ "where "
    		+ "	test_result.lab_sample_dtls_id =:labSampleDtlsId "
    		+ " and test_result.status ='A'")
})

@Entity
@Table(name = "t_serology_test_result", schema = "lab")
@SequenceGenerator(name = "seq_t_serology_test_result", sequenceName = "lab.seq_t_serology_test_result", allocationSize = 1)
public class SerologyTestResult {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_t_serology_test_result")
	@Column(name = "serology_test_result_id")
	private int serologyTestResultId;

	@Column(name = "lab_sample_dtls_id")
	private int labSampleDtlsId;

	@Column(name = "result_enter_by")
	private Integer resultEnterBy;
	
	@Column(name = "result_enter_date")
	private Date resultEnterDate;

	@Column(name = "result_validated_by")
	private Integer resultValidatedBy;
	
	@Column(name = "result_validated_date")
	private Date resultValidatedDate;
	
	@Column(name = "report_release_by")
	private Integer reportReleaseBy;
	
	@Column(name = "report_release_date")
	private Date reportReleaseDate;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "org_unit_id")
	private Integer orgUnitId;
	
	@Column(name = "manual_result_enter_time")
	  private Date manualResultEnterTime;
	  
	  @Column(name = "manual_validation_time")
	  private Date manualValidationTime;
	  
	  @Column(name = "manual_release_time")
	  private Date manualReleaseTime;

	public int getSerologyTestResultId() {
		return serologyTestResultId;
	}

	public void setSerologyTestResultId(int serologyTestResultId) {
		this.serologyTestResultId = serologyTestResultId;
	}

	public int getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(int labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public Integer getResultEnterBy() {
		return resultEnterBy;
	}

	public void setResultEnterBy(Integer resultEnterBy) {
		this.resultEnterBy = resultEnterBy;
	}

	public Date getResultEnterDate() {
		return resultEnterDate;
	}

	public void setResultEnterDate(Date resultEnterDate) {
		this.resultEnterDate = resultEnterDate;
	}

	public Integer getResultValidatedBy() {
		return resultValidatedBy;
	}

	public void setResultValidatedBy(Integer resultValidatedBy) {
		this.resultValidatedBy = resultValidatedBy;
	}

	public Date getResultValidatedDate() {
		return resultValidatedDate;
	}

	public void setResultValidatedDate(Date resultValidatedDate) {
		this.resultValidatedDate = resultValidatedDate;
	}

	public Integer getReportReleaseBy() {
		return reportReleaseBy;
	}

	public void setReportReleaseBy(Integer reportReleaseBy) {
		this.reportReleaseBy = reportReleaseBy;
	}

	public Date getReportReleaseDate() {
		return reportReleaseDate;
	}

	public void setReportReleaseDate(Date reportReleaseDate) {
		this.reportReleaseDate = reportReleaseDate;
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

	public Date getManualResultEnterTime() {
		return manualResultEnterTime;
	}

	public void setManualResultEnterTime(Date manualResultEnterTime) {
		this.manualResultEnterTime = manualResultEnterTime;
	}

	public Date getManualValidationTime() {
		return manualValidationTime;
	}

	public void setManualValidationTime(Date manualValidationTime) {
		this.manualValidationTime = manualValidationTime;
	}

	public Date getManualReleaseTime() {
		return manualReleaseTime;
	}

	public void setManualReleaseTime(Date manualReleaseTime) {
		this.manualReleaseTime = manualReleaseTime;
	}


	
	

}
