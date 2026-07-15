package com.param.entity.lis.micro;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.param.entity.lis.common.LocalTimeConverter;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;

@NamedNativeQueries({

  


  @NamedNativeQuery(name = "MACROSCOPIC_EXAMINATION_WORKLIST", query = " SELECT   "
      + "  visit_type_master.visit_type_code AS \"visitType\",   "
      + "  panel_master.panel_code AS \"panelCode\",   "
      + "  panel_master.panel_id AS \"panelId\",   "
      + "  lbsampledtls.gender_id AS \"genderId\",   "
      + "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",   "
      + " test_mst.sample_id AS \"sampleId\", "
      + "  lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\",   "
      + "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\",   "
      + "  pati_mst.uhid_number AS uhid,   "
      + "  pati_mst.image_path AS \"imagePath\",   "
      + "  sample_mst.sample_no AS \"labSampleNo\",  "
      + "  test_mst.test_desc AS \"testDesc\",   "
      + "  order_master.admission_id AS \"visitAdmId\",   "
      + "  order_master.visit_type_id AS \"visitTypeId\",   "
      + "  lbsampledtls.dept_id AS \"deptId\",   " 
      + "  lbsampledtls.test_id AS \"testId\",   "
      + "  test_mst.test_type AS \"testType\",   "
      + "  lbsampledtls.sub_dept_id AS \"subDeptId\",   "
      + "  lbsampledtls.patient_id AS \"patientId\",   "
      + "  lbsampledtls.order_details_id AS \"orderDetailsId\",   "
      + "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) ||' ' || pati_mst.last_name || ' ' || EXTRACT(   "
      + "    YEAR      FROM        age(NOW(), pati_mst.dob)   "
      + "  ) || '/' || gen_mst.gender_code AS \"patientDetails\",   "
      + "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\",   "
      + "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",   "
      + "  lbsampledtls.sample_barcode AS \"sampleBarcode\",   "
      + "  order_details.created_date AS \"orderDateTime\",   "
      + "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\" ,  "
	  + "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",   "
	  + "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDatetimeString\" ,  "
      + "  wardmast.ward_code AS \"wardCode\" ,  " 
      + "  bedmst.bed_code AS \"bedNumber\",   "
      + "    bill_mst.bill_number as \"billNumber\", " 
      + "  test_mst.test_code AS \"testCode\" ,  "
      + "  samp_mst.sample_desc AS \"sampleType\" ,  "
      + "  macroscopic_examination.t_macro_exa_id AS \"tMacroExaId\",   " 
      +" (case when order_master.encounter_id > 0 then " +
      " ( tpsd.payment_entitlement_id ) " +
      " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
      + " stsMstr.status_name  as \"statusName\" ,"
      + "order_details.is_dummy_service as \"isDummyService\" "
      + "FROM   "
      + "  lab.t_lab_sample_dtls lbsampledtls   "
      + "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id   "
      + "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id   "
      + "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
      + "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id   "
      + "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id   "
      + "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id   "
      + "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id   "
      + "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id   "
      + "  INNER JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id   "
      + "  INNER JOIN lab.m_test_master test_master ON test_master.test_id = lbsampledtls.test_id   "
      + "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id   "
      + "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_master.sample_id   "
      + "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id   "
      + "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id   "
      + " left join billing.t_billing_details bill_dtls on " 
      + "     order_details.order_details_id = bill_dtls.order_details_id " 
      + " left join billing.m_billing_master bill_mst on " 
      + "     bill_mst.billing_master_id = bill_dtls.billing_master_id " 
      + "     and bill_mst.bill_cancelled = 'N' " 

      + " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
		+ " left join opd.t_patient_sponsor_details tpsd "
		+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
		+ " left join opd.t_patient_sponsor_details tpsd2 "
		+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
      + "  LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
      + "  and admission_dtls.status = 'A' " 
  	  + "  and admission_dtls.nursing_acceptance = 'Y' "
      +"  INNER JOIN lab.m_test_status_master stsMstr on lbsampledtls.sample_status_id = stsMstr.test_status_id "
      + "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id   "
      + "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id   "
      + "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id   "
      + "  LEFT JOIN lab.t_macroscopic_examination macroscopic_examination ON lbsampledtls.lab_sample_dtls_id = macroscopic_examination.lab_sample_dtls_id   "
      + " left join billing.t_credit_note_detail tcnd on "
      + " tcnd.order_detail_id = order_details.order_details_id "
      + " and tcnd.status = 'A' "
      + " and tcnd.cancel_or_discount = 1 "
      + " WHERE   " 
      + "  lbsampledtls.sample_status_id IN (:sampleStatusIds)    "
      + "  AND lbsampledtls.org_id = :orgId   " 
      + "  AND lbsampledtls.org_unit_id = :orgUnitId   "
      + "  AND lbsampledtls.dept_id = :deptId   "
      + "  AND lbsampledtls.sub_dept_id = :subDeptId   " 
      + "  and order_details.ord_cancelled = 'N' "
      + " and (case when order_master.visit_type_id = 1 then bill_dtls.status = 'A' else order_details.status = 'A' end )"
	  + "  and order_details.status = 'A' "
	  + " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
      +"   AND lbsampledtls.is_outsourced = 'N' "  ),

  /** Sample Pending Acceptance Count */
  @NamedNativeQuery(name = "MACROSCOPIC_EXAMINATION_WORKLIST_COUNT",
      query = "  SELECT  " 
          + "  COUNT(*)  " + "FROM  " 
          + "  lab.t_lab_sample_dtls lbsampledtls  "
          + "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id   "
          + "  INNER JOIN public.t_order_master order_master ON lbsampledtls.order_id = order_master.order_id   "
          + " WHERE  " 
          + "  lbsampledtls.sample_status_id IN (:sampleStatusIds)  "
          + "  AND lbsampledtls.org_id = :orgId  "
          + "  AND lbsampledtls.org_unit_id = :orgUnitId  "
          + "  AND lbsampledtls.dept_id = :deptId  "
          + "  AND lbsampledtls.sub_dept_id = :subDeptId  "
          + "  AND test_mst.is_outsourced = 'N' "),

})


