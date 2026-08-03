package com.param.billing.global.model;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_patient_coupon_template_mapper", schema = "billing")
@SequenceGenerator(name = "patient_coupon_template_mapper_seq", sequenceName = "billing.patient_coupon_template_mapper_seq", allocationSize = 1)
public class PatientCouponTemplateMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_coupon_template_mapper_seq")
	@Column(name = "patient_coupon_template_mapper_id")
	private Integer patientCouponTemplateMapperId;
	
	@Column(name = "coupon_template_details_id")
	private Integer couponTemplateDetailsId;
	
	@Column(name = "coupon_template_master_id")
	private Integer couponTemplateMasterId; 
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "consume")
	private Boolean consume;
	
	@Column(name="speciality_id")
	private Integer specialityId;
	
	@Column(name="sub_speciality_id")
	private Integer subSpecialityId;
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="percentage")
	private Double percentage;
	
	@Column(name="coupon_template_description")
	private String couponTemplateDescription; 
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="status")
	private char status;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="coupon_code")
	private String couponCode; 
	
	@Column(name="coupon_qr_url")
	private String couponQrUrl; 
	
	@Column(name="expiry_days")
	private Integer expireDays;
	
	@Column(name="amount")
	private Double amount;
	
	@Column(name="consumption")
	private Character consumption; 
	
	@Column(name="order_details_id")
	private Integer orderDetailsId;

	public Integer getPatientCouponTemplateMapperId() {
		return patientCouponTemplateMapperId;
	}

	public void setPatientCouponTemplateMapperId(Integer patientCouponTemplateMapperId) {
		this.patientCouponTemplateMapperId = patientCouponTemplateMapperId;
	}

	public Integer getCouponTemplateDetailsId() {
		return couponTemplateDetailsId;
	}

	public void setCouponTemplateDetailsId(Integer couponTemplateDetailsId) {
		this.couponTemplateDetailsId = couponTemplateDetailsId;
	}

	public Integer getCouponTemplateMasterId() {
		return couponTemplateMasterId;
	}

	public void setCouponTemplateMasterId(Integer couponTemplateMasterId) {
		this.couponTemplateMasterId = couponTemplateMasterId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Boolean getConsume() {
		return consume;
	}

	public void setConsume(Boolean consume) {
		this.consume = consume;
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

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public String getCouponTemplateDescription() {
		return couponTemplateDescription;
	}

	public void setCouponTemplateDescription(String couponTemplateDescription) {
		this.couponTemplateDescription = couponTemplateDescription;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getCouponQrUrl() {
		return couponQrUrl;
	}

	public void setCouponQrUrl(String couponQrUrl) {
		this.couponQrUrl = couponQrUrl;
	}

	public Integer getExpireDays() {
		return expireDays;
	}

	public void setExpireDays(Integer expireDays) {
		this.expireDays = expireDays;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Character getConsumption() {
		return consumption;
	}

	public void setConsumption(Character consumption) {
		this.consumption = consumption;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	
}
