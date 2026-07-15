package com.param.billing.packages.model;

import java.util.Date;

import javax.persistence.Column;
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

import com.param.global.model.SpecialityMaster;
import com.param.global.model.SubSpecialityMaster;

@NamedNativeQueries({ @NamedNativeQuery(name = "GET_PACKAGE_CONSUMPTION_CAP_DETAILS", query = "select "
		+ "	pccd.package_consumption_cap_details_id as \"packageConsumptionCapDetailsId\", "
		+ "	pccd.package_consumption_master_id as \"packageConsumptionMasterId\", "
		+ "	pccd.department_id as \"departmentId\", " 
		+ "	pccd.sub_department_id as \"subDepartmentId\", "
		+ "	cast(cast(coalesce(pccd.available_cap, 0) as numeric(15, 10)) as double precision) as \"availableCap\", " 
		+ "	pccd.is_service_item as \"isServiceItem\", "
		+ "	pccd.quantity as \"quantity\", "
		+ "	pccd.product_category_id as \"productCategoryId\" " 
		+ " from "
		+ "	billing.t_package_consumption_cap_details pccd "
		+ "	inner join billing.t_package_consumption_master pmst on pmst.package_consumption_master_id =pccd.package_consumption_master_id "
		+ " where " 
		+ "	pccd.package_consumption_master_id =:packageConsumptionMasterId " + "and pmst.status = 'A' and pccd.status = 'A' "),

		@NamedNativeQuery(name = "GET_PACKAGE_CONS_CAP_DTLS_BY_PACKAGE_ENCOUNTER_ID", query = "select pcap.package_consumption_cap_details_id as \"packageConsumptionCapDetailsId\", "
				+ " pcap.package_consumption_master_id as \"packageConsumptionMasterId\",  "
				+ " pcap.department_id as \"departmentId\", pcap.sub_department_id as \"subDepartmentId\", "
				+ " pcap.is_service_item as \"isServiceItem\",  "
				+ " pcap.product_category_id as \"productCategoryId\", "
				+ " coalesce(pccd.available_cap, 0) as \"availableCap\", " + " pcap.status as \"status\"  "
				+ " from billing.t_package_consumption_cap_details pcap "
				+ " inner join billing.t_package_consumption_master pconsump "
				+ " on pconsump.package_consumption_master_id = pcap.package_consumption_master_id "
				+ " where pconsump.package_id = :packageId and "
				+ " (pconsump.encounter_id = :encounterId and pconsump.visit_type = :visitTypeId) or (pconsump.admission_id = :encounterId and pconsump.visit_type = :visitTypeId)") })

@Entity
@Table(name = "t_package_consumption_cap_details", schema = "billing")
@SequenceGenerator(name = "package_consumption_cap_details_seq", sequenceName = "billing.package_consumption_cap_details_seq", allocationSize = 1)
public class TPackageConsumptionCapDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "package_consumption_cap_details_seq")
	@Column(name = "package_consumption_cap_details_id")
	private int packageConsumptionCapDetailsId;

	@Column(name = "package_consumption_master_id")
	private Integer packageConsumptionMasterId;

	@Column(name = "department_id")
	private Integer departmentId;

	@Column(name = "sub_department_id")
	private Integer subDepartmentId;

	@Column(name = "is_service_item")
	private Integer isServiceItem;

	@Column(name = "product_category_id")
	private Integer productCategoryId;

	@Column(name = "cap_amount")
	private Double capAmount;

	@Column(name = "available_cap")
	private Double availableCap;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "status")
	private char status;

	@Column(name = "organisation_id")
	private Integer organisationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "package_qty")
	private Integer packageQty;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", insertable = false, updatable = false, nullable = false)
	private SpecialityMaster specialityMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_consumption_master_id", insertable = false, updatable = false, nullable = false)
	private TPackageConsumptionMaster tPackageConsumptionMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_department_id", insertable = false, updatable = false, nullable = false)
	private SubSpecialityMaster subSpecialityMaster;

	public int getPackageConsumptionCapDetailsId() {
		return packageConsumptionCapDetailsId;
	}

	public void setPackageConsumptionCapDetailsId(int packageConsumptionCapDetailsId) {
		this.packageConsumptionCapDetailsId = packageConsumptionCapDetailsId;
	}

	public Integer getPackageConsumptionMasterId() {
		return packageConsumptionMasterId;
	}

	public void setPackageConsumptionMasterId(Integer packageConsumptionMasterId) {
		this.packageConsumptionMasterId = packageConsumptionMasterId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getSubDepartmentId() {
		return subDepartmentId;
	}

	public void setSubDepartmentId(Integer subDepartmentId) {
		this.subDepartmentId = subDepartmentId;
	}

	public Integer getIsServiceItem() {
		return isServiceItem;
	}

	public void setIsServiceItem(Integer isServiceItem) {
		this.isServiceItem = isServiceItem;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
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

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public TPackageConsumptionMaster gettPackageConsumptionMaster() {
		return tPackageConsumptionMaster;
	}

	public void settPackageConsumptionMaster(TPackageConsumptionMaster tPackageConsumptionMaster) {
		this.tPackageConsumptionMaster = tPackageConsumptionMaster;
	}

	public SubSpecialityMaster getSubSpecialityMaster() {
		return subSpecialityMaster;
	}

	public void setSubSpecialityMaster(SubSpecialityMaster subSpecialityMaster) {
		this.subSpecialityMaster = subSpecialityMaster;
	}

	public Double getCapAmount() {
		return capAmount;
	}

	public void setCapAmount(Double capAmount) {
		this.capAmount = capAmount;
	}

	public Double getAvailableCap() {
		return availableCap;
	}

	public void setAvailableCap(Double availableCap) {
		this.availableCap = availableCap;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getPackageQty() {
		return packageQty;
	}

	public void setPackageQty(Integer packageQty) {
		this.packageQty = packageQty;
	}
	
	

}