@Entity
@Table(name = "t_macroscopic_examination", schema = "lab")
@SequenceGenerator(name = "t_seq_macroscopic_examination",
    sequenceName = "lab.t_seq_macroscopic_examination", allocationSize = 1)
public class TMacroscopicExaminationMaster {


  @Id
  @Column(name = "t_macro_exa_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_macroscopic_examination")
  private int tMacroExaId;

  @Column(name = "lab_sample_dtls_id")
  private Integer labSampleDtlsId;

  @Column(name = "macro_exa_result")
  private String macroExaResult;

  @Column(name = "is_completed")
  private Character isCompleted;

  @Column(name = "org_id")
  private Integer orgId;

  @Column(name = "org_unit_id")
  private Integer orgUnitId;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lab_sample_dtls_id", insertable = false, nullable = false, updatable = false)
  private LabSampleDetailsMaster labSampleDetailsMaster;

  public int gettMacroExaId() {
    return tMacroExaId;
  }

  public void settMacroExaId(int tMacroExaId) {
    this.tMacroExaId = tMacroExaId;
  }

  public Integer getLabSampleDtlsId() {
    return labSampleDtlsId;
  }

  public void setLabSampleDtlsId(Integer labSampleDtlsId) {
    this.labSampleDtlsId = labSampleDtlsId;
  }

  public String getMacroExaResult() {
    return macroExaResult;
  }

  public void setMacroExaResult(String macroExaResult) {
    this.macroExaResult = macroExaResult;
  }

  public Character getIsCompleted() {
    return isCompleted;
  }

  public void setIsCompleted(Character isCompleted) {
    this.isCompleted = (isCompleted == '\u0000') ? 'N' : isCompleted;
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
    this.isDeleted = (isDeleted == '\u0000') ? 'N' : isDeleted;
  }

  public LabSampleDetailsMaster getLabSampleDetailsMaster() {
    return labSampleDetailsMaster;
  }

  public void setLabSampleDetailsMaster(LabSampleDetailsMaster labSampleDetailsMaster) {
    this.labSampleDetailsMaster = labSampleDetailsMaster;
  }


}
