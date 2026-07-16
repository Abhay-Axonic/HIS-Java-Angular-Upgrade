package com.param.otims.model;

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
	@NamedNativeQuery(name = "GET_OT_COMPLETION_MEDICATION_LIST",
					 query =  "select "
							 + "	otmed.otc_completion_inv_medication_id as \"otcCompletionInvMedicationId\", "
							 + "	otmed.procedure_request_id as \"procedureRequestId\", "
							 + "	otmed.item_id as \"itemId\", "
							 + "	otmed.booked_qty as \"bookedQty\", "
							 + "	otmed.issue_details_batch_mapper_id as \"issueDetailsBatchMpprId\", "
							 + "	otmed.used_qty as \"usedQty\", "
							 + "	otmed.remaining_qty as \"remainingQty\", "
							 + "	otmed.mrp as \"mrp\", "
							 + "	otmed.tax_percentage as \"taxPercentage\", "
							 + "	otmed.total_amount as \"totalAmount\", "
							 + "	cast(otmed.batch_id as int) as \"tempBatchId\", "
							 + "	otmed.uom_type_id as \"uomTypeId\", "
							 + "	otmed.uom_unit_id as \"uomUnitId\", "
							 + "	otmed.product_category_id as \"productCategoryId\", "
							 + "	itm.code as \"itemCode\", "
							 + "	itm.name as \"itemName\", "
							 + "    itm.is_three_uom_applicable as \"isThreeUOMApplicable\", "
							 + "	bth.batch_no as \"batchNo\", "
							 + "	uomtype.code as \"uomType\", "
							 + "	uomunit.code as \"uomUnit\", "
							 + "	proct.asset_category_id as \"assetCategoryId\", "
							 + "	coalesce(otmed.consession_amount,0) as \"consessionAmount\", "
							 + "	coalesce(otmed.unit_consession_amount,0) as \"unitConsessionAmount\", "
							 + "	coalesce(otmed.tax_amount,0) as \"taxAmount\", "
							 + "	coalesce(otmed.consession_percentage,0) as \"consessionPercentage\", "
							 + "	coalesce(otmed.net_amount,0) as \"netAmount\" ,"
							 + "    otmed.from_ot as \"fromOT\" ,"
							 + "   	otmed.allocation_id as \"allocationId\","
							 + "    tax.\"name\"  as \"taxName\" " 
							 + "from "
							 + "	otims.t_otc_completion_inv_medication otmed "
							 + "inner join public.m_item itm on "
							 + "	otmed. item_id = itm.id "
							 + "inner join inventory.t_batch bth on "
							 + "	otmed.batch_id = bth.id "
							 + "inner join public.m_uom_type uomtype on "
							 + "	otmed.uom_type_id = uomtype.id "
							 + "inner join public.m_uom_unit uomunit on "
							 + "	otmed.uom_unit_id = uomunit.id "
							 + "left join public.m_product_category proct on "
							 + "	otmed.product_category_id = proct.id "
							 + " left join public.m_tax tax on "
							 + "  otmed.tax_id = tax.id	"
							 + "where "
							 + "	otmed.procedure_request_id =:id")
})


@Entity
@Table(name = "t_otc_completion_inv_medication" , schema = "otims")
@SequenceGenerator(name = "t_otc_completion_inv_medication_seq" , sequenceName = "otims.t_otc_completion_inv_medication_seq" , allocationSize = 1)
public class OtcCompletionInvMedication {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_otc_completion_inv_medication_seq")
	@Column(name = "otc_completion_inv_medication_id")
	private int otcCompletionInvMedicationId;
	
    @Column(name = "procedure_request_id")
    private Integer procedureRequestId;
	
	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "booked_qty")
	private Integer bookedQty;
	
	@Column(name = "used_qty")
	private Integer usedQty;
	
	@Column(name = "mrp")
	private Double mrp;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "tax_percentage")
	private Double taxPercentage;
	
	@Column(name = "remaining_qty")
	private Integer remainingQty;
	
	@Column(name = "store_id")
	private Integer storeId;
	
	@Column(name = "total_amount")
	private Double totalAmount;
	
	@Column(name = "batch_id")
	private Integer batchId;
	
	@Column(name = "uom_type_id")
	private Integer uomTypeId;
	
	@Column(name = "uom_unit_id")
	private Integer uomUnitId;
	
    @Column(name = "rejected_qty ")
    private Integer rejectedQty ;
    
    @Column(name = "issue_details_batch_mapper_id")
	private Integer issueDetailsBatchMapperId;
    
    @Column(name = "product_category_id")
    private Integer productCategoryId;
    
	// column for billing
	@Column(name = "consession_amount")
	private Double consessionAmount;

	@Column(name = "unit_consession_amount")
	private Double unitConsessionAmount;

	@Column(name = "tax_amount")
	private Double taxAmount;

	@Column(name = "consession_percentage")
	private Double consessionPercentage;	
    
	@Column(name = "net_amount")
	private Double netAmount;

	@Column(name = "tax_id")
	private Integer taxId;
	
	@Column(name = "from_ot")
	private Boolean fromOt; 
	
	@Column(name = "allocation_id")
	private Integer allocationId;
	
	public int getOtcCompletionInvMedicationId() {
		return otcCompletionInvMedicationId;
	}

	public void setOtcCompletionInvMedicationId(int otcCompletionInvMedicationId) {
		this.otcCompletionInvMedicationId = otcCompletionInvMedicationId;
	}

	public Integer getProcedureRequestId() {
		return procedureRequestId;
	}

	public void setProcedureRequestId(Integer procedureRequestId) {
		this.procedureRequestId = procedureRequestId;
	}

	public Integer getRejectedQty() {
		return rejectedQty;
	}

	public void setRejectedQty(Integer rejectedQty) {
		this.rejectedQty = rejectedQty;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getBookedQty() {
		return bookedQty;
	}

	public void setBookedQty(Integer bookedQty) {
		this.bookedQty = bookedQty;
	}

	public Integer getUsedQty() {
		return usedQty;
	}

	public void setUsedQty(Integer usedQty) {
		this.usedQty = usedQty;
	}

	public double getMrp() {
		return mrp;
	}

	public void setMrp(double mrp) {
		this.mrp = mrp;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getRemainingQty() {
		return remainingQty;
	}

	public void setRemainingQty(Integer remainingQty) {
		this.remainingQty = remainingQty;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Integer getIssueDetailsBatchMapperId() {
		return issueDetailsBatchMapperId;
	}

	public void setIssueDetailsBatchMapperId(Integer issueDetailsBatchMapperId) {
		this.issueDetailsBatchMapperId = issueDetailsBatchMapperId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Double getConsessionAmount() {
		return consessionAmount==null?0:consessionAmount;
	}

	public void setConsessionAmount(Double consessionAmount) {
		this.consessionAmount = consessionAmount==null?0:consessionAmount;
	}

	public Double getUnitConsessionAmount() {
		return  unitConsessionAmount==null?0:unitConsessionAmount;
	}

	public void setUnitConsessionAmount(Double unitConsessionAmount) {
		this.unitConsessionAmount =  unitConsessionAmount==null?0:unitConsessionAmount;
	}

	public Double getTaxAmount() {
		return taxAmount==null?0:taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount==null?0:taxAmount;
	}

	public Double getConsessionPercentage() {
		return consessionPercentage==null?0:consessionPercentage;
	}

	public void setConsessionPercentage(Double consessionPercentage) {
		this.consessionPercentage = consessionPercentage==null?0:consessionPercentage;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Boolean getFromOt() {
		return fromOt;
	}

	public void setFromOt(Boolean fromOt) {
		this.fromOt = fromOt;
	}

	public Integer getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(Integer allocationId) {
		this.allocationId = allocationId;
	}
	
}
