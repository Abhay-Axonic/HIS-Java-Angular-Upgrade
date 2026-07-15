package com.param.global.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_BILL_STAT_LIST",
			query="Select bs.organization_id as \"organizationId\","
					+ " bs.unit_id as \"unitId\","
					+ " bs.bed_category_id as \"bedCategoryId\","
					+ " bs.bed_category as \"bedCategory\","
					+ " bs.bed_vacant_count as \"bedVacantCount\","
					+ " bs.houskeeping_count as \"houskeepingCount\","
					+ " bs.occupied_count as \"occupiedCount\" "
					+ "  from public.m_bill_stats bs"
					+ " where bs.organization_id=:organizationId"
					+ " and bs.unit_id=:unitId"
					+ " and bs.bed_category_id is not null"
					),
	
	
	@NamedNativeQuery(name="GET_BILLING_LIST_BY_VISIT_TYPE",
	query="Select DISTINCT(bs.visit_type_id) as \"visitTypeId\", "
			+ " bs.visit_type as \"visitType\" "
			+ "  from public.m_bill_stats bs"
			+ " where bs.visit_type_id IS NOT NULL"
			+ " and bs.organization_id=:organizationId"
			+ " and bs.unit_id=:unitId"),
	
	@NamedNativeQuery(name="GET_BILLING_LIST_BY_VISIT_TYPE_ID",
	query="Select "
			+ " bs.visit_type_id as \"visitTypeId\", "
			+ " bs.visit_type as \"visitType\","
			+ " bs.payment_mode_id as \"paymentModeId\","
			+ " bs.payment_mode as \"paymentMode\", "
			+ " bs.amount as \"amount\" "
			+ " from public.m_bill_stats bs"
			+ " where bs.visit_type_id=:visitTypeId"
			+ " and bs.payment_mode_id IS NOT NULL"
			+ " and bs.organization_id=:organizationId"
			+ " and bs.unit_id=:unitId"

			),
	@NamedNativeQuery(name="GET_OPERATION_COUNT_LIST",
			query=" select "
//					+ " 	coalesce((select ehc_count from m_bill_stats where visit_type_id = :visitTypeId and ehc_count is not null and organization_id=:organizationId and unit_id=:unitId),0) as \"ehcCount\", "
					+ " 	coalesce((select lab_count from m_bill_stats where visit_type_id = :visitTypeId and lab_count is not null and organization_id=:organizationId and unit_id=:unitId),0) as \"labCount\", "
					+ " 	coalesce((select radiology_count  from m_bill_stats where visit_type_id =:visitTypeId and radiology_count is not null and organization_id=:organizationId and unit_id=:unitId),0) as \"radiologyCount\" "
					),
	

	
	/*@NamedNativeQuery(name="GET_ADMISSION_DISCHARGE_DEATH_COUNT",
	query="Select "
			+ " bs.visit_type_id as \"visitTypeId\", "
			+ " bs.visit_type as \"visitType\","
			+ " bs.discharge_count as \"dischargeCount\","
			+ " bs.admission_count as \"admissionCount\", "
			+ " bs.death_count as \"deathCount\" "
			+ " from public.m_bill_stats bs"
			+ " where bs.visit_type_id=:visitTypeId "
			+ " and (bs.discharge_count is not null" 
			+ " or bs.admission_count is not null" 
			+ " or bs.death_count is not null)"
			+ " and bs.visit_type_id != 1 ")*/

          @NamedNativeQuery(name="GET_ADMISSION_DISCHARGE_DEATH_COUNT",
          query=" select "
            + " coalesce((select discharge_count from m_bill_stats where visit_type_id = :visitTypeId and discharge_count is not null and organization_id=:organizationId and unit_id=:unitId),0) as \"dischargeCount\", "
            + " coalesce((select admission_count from m_bill_stats where visit_type_id = :visitTypeId and admission_count is not null and organization_id=:organizationId and unit_id=:unitId),0) as \"admissionCount\", "
            + " coalesce((select death_count  from m_bill_stats where visit_type_id =:visitTypeId and death_count is not null and organization_id=:organizationId and unit_id=:unitId),0) as \"deathCount\" "
			),

	
	@NamedNativeQuery(name="GET_OP_VISITS_COUNT",
	query="Select "
			+ " bs.user_defined_visit_type_id as \"userDefinedVisitTypeId\" "
			+ " from public.m_bill_stats bs "
			+ " where bs.user_defined_visit_type_id is not null"
			+ " and bs.organization_id=:organizationId"
			+ " and bs.unit_id=:unitId"),
	
	@NamedNativeQuery(name="GET_OP_VISITS_COUNT_BY_ID",
	query="Select "
			+ " bs.user_defined_visit_type_id as \"userDefinedVisitTypeId\", "
			+ " bs.totat_pat_count as \"totatPatCount\" "
			+ " from public.m_bill_stats bs "
			+ " where bs.user_defined_visit_type_id=:userDefinedVisitTypeId "
			+ " and bs.organization_id=:organizationId"
			+ " and bs.unit_id=:unitId"),
	
	@NamedNativeQuery(name="GET_FINANCE_DETAILS_COUNT",
	query=" select "
			+ " 	coalesce((select discount_count from m_bill_stats where visit_type_id = :visitTypeId and discount_count is not null and organization_id=:organizationId and unit_id=:unitId),0) as \"discountCount\", "
			+ " 	coalesce((select credit_note_count from m_bill_stats where visit_type_id = :visitTypeId and credit_note_count is not null and organization_id=:organizationId and unit_id=:unitId),0) as \"creditNoteCount\", "
			+ " 	coalesce((select refund_count  from m_bill_stats where visit_type_id =:visitTypeId and refund_count is not null and organization_id=:organizationId and unit_id=:unitId),0) as \"refundCount\", "
			+ " 	coalesce((select insurance_count from m_bill_stats where visit_type_id =:visitTypeId and insurance_count is not null and organization_id=:organizationId and unit_id=:unitId),0) as \"insuranceCount\" , "
			+ " 	coalesce((select corporate_company_count from m_bill_stats where visit_type_id = :visitTypeId and corporate_company_count is not null and organization_id=:organizationId and unit_id=:unitId),0 ) as \"corporateCompanyCount\" "
			)

})


@Entity
@Table(name = "m_bill_stats", schema = "public")
//@SequenceGenerator(name = "bed_master_seq", sequenceName = "adt.bed_master_seq", allocationSize = 1)
public class BillStatModel {
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bed_master_seq")
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "visit_type")
	private String visitType;
	
	@Column(name = "payment_mode_id")
	private Integer paymentModeId;
	
	@Column(name = "payment_mode")
	private String paymentMode;
	
	@Column(name = "amount")
	private BigDecimal amount;
	
	@Column(name = "bed_category_id")
	private Integer bedCategoryId;
	
	@Column(name = "bed_category")
	private Integer bedCategory;
	
	@Column(name = "bed_vacant_count")
	private Integer bed_vacantCount;
	
	@Column(name = "houskeeping_count")
	private Integer houskeepingCount;
	
	@Column(name = "occupied_count")
	private Integer occupiedCount;
	
	@Column(name = "procedure_count")
	private Integer procedureCount;
	
	@Column(name = "ehc_count")
	private Integer ehcCount;
	
	@Column(name = "lab_count")
	private Integer labCount;
	
	@Column(name = "radiology_count")
	private Integer radiologyCount;
	
	
	@Column(name = "discharge_count")
	private Integer dischargeCount;
	
	@Column(name = "admission_count")
	private Integer admissionCount;
	
	@Column(name = "death_count")
	private Integer deathCount;
	
	@Column(name = "user_defined_visit_type_id")
	private Integer userDefinedVisitTypeId;

	@Column(name = "totat_pat_count")
	private Integer totatPatCount;
	
	@Column(name = "credit_note_count")
	private Integer creditNoteCount;
	
	@Column(name = "discount_count")
	private Integer discountCount;
	
	@Column(name = "insurance_count")
	private Integer insuranceCount;
	
	@Column(name = "corporate_company_count")
	private Integer corporateCompanyCount;
	
	@Column(name = "refund_count")
	private Integer refundCount;
	

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

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}


	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Integer getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Integer paymentModeId) {
		this.paymentModeId = paymentModeId;
	}
	
	public String getPaymentMode() {
		return paymentMode;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public Integer getBedCategory() {
		return bedCategory;
	}

	public void setBedCategory(Integer bedCategory) {
		this.bedCategory = bedCategory;
	}

	public Integer getBed_vacantCount() {
		return bed_vacantCount;
	}

	public void setBed_vacantCount(Integer bed_vacantCount) {
		this.bed_vacantCount = bed_vacantCount;
	}

	public Integer getHouskeepingCount() {
		return houskeepingCount;
	}

	public void setHouskeepingCount(Integer houskeepingCount) {
		this.houskeepingCount = houskeepingCount;
	}

	public Integer getOccupiedCount() {
		return occupiedCount;
	}

	public void setOccupiedCount(Integer occupiedCount) {
		this.occupiedCount = occupiedCount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getProcedureCount() {
		return procedureCount;
	}

	public void setProcedureCount(Integer procedureCount) {
		this.procedureCount = procedureCount;
	}

	public Integer getEhcCount() {
		return ehcCount;
	}

	public void setEhcCount(Integer ehcCount) {
		this.ehcCount = ehcCount;
	}

	public Integer getLabCount() {
		return labCount;
	}

	public void setLabCount(Integer labCount) {
		this.labCount = labCount;
	}

	public Integer getRadiologyCount() {
		return radiologyCount;
	}

	public void setRadiologyCount(Integer radiologyCount) {
		this.radiologyCount = radiologyCount;
	}
	
	

	public Integer getDischargeCount() {
		return dischargeCount;
	}

	public void setDischargeCount(Integer dischargeCount) {
		this.dischargeCount = dischargeCount;
	}

	public Integer getAdmissionCount() {
		return admissionCount;
	}

	public void setAdmissionCount(Integer admissionCount) {
		this.admissionCount = admissionCount;
	}

	public Integer getDeathCount() {
		return deathCount;
	}

	public void setDeathCount(Integer deathCount) {
		this.deathCount = deathCount;
	}
	
	public Integer getUserDefinedVisitTypeId() {
		return userDefinedVisitTypeId;
	}

	public void setUserDefinedVisitTypeId(Integer userDefinedVisitTypeId) {
		this.userDefinedVisitTypeId = userDefinedVisitTypeId;
	}

	public Integer getTotatPatCount() {
		return totatPatCount;
	}

	public void setTotatPatCount(Integer totatPatCount) {
		this.totatPatCount = totatPatCount;
	}
	
	public Integer getCreditNoteCount() {
		return creditNoteCount;
	}

	public void setCreditNoteCount(Integer creditNoteCount) {
		this.creditNoteCount = creditNoteCount;
	}

	public Integer getDiscountCount() {
		return discountCount;
	}

	public void setDiscountCount(Integer discountCount) {
		this.discountCount = discountCount;
	}

	public Integer getInsuranceCount() {
		return insuranceCount;
	}

	public void setInsuranceCount(Integer insuranceCount) {
		this.insuranceCount = insuranceCount;
	}

	public Integer getCorporateCompanyCount() {
		return corporateCompanyCount;
	}

	public void setCorporateCompanyCount(Integer corporateCompanyCount) {
		this.corporateCompanyCount = corporateCompanyCount;
	}

	public Integer getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(Integer refundCount) {
		this.refundCount = refundCount;
	}
	
}
