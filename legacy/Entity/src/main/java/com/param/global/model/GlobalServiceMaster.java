package com.param.global.model;

import java.math.BigDecimal;

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

import com.param.global.common.DateConverter;


@NamedNativeQueries({
	
	@NamedNativeQuery(name = "GET_SPECIALITY_NAME_FROM_GLOBAL_SERVICE_MASTER", 
			query = " select distinct (gsm.speciality_name ) as \"specialityName\" " + 
					" from service.m_global_service_master gsm order by gsm.speciality_name asc"),
	
	
	@NamedNativeQuery(name = "GET_SUB_SPECIALITY_NAME_FROM_GLOBAL_SERVICE_MASTER", 
	query = " select distinct (sub_speciality_name  ) as \"subSpecialityName\", " + 
			" from service.m_global_service_master " + 
			 " where speciality_name in (:specialityNameList) "),
})
@Entity
@Table(name = "m_global_service_master", schema = "service")
@SequenceGenerator(name = "m_global_service_master_seq", sequenceName = "service.m_global_service_master_seq", allocationSize = 1)
public class GlobalServiceMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_global_service_master_seq")
	@Column(name = "global_service_master_id")
	private int globalServiceMasterId;

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
	private BigDecimal refSharePer;

	@Column(name = "product_category_id")
	private Integer productCategoryId;

	@Column(name = "bill_of_material")
	private Character billOfMaterial;
	
	@Column(name = "bed_category_id")
	private Integer bedCategoryId;
	
	@Column(name = "doc_ref_per")
	private BigDecimal docRefPer;

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

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_master_id", insertable = false, updatable = false, nullable = false)
	private ServiceMaster serviceMaster;

	public Character getIsRefSharePerWise() {
		return isRefSharePerWise;
	}

	public void setIsRefSharePerWise(Character isRefSharePerWise) {
		this.isRefSharePerWise = isRefSharePerWise;
	}

	public Character getIsDocSharePerWise() {
		return isDocSharePerWise;
	}

	public void setIsDocSharePerWise(Character isDocSharePerWise) {
		this.isDocSharePerWise = isDocSharePerWise;
	}

	public BigDecimal getRefSharePer() {
		return refSharePer;
	}

	public void setRefSharePer(BigDecimal refSharePer) {
		this.refSharePer = refSharePer;
	}

	public BigDecimal getRefShareAmt() {
		return refShareAmt;
	}

	public void setRefShareAmt(BigDecimal refShareAmt) {
		this.refShareAmt = refShareAmt;
	}

	public Character getIsAppointable() {
		return isAppointable;
	}

	public void setIsAppointable(Character isAppointable) {
		this.isAppointable = (isAppointable == '\u0000') ? 'N' : isAppointable;
	}

	public Character getIsQuantityEditable() {
		return isQuantityEditable;
	}

	public void setIsQuantityEditable(Character isQuantityEditable) {
		this.isQuantityEditable = (isQuantityEditable == '\u0000') ? 'N' : isQuantityEditable;
	}


	public char getIsOutsourceJob() {
		return isOutsourceJob;
	}

	public void setIsOutsourceJob(char isOutsourceJob) {
		this.isOutsourceJob = (isOutsourceJob == '\u0000') ? 'N' : isOutsourceJob;
	}

	public char getIsOutsource() {
		return isOutsource;
	}

	public void setIsOutsource(char isOutsource) {
		this.isOutsource = (isOutsource == '\u0000') ? 'N' : isOutsource;
	}

	public char getIsPackage() {
		return isPackage;
	}

	public void setIsPackage(char isPackage) {
		this.isPackage = (isPackage == '\u0000') ? 'I' : isPackage;
	}

	public char getIsRateEditable() {
		return isRateEditable;
	}

	public void setIsRateEditable(char isRateEditable) {
		this.isRateEditable = (isRateEditable == '\u0000') ? 'I' : isRateEditable;
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
		this.isTaxApplicable = (isTaxApplicable == '\u0000') ? 'I' : isTaxApplicable;
	}

	public char getIsRefDoctorShare() {
		return isRefDoctorShare;
	}

	public void setIsRefDoctorShare(char isRefDoctorShare) {
		this.isRefDoctorShare = (isRefDoctorShare == '\u0000') ? 'I' : isRefDoctorShare;
	}

	public char getIsDiscount() {
		return isDiscount;
	}

	public void setIsDiscount(char isDiscount) {
		this.isDiscount = (isDiscount == '\u0000') ? 'I' : isDiscount;
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
		this.status = (status == '\u0000') ? 'I' : status;
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

	public BigDecimal getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}

	public Character getIsPanel() {
		return isPanel;
	}

	public void setIsPanel(Character isPanel) {
		this.isPanel = (isPanel == '\u0000') ? 'I' : isPanel;
	}

	public Character getIsProcedure() {
		return isProcedure;
	}

	public void setIsProcedure(Character isProcedure) {
		this.isProcedure = (isProcedure == '\u0000') ? 'I' : isProcedure;
	}

	public Character getIsDocReq() {
		return isDocReq;
	}

	public void setIsDocReq(Character isDocReq) {
		this.isDocReq = (isDocReq == '\u0000') ? 'I' : isDocReq;
	}

	public Character getIsBedSide() {
		return isBedSide;
	}

	public void setIsBedSide(Character isBedSide) {
		this.isBedSide = (isBedSide == '\u0000') ? 'I' : isBedSide;
	}

	public Character getIsPoc() {
		return isPoc;
	}

	public void setIsPoc(Character isPoc) {
		this.isPoc = (isPoc == '\u0000') ? 'I' : isPoc;
	}

	public Character getIsPeriodicity() {
		return isPeriodicity;
	}

	public void setIsPeriodicity(Character isPeriodicity) {
		this.isPeriodicity = (isPeriodicity == '\u0000') ? 'I' : isPeriodicity;
	}

	public ServiceMaster getServiceMaster() {
		return serviceMaster;
	}

	public void setServiceMaster(ServiceMaster serviceMaster) {
		this.serviceMaster = serviceMaster;
	}

	public Character getIsAutoRender() {
		return isAutoRender;
	}

	public void setIsAutoRender(Character isAutoRender) {
		this.isAutoRender = isAutoRender;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Integer getProcedureTypeId() {
		return procedureTypeId;
	}

	public void setProcedureTypeId(Integer procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
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
		this.isReviewApplicable = (isReviewApplicable == null) ? 'N' : isReviewApplicable;
	}

	public int getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(int reviewTime) {
		this.reviewTime = reviewTime;
	}

	public Character getBillOfMaterial() {
		return billOfMaterial;
	}

	public void setBillOfMaterial(Character billOfMaterial) {
		this.billOfMaterial = billOfMaterial;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	
	public Character getIsCyclicFlag() {
		return isCyclicFlag;
	}

	public void setIsCyclicFlag(Character isCyclicFlag) {
		this.isCyclicFlag = (isCyclicFlag == null) ? 'N' : isCyclicFlag;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public BigDecimal getDocRefPer() {
		return docRefPer;
	}

	public void setDocRefPer(BigDecimal docRefPer) {
		this.docRefPer = docRefPer;
	}

	public Character getSampleCollection() {
		return sampleCollection;
	}

	public void setSampleCollection(Character sampleCollection) {
		this.sampleCollection = sampleCollection;
	}

	public Integer getSurgeryGradeId() {
		return surgeryGradeId;
	}

	public void setSurgeryGradeId(Integer surgeryGradeId) {
		this.surgeryGradeId = surgeryGradeId;
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

	public int getGlobalServiceMasterId() {
		return globalServiceMasterId;
	}

	public void setGlobalServiceMasterId(int globalServiceMasterId) {
		this.globalServiceMasterId = globalServiceMasterId;
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

	
}
