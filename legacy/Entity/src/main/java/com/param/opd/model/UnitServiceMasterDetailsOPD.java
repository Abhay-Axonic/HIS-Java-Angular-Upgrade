package com.param.opd.model;

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
import com.param.global.model.ServiceMaster;

@Entity
@Table(name="t_unit_service_master_details", schema="service")
@SequenceGenerator(name="unit_service_master_details_seq", sequenceName="service.unit_service_master_details_seq", allocationSize=1)
public class UnitServiceMasterDetailsOPD {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="unit_service_master_details_seq")
	@Column(name="unit_service_master_details_id")
	private int unitServiceMasterDetailsId;
	
	@Column(name="service_master_id")
	private Integer serviceMasterId;
	
	@Column(name="is_outsource")
	private char isOutsource;
	
	@Column(name="is_package")
	private char isPackage;
	
	@Column(name="is_rate_editable")
	private char isRateEditable;
	
	@Column(name="rate_editble_min_value")
	private double rateEditbleMinValue;
	
	@Column(name="rate_editable_max_value")
	private double rateEditableMaxValue;
	
	@Column(name="is_tax_applicable")
	private char isTaxApplicable;
	
	@Column(name="is_ref_doctor_share")
	private char isRefDoctorShare;
	
	@Column(name="is_discount")
	private char isDiscount;
	
	@Column(name="discount_value")
	private double discountValue;
	
	@Column(name="gst_type_id")
	private Integer gstTypeId;
	
	@Column(name="otc_gst_type_id")
	private Integer otcGstTypeId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="created_date")
	@Convert(converter = DateConverter.class)
	private Long createdDate;
	
	@Column(name="status")
	private char status;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="is_panel")
    private Character isPanel;
	
	@Column(name="updated_date")
	@Convert(converter = DateConverter.class)
	private Long updatedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_master_id", insertable = false, nullable = false, updatable = false)
	private ServiceMaster serviceMaster;
	  
	public int getUnitServiceMasterDetailsId() {
		return unitServiceMasterDetailsId;
	}

	public void setUnitServiceMasterDetailsId(int unitServiceMasterDetailsId) {
		this.unitServiceMasterDetailsId = unitServiceMasterDetailsId;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public char getIsOutsource() {
		return isOutsource;
	}

	public void setIsOutsource(char isOutsource) {
		this.isOutsource = (isOutsource == '\u0000') ? 'I' : isOutsource;
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

	public double getRateEditbleMinValue() {
		return rateEditbleMinValue;
	}

	public void setRateEditbleMinValue(double rateEditbleMinValue) {
		this.rateEditbleMinValue = rateEditbleMinValue;
	}

	public double getRateEditableMaxValue() {
		return rateEditableMaxValue;
	}

	public void setRateEditableMaxValue(double rateEditableMaxValue) {
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

	public double getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(double discountValue) {
		this.discountValue = discountValue;
	}

    public Character getIsPanel() {
    return isPanel;
  }

  public void setIsPanel(Character isPanel) 
  {
    this.isPanel = (isPanel == '\u0000') ? 'N' : isPanel;
  }
	
}
