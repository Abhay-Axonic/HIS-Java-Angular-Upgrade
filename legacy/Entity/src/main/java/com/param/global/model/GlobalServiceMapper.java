package com.param.global.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.DateConverter;

@Entity
@Table(name = "t_global_service_mapper", schema = "service")
@SequenceGenerator(name = "t_global_service_mapper_seq", sequenceName = "service.t_global_service_mapper_seq", allocationSize = 1)
public class GlobalServiceMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_global_service_mapper_seq")
	@Column(name = "global_service_mapper_id")
	private int globalServiceMapperId;

	@Column(name = "service_standard_name")
	private String serviceStandardName;
	
	@Column(name = "service_standard_code")
	private String serviceStandardCode;
	
	@Column(name = "speciality_name")
	private String specialityName;
	
	@Column(name = "sub_speciality_name")
	private String subSpecialityName;

	@Column(name = "is_outsource")
	private char isOutsource;
	
	@Column(name = "is_outsource_job")
	private char isOutsourceJob;

	@Column(name = "is_package")
	private char isPackage;

	@Column(name = "is_rate_editable")
	private char isRateEditable;

	@Column(name = "rate_editble_min_value")
	private BigDecimal rateEditbleMinValue;

	@Column(name = "rate_editable_max_value")
	private BigDecimal rateEditableMaxValue;

	@Column(name = "is_tax_applicable")
	private char isTaxApplicable;

	@Column(name = "is_ref_doctor_share")
	private char isRefDoctorShare;

	@Column(name = "is_discount")
	private char isDiscount;

	@Column(name = "discount_value")
	private BigDecimal discountValue;

	@Column(name = "gst_type_id")
	private Integer gstTypeId;

	@Column(name = "otc_gst_type_id")
	private Integer otcGstTypeId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	@Convert(converter = DateConverter.class)
	private Long createdDate;

	@Column(name = "status")
	private char status;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "is_panel")
	private Character isPanel;

	@Column(name = "is_allow_multiple_quantity")
	private Character isQuantityEditable;

	@Column(name = "updated_date")
	@Convert(converter = DateConverter.class)
	private Long updatedDate;

	@Column(name = "is_procedure")
	private Character isProcedure;

	@Column(name = "is_doc_req")
	private Character isDocReq;

	@Column(name = "is_bed_side")
	private Character isBedSide;

	@Column(name = "is_poc")
	private Character isPoc;

	@Column(name = "is_periodicity")
	private Character isPeriodicity;

	@Column(name = "is_auto_render")
	private Character isAutoRender;

	@Column(name = "is_appointable")
	private Character isAppointable;

	@Column(name = "gender")
	private Integer gender;

	@Column(name = "ref_share_amt")
	private BigDecimal refShareAmt;

	@Column(name = "procedure_type_id")
	private Integer procedureTypeId;
	
	@Column(name = "surgery_grade_id")
	private Integer surgeryGradeId;

	@Column(name = "is_pacs_req")
	private Character isPacsReq;

	@Column(name = "is_review_applicable")
	private Character isReviewApplicable;

	@Column(name = "review_time")
	private int reviewTime;

	@Column(name = "ref_share_per")
	private double refSharePer;

	@Column(name = "product_category_id")
	private Integer productCategoryId;

	@Column(name = "bill_of_material")
	private Character billOfMaterial;
	
	@Column(name = "bed_category_id")
	private Integer bedCategoryId;
	
	@Column(name = "doc_ref_per")
	private double docRefPer;

	@Column(name = "is_cyclic")
	private Character isCyclicFlag;
	
	@Column(name = "sample_collection")
	private Character sampleCollection;
	
	@Column(name = "is_doc_share_per_wise")
	private Character isDocSharePerWise;
	
	@Column(name = "is_ref_share_per_wise")
	private Character isRefSharePerWise;
	
	@Column(name = "lab_auto_mail_exclusion")
	private Character labAutoMailExclusion;

	@Column(name = "pre_requisite")
	private Character preRequisite;
	
	@Column(name = "pre_requisite_details")
	private String preRequisiteDetails;
	
	@Column(name = "billing_bed_category_id")
	private Integer billingBedCategoryId;
	
	@Column(name = "billing_bed_desc")
	private String billingBedDesc;
	
	@Column(name = "rate")
	private double rate;
	
	@Column(name = "tariff_id")
	private Integer tariffId;
	
	@Column(name = "speciality_id")
	private Integer specialityId;
	
	@Column(name = "sub_speciality_id")
	private Integer subSpecialityId;
	
	@Column(name = "service_master_id")
	private Integer serviceMasterId;
	
	@Column(name = "payment_entitlement_id")
	private Integer paymentEntitlementId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "patient_type_id")
	private Integer patientTypeId;
	

	public int getGlobalServiceMapperId() {
		return globalServiceMapperId;
	}

	public void setGlobalServiceMapperId(int globalServiceMapperId) {
		this.globalServiceMapperId = globalServiceMapperId;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getSubSpecialityName() {
		return subSpecialityName;
	}

	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}

	public char getIsOutsource() {
		return isOutsource;
	}

	public void setIsOutsource(char isOutsource) {
		this.isOutsource = isOutsource;
	}

	public char getIsOutsourceJob() {
		return isOutsourceJob;
	}

	public void setIsOutsourceJob(char isOutsourceJob) {
		this.isOutsourceJob = isOutsourceJob;
	}

	public char getIsPackage() {
		return isPackage;
	}

	public void setIsPackage(char isPackage) {
		this.isPackage = isPackage;
	}

	public char getIsRateEditable() {
		return isRateEditable;
	}

	public void setIsRateEditable(char isRateEditable) {
		this.isRateEditable = isRateEditable;
	}

	public BigDecimal getRateEditbleMinValue() {
		return rateEditbleMinValue;
	}

	public void setRateEditbleMinValue(BigDecimal rateEditbleMinValue) {
		this.rateEditbleMinValue = rateEditbleMinValue;
	}

	public BigDecimal getRateEditableMaxValue() {
		return rateEditableMaxValue;
	}

	public void setRateEditableMaxValue(BigDecimal rateEditableMaxValue) {
		this.rateEditableMaxValue = rateEditableMaxValue;
	}

	public char getIsTaxApplicable() {
		return isTaxApplicable;
	}

	public void setIsTaxApplicable(char isTaxApplicable) {
		this.isTaxApplicable = isTaxApplicable;
	}

	public char getIsRefDoctorShare() {
		return isRefDoctorShare;
	}

	public void setIsRefDoctorShare(char isRefDoctorShare) {
		this.isRefDoctorShare = isRefDoctorShare;
	}

	public char getIsDiscount() {
		return isDiscount;
	}

	public void setIsDiscount(char isDiscount) {
		this.isDiscount = isDiscount;
	}

	public BigDecimal getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}

	public Integer getGstTypeId() {
		return gstTypeId;
	}

	public void setGstTypeId(Integer gstTypeId) {
		this.gstTypeId = gstTypeId;
	}

	public Integer getOtcGstTypeId() {
		return otcGstTypeId;
	}

	public void setOtcGstTypeId(Integer otcGstTypeId) {
		this.otcGstTypeId = otcGstTypeId;
	}

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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Character getIsPanel() {
		return isPanel;
	}

	public void setIsPanel(Character isPanel) {
		this.isPanel = isPanel;
	}

	public Character getIsQuantityEditable() {
		return isQuantityEditable;
	}

	public void setIsQuantityEditable(Character isQuantityEditable) {
		this.isQuantityEditable = isQuantityEditable;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Character getIsProcedure() {
		return isProcedure;
	}

	public void setIsProcedure(Character isProcedure) {
		this.isProcedure = isProcedure;
	}

	public Character getIsDocReq() {
		return isDocReq;
	}

	public void setIsDocReq(Character isDocReq) {
		this.isDocReq = isDocReq;
	}

	public Character getIsBedSide() {
		return isBedSide;
	}

	public void setIsBedSide(Character isBedSide) {
		this.isBedSide = isBedSide;
	}

	public Character getIsPoc() {
		return isPoc;
	}

	public void setIsPoc(Character isPoc) {
		this.isPoc = isPoc;
	}

	public Character getIsPeriodicity() {
		return isPeriodicity;
	}

	public void setIsPeriodicity(Character isPeriodicity) {
		this.isPeriodicity = isPeriodicity;
	}

	public Character getIsAutoRender() {
		return isAutoRender;
	}

	public void setIsAutoRender(Character isAutoRender) {
		this.isAutoRender = isAutoRender;
	}

	public Character getIsAppointable() {
		return isAppointable;
	}

	public void setIsAppointable(Character isAppointable) {
		this.isAppointable = isAppointable;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public BigDecimal getRefShareAmt() {
		return refShareAmt;
	}

	public void setRefShareAmt(BigDecimal refShareAmt) {
		this.refShareAmt = refShareAmt;
	}

	public Integer getProcedureTypeId() {
		return procedureTypeId;
	}

	public void setProcedureTypeId(Integer procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
	}

	public Integer getSurgeryGradeId() {
		return surgeryGradeId;
	}

	public void setSurgeryGradeId(Integer surgeryGradeId) {
		this.surgeryGradeId = surgeryGradeId;
	}

	public Character getIsPacsReq() {
		return isPacsReq;
	}

	public void setIsPacsReq(Character isPacsReq) {
		this.isPacsReq = isPacsReq;
	}

	public Character getIsReviewApplicable() {
		return isReviewApplicable;
	}

	public void setIsReviewApplicable(Character isReviewApplicable) {
		this.isReviewApplicable = isReviewApplicable;
	}

	public int getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(int reviewTime) {
		this.reviewTime = reviewTime;
	}

	

	public double getRefSharePer() {
		return refSharePer;
	}

	public void setRefSharePer(double refSharePer) {
		this.refSharePer = refSharePer;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Character getBillOfMaterial() {
		return billOfMaterial;
	}

	public void setBillOfMaterial(Character billOfMaterial) {
		this.billOfMaterial = billOfMaterial;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	

	public double getDocRefPer() {
		return docRefPer;
	}

	public void setDocRefPer(double docRefPer) {
		this.docRefPer = docRefPer;
	}

	public Character getIsCyclicFlag() {
		return isCyclicFlag;
	}

	public void setIsCyclicFlag(Character isCyclicFlag) {
		this.isCyclicFlag = isCyclicFlag;
	}

	public Character getSampleCollection() {
		return sampleCollection;
	}

	public void setSampleCollection(Character sampleCollection) {
		this.sampleCollection = sampleCollection;
	}

	public Character getIsDocSharePerWise() {
		return isDocSharePerWise;
	}

	public void setIsDocSharePerWise(Character isDocSharePerWise) {
		this.isDocSharePerWise = isDocSharePerWise;
	}

	public Character getIsRefSharePerWise() {
		return isRefSharePerWise;
	}

	public void setIsRefSharePerWise(Character isRefSharePerWise) {
		this.isRefSharePerWise = isRefSharePerWise;
	}

	public Character getLabAutoMailExclusion() {
		return labAutoMailExclusion;
	}

	public void setLabAutoMailExclusion(Character labAutoMailExclusion) {
		this.labAutoMailExclusion = labAutoMailExclusion;
	}

	public Character getPreRequisite() {
		return preRequisite;
	}

	public void setPreRequisite(Character preRequisite) {
		this.preRequisite = preRequisite;
	}

	public String getPreRequisiteDetails() {
		return preRequisiteDetails;
	}

	public void setPreRequisiteDetails(String preRequisiteDetails) {
		this.preRequisiteDetails = preRequisiteDetails;
	}

	

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public String getBillingBedDesc() {
		return billingBedDesc;
	}

	public void setBillingBedDesc(String billingBedDesc) {
		this.billingBedDesc = billingBedDesc;
	}

	
	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	
	
}
