package com.param.entity.model.pharmacy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.procurement.ImplantIndentDetail;

@Entity(name = "IssueDetail")
@Table(name = "t_issue_detail", schema = "pharmacy")
public class IssueDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_detail_id")
	private SaleDetail saleDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prescription_detail_id")
	private PrescriptionDetail prescriptionDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_id")
	private Issue issue;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_indent_detail_id")
	private PatientIndentDetail patientIndentDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "implant_indent_detail_id")
	private ImplantIndentDetail implantIndentDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assign_picker_detail_id")
	private AssignPickerDetail assignPickerDetail;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "issueDetail", cascade = CascadeType.ALL)
	private List<IssueDetailBatchMapper> issueDetailBatchMapper;

	@Column(name = "issue_quantity")
	private Integer issueQuantity;
	
	@Column(name = "is_from_assign_picker")
	private Boolean isFromAssignPicker;
	
	@Column(name = "is_narcotic_substitute")
	private Boolean isNarcoticSubstitute;
	
	@Column(name = "parent_issue_detail_id")
	private Integer parentIssueDetailId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="batch_id")
	private Batch batch;
	
	@Column(name = "net_amount")
	private Double netAmount;

	@Column(name = "credit_payable")
	private Double creditPayable;

	@Column(name = "self_payable")
	private Double selfPayable;

	@Column(name = "discount_amount")
	private Double discountAmount;

	private Double rate;

	@Column(name = "tax_amount", nullable = false)
	private Double taxAmount = 0.0;

	@Column(name = "tax_percentage")
	private Double taxPercentage;

	@Column(name = "total_amount")
	private Double totalAmount;

	@Column(name = "unit_discount_amount")
	private Double unitDiscountAmount;

	@Column(name = "package_consumption_amt")
	private Double packageConsumptionAmt;

	@Column(name = "consession_amount")
	private Double consessionAmount;

	@Column(name = "unit_consession_amount")
	private Double unitConsessionAmount;
	
	@Column(name = "consession_percentage")
	private Double consessionPercentage;
	
	@Column(name = "batch_id",updatable=false,insertable=false)
	private Integer batchId;

	@Column(name = "is_substitute")
	private Boolean isSubstitute;
	
	public Double getNetAmount() {
		return netAmount;
	}

	public Double getConsessionPercentage() {
		return consessionPercentage;
	}

	public void setConsessionPercentage(Double consessionPercentage) {
		this.consessionPercentage = consessionPercentage;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}

	public Double getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
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

	public Double getUnitDiscountAmount() {
		return unitDiscountAmount;
	}

	public void setUnitDiscountAmount(Double unitDiscountAmount) {
		this.unitDiscountAmount = unitDiscountAmount;
	}

	public Double getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}

	public void setPackageConsumptionAmt(Double packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public Double getConsessionAmount() {
		return consessionAmount;
	}

	public void setConsessionAmount(Double consessionAmount) {
		this.consessionAmount = consessionAmount;
	}

	public Double getUnitConsessionAmount() {
		return unitConsessionAmount;
	}

	public void setUnitConsessionAmount(Double unitConsessionAmount) {
		this.unitConsessionAmount = unitConsessionAmount;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public UomType getUomType() {
		return uomType;
	}

	public void setUomType(UomType uomType) {
		this.uomType = uomType;
	}

	public UomUnit getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(UomUnit uomUnit) {
		this.uomUnit = uomUnit;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="uom_unit_id")
	private UomUnit uomUnit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SaleDetail getSaleDetail() {
		return saleDetail;
	}

	public void setSaleDetail(SaleDetail saleDetail) {
		this.saleDetail = saleDetail;
	}

	public PrescriptionDetail getPrescriptionDetail() {
		return prescriptionDetail;
	}

	public void setPrescriptionDetail(PrescriptionDetail prescriptionDetail) {
		this.prescriptionDetail = prescriptionDetail;
	}

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	public PatientIndentDetail getPatientIndentDetail() {
		return patientIndentDetail;
	}

	public void setPatientIndentDetail(PatientIndentDetail patientIndentDetail) {
		this.patientIndentDetail = patientIndentDetail;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ImplantIndentDetail getImplantIndentDetail() {
		return implantIndentDetail;
	}

	public void setImplantIndentDetail(ImplantIndentDetail implantIndentDetail) {
		this.implantIndentDetail = implantIndentDetail;
	}

	public List<IssueDetailBatchMapper> getIssueDetailBatchMapper() {
		return issueDetailBatchMapper;
	}

	public void setIssueDetailBatchMapper(List<IssueDetailBatchMapper> issueDetailBatchMapper) {
		this.issueDetailBatchMapper = issueDetailBatchMapper;
	}

	public AssignPickerDetail getAssignPickerDetail() {
		return assignPickerDetail;
	}

	public void setAssignPickerDetail(AssignPickerDetail assignPickerDetail) {
		this.assignPickerDetail = assignPickerDetail;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public void addBatch(IssueDetailBatchMapper batchMapper) {
		if (this.issueDetailBatchMapper == null)
			this.issueDetailBatchMapper = new ArrayList<IssueDetailBatchMapper>();

		this.issueDetailBatchMapper.add(batchMapper);
		batchMapper.setIssueDetail(this);
	}

	public IssueDetail() {
		super();
	}

	public IssueDetail(Integer id) {
		super();
		this.id = id;
	}

	public IssueDetail(Integer id, SaleDetail saleDetail, PrescriptionDetail prescriptionDetail, Issue issue) {
		super();
		this.id = id;
		this.saleDetail = saleDetail;
		this.prescriptionDetail = prescriptionDetail;
		this.issue = issue;
	}

	public IssueDetail(PatientIndentDetail patientIndentDetail, Status status) {
		super();
		this.patientIndentDetail = patientIndentDetail;
		this.status = status;
	}

	public IssueDetail(ImplantIndentDetail implantIndentDetail, Status status) {
		super();
		this.implantIndentDetail = implantIndentDetail;
		this.status = status;
	}

	public IssueDetail(AssignPickerDetail assignPickerDetail, PrescriptionDetail prescriptionDetail, Status status) {
		super();
		this.assignPickerDetail = assignPickerDetail;
		this.prescriptionDetail = prescriptionDetail;
		this.status = status;
	}

	public IssueDetail(AssignPickerDetail assignPickerDetail, PrescriptionDetail prescriptionDetail, Status status, Integer issueQuantity) {
		super();
		this.assignPickerDetail = assignPickerDetail;
		this.prescriptionDetail = prescriptionDetail;
		this.status = status;
		this.issueQuantity = issueQuantity;
	}

	public IssueDetail(PrescriptionDetail prescriptionDetail, Status status) {
		super();
		this.prescriptionDetail = prescriptionDetail;
		this.status = status;
	}

	public IssueDetail(SaleDetail saleDetail, PrescriptionDetail prescriptionDetail, Status status) {
		super();
		this.saleDetail = saleDetail;
		this.prescriptionDetail = prescriptionDetail;
		this.status = status;
	}

	public IssueDetail(SaleDetail saleDetail, Issue issue, Status status) {
		super();
		this.saleDetail = saleDetail;
		this.issue = issue;
		this.status = status;
	}

	public Boolean getIsFromAssignPicker() {
		return isFromAssignPicker;
	}

	public void setIsFromAssignPicker(Boolean isFromAssignPicker) {
		this.isFromAssignPicker = isFromAssignPicker;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getParentIssueDetailId() {
		return parentIssueDetailId;
	}

	public void setParentIssueDetailId(Integer parentIssueDetailId) {
		this.parentIssueDetailId = parentIssueDetailId;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
	
	public Boolean getIsSubstitute() {
		return isSubstitute;
	}

	public void setIsSubstitute(Boolean isSubstitute) {
		this.isSubstitute = isSubstitute;
	}

	public Boolean getIsNarcoticSubstitute() {
		return isNarcoticSubstitute;
	}

	public void setIsNarcoticSubstitute(Boolean isNarcoticSubstitute) {
		this.isNarcoticSubstitute = isNarcoticSubstitute;
	}
	/*public void updateNarcoticsDetail(Batch batch2, UomType uomType2, UomUnit uomUnit2, Integer quantity, Status status2) {
		this.batch = batch2;
		this.uomType = uomType2;
		this.uomUnit = uomUnit2;
		this.issueQuantity = quantity;
		this.status = status2;
	}*/

	

}