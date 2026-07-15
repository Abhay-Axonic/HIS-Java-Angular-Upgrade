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

import com.param.global.model.ServiceMaster;

@NamedNativeQueries({
		@NamedNativeQuery(name = "GET_SERVICE_DETAILS_BY_PACKCONSUMP_SERVICE_ID", query = "select pcsd.pacakge_consumption_service_detail_id as \"pacakgeConsumptionServiceDetailId\", pcsd.package_consumption_master_id as \"packageConsumptionMasterId\", "
				+ " pcsd.service_id as \"serviceId\", pcsd.package_quantity as \"packageQuantity\", pcsd.balance_package_quantity as \"balancePackageQuantity\",  packcon.package_type_id as \"packageTypeId\" ,"
				+ " cast(pcsd.apportioned_price as double precision) as \"apportionedPrice\", pcsd.is_service_item as \"isServiceItem\", pcsd.item_id as \"itemId\",  "
				+ " pcsd.package_eo_detail_id as \"packageEoDetailId\", pcsd.is_inclusive_exclusive as \"isInclusiveExclusive\", packcon.package_id as \"packageId\", "
				+ " srv.service_standard_code as \"serviceCode\", srv.service_standard_name as \"serviceName\", spc.speciality_name as \"specialityName\",  "
				+ " spc.speciality_id as \"specialityId\", subspc.sub_speciality_id as \"subSpecialityId\", subspc.sub_speciality_name as \"subSpecialityName\", cast('Y' as char) as \"isPackageService\" "
				+ " from billing.t_package_consumption_service_detail pcsd  "
				+ " inner join billing.t_package_consumption_master packcon "
				+ " on packcon.package_consumption_master_id = pcsd.package_consumption_master_id "
				+ " inner join  service.m_service_master srv " + " on srv.service_master_id = pcsd.service_id "
				+ " inner join  doctor.m_speciality_master spc " + " on spc.speciality_id = srv.speciality_id  "
				+ " inner join  doctor.m_sub_speciality_master subspc "
				+ " on subspc.sub_speciality_id = srv.sub_speciality_id "
				+ " where pcsd.package_consumption_master_id = :packageConsumptionId and pcsd.is_service_item = 1 "
				+ " and pcsd.service_id = :serviceId "),

		@NamedNativeQuery(name = "GET_SERVICE_DETAILS_BY_PACKCONSUMP_ITEM_ID", query = " select "
				+ "		pcsd.pacakge_consumption_service_detail_id as \"pacakgeConsumptionServiceDetailId\", "
				+ "		pcsd.package_consumption_master_id as \"packageConsumptionMasterId\", "
				+ "		pcsd.package_quantity as \"packageQuantity\", "
				+ "		pcsd.balance_package_quantity as \"balancePackageQuantity\", "
				+ "		packcon.package_type_id as \"packageTypeId\" , "
				+ "		cast(pcsd.apportioned_price as double precision) as \"apportionedPrice\", "
				+ "		pcsd.is_service_item as \"isServiceItem\", " + "		pcsd.item_id as \"itemId\", "
				+ "		pcsd.package_eo_detail_id as \"packageEoDetailId\", "
				+ "		pcsd.is_inclusive_exclusive as \"isInclusiveExclusive\", "
				+ "		packcon.package_id as \"packageId\", " + "		cast('Y' as char) as \"isPackageService\" "
				+ "	from " + "		billing.t_package_consumption_service_detail pcsd "
				+ "	inner join billing.t_package_consumption_master packcon on "
				+ "		packcon.package_consumption_master_id = pcsd.package_consumption_master_id "
				+ "	inner join public.m_item item on  item.id = pcsd.item_id " + "	where "
				+ "		pcsd.package_consumption_master_id = :packageConsumptionId "
				+ "		and pcsd.is_service_item = 2 " + "		and pcsd.item_id = :itemId"),

		@NamedNativeQuery(name = "UPDATE_PACKAGE_CONSUMPTION_SERVICE_QTY", query = "update billing.t_package_consumption_service_detail as pcsd "
				+ " set balance_package_quantity=(pcsd.balance_package_quantity + :qty)"
				+ " from billing.t_package_consumption_master as pcm "
				+ " where pcsd.package_consumption_master_id = pcm.package_consumption_master_id"
				+ " and  pcm.package_id =:packageId " + " and  pcm.patient_id =:patientId"
				+ " and  ((pcm.encounter_id =:encounterId and visit_type=:visitTypeId) or (pcm.admission_id =:encounterId and visit_type=:visitTypeId)) "
				+ " and  pcsd.service_id =:serviceId"),

		@NamedNativeQuery(name = "UPDATE_PACKAGE_CONSUMPTION_SERVICE_QTY_ME", query = "update billing.t_package_consumption_service_detail as pcsd "
				+ " set balance_package_quantity=(pcsd.balance_package_quantity + :qty)"
				+ " from billing.t_package_consumption_master as pcm "
				+ " where pcsd.package_consumption_master_id = pcm.package_consumption_master_id"
				+ " and  pcm.package_id =:packageId " + " and  pcm.patient_id =:patientId"
				+ " and  pcm.visit_type =:visitTypeId  " + " and  pcsd.service_id =:serviceId"),

		@NamedNativeQuery(name = "UPDATE_PACKAGE_CONSUMPTION_ITEM_QTY_ME", query = "update billing.t_package_consumption_service_detail as pcsd "
				+ " set balance_package_quantity=(pcsd.balance_package_quantity + :qty)"
				+ " from billing.t_package_consumption_master as pcm "
				+ " where pcsd.package_consumption_master_id = pcm.package_consumption_master_id"
				+ " and  pcm.package_id =:packageId " + " and  pcm.patient_id =:patientId"
				+ " and  pcm.visit_type =:visitTypeId  " + " and  pcsd.item_id =:itemId"),

		@NamedNativeQuery(name = "UPDATE_PACKAGE_CONUMPTION_EO_BALANCE_QTY", query = "update billing.t_package_consumption_eo_details as pceod"
				+ " set balance_quantity = (pceod.balance_quantity + :qty)"
				+ " from billing.t_package_consumption_master as pcm"
				+ " inner join billing.t_package_consumption_service_detail pcsd on pcm.package_consumption_master_id = pcsd.package_consumption_master_id"
				+ " where pceod.package_consumption_master_id = pcm.package_consumption_master_id"
				+ " and pceod.pacakge_eighteror_group_id = pcsd.package_eo_detail_id"
				+ " and  pcm.package_id =:packageId " + " and  pcm.patient_id =:patientId"
				+ " and  (pcm.encounter_id =:encounterId and visit_type=:visitTypeId) or (pcm.admission_id =:encounterId and visit_type=:visitTypeId) "
				+ " and  pcsd.service_id =:serviceId"),

		@NamedNativeQuery(name = "UPDATE_PACKAGE_CONSUMPTION_DEPT_CAP_BALANCE", query = "update billing.t_package_consumption_cap_details pccd"
				+ " set sub_department_balance_cap_amount = (pccd.department_balance_cap_amount + :serviceAmount)"
				+ " from  billing.t_package_consumption_master pcm"
				+ " where pccd.package_consumption_master_id  = pcm.package_consumption_master_id"
				+ " and  pcm.package_id =:packageId " + " and  pcm.patient_id =:patientId "
				+ " and  pcm.encounter_id =:encounterId " + " and  pccd.department_id =:departmentId "),

		@NamedNativeQuery(name = "UPDATE_PACKAGE_CONSUMPTION_SUBDEPT_CAP_BALANCE", query = "update billing.t_package_consumption_cap_details pccd"
				+ " set sub_department_balance_cap_amount = (pccd.sub_department_balance_cap_amount + :serviceAmount)"
				+ " from  billing.t_package_consumption_master pcm"
				+ " where pccd.package_consumption_master_id  = pcm.package_consumption_master_id"
				+ " and  pcm.package_id =:packageId " + " and  pcm.patient_id =:patientId "
				+ " and  (pcm.encounter_id =:encounterId and visit_type=:visitTypeId) or (pcm.admission_id =:encounterId and visit_type=:visitTypeId) "
				+ " and  pccd.department_id =:departmentId " + " and  pccd.sub_department_id =:subDepartmentId "),

		@NamedNativeQuery(name = "GET_PACKAGE_SERVICES_BY_PACKAGE_ENCOUNTER_ID", query = "select pcsd.pacakge_consumption_service_detail_id as \"pacakgeConsumptionServiceDetailId\", pcsd.package_consumption_master_id as \"packageConsumptionMasterId\", "
				+ " pcsd.service_id as \"serviceId\", pcsd.package_quantity as \"packageQuantity\", pcsd.balance_package_quantity as \"balancePackageQuantity\",  "
				+ " cast(pcsd.apportioned_price as double precision) as \"apportionedPrice\", pcsd.is_service_item as \"isServiceItem\", pcsd.item_id as \"itemId\",  "
				+ " pcsd.package_eo_detail_id as \"packageEoDetailId\", pcsd.is_inclusive_exclusive as \"isInclusiveExclusive\",  "
				+ " srv.service_standard_code as \"serviceCode\", srv.service_standard_name as \"serviceName\", spc.speciality_name as \"specialityName\",  "
				+ " spc.speciality_id as \"specialityId\", subspc.sub_speciality_id as \"subSpecialityId\", subspc.sub_speciality_name as \"subSpecialityName\" "
				+ " from billing.t_package_consumption_service_detail pcsd   "
				+ " inner join  service.m_service_master srv  " + " on srv.service_master_id = pcsd.service_id  "
				+ " inner join  doctor.m_speciality_master spc  " + " on spc.speciality_id = srv.speciality_id   "
				+ " inner join  doctor.m_sub_speciality_master subspc  "
				+ " on subspc.sub_speciality_id = srv.sub_speciality_id  "
				+ " inner join  billing.t_package_consumption_master pcm "
				+ " on pcsd.package_consumption_master_id = pcm.package_consumption_master_id "
				+ " where (pcm.encounter_id = :encounterId and pcm.visit_type=:visitTypeId) or (pcm.admission_id = :encounterId and pcm.visit_type=:visitTypeId) "
				+ " and pcsd.is_service_item = 1  " + " and pcm.package_id = :packageId "),

		@NamedNativeQuery(name = "UPDATE_PACKAGE_CONSUMPTION_QTY_MULTI_ENC", query = "update billing.t_package_consumption_service_detail as pcsd   "
				+ "set balance_package_quantity=(pcsd.balance_package_quantity + :qty) "
				+ "from billing.t_package_consumption_master as pcm  "
				+ "where pcsd.package_consumption_master_id = pcm.package_consumption_master_id "
				+ "and  pcm.package_id =:packageId  "
				+ "and  pcm.patient_id =:patientId  and pcm.visit_type=:visitTypeId "
				+ "and  pcm.package_type_id = 5 and to_date(to_char(now(),'DD-MM-YYYY'),'DD-MM-YYYY') between pcm.validity_from and pcm.validity_to  "
				+ "and  pcsd.service_id =:serviceId "),

		@NamedNativeQuery(name = "UPDATE_PACKAGE_ITEM_CONSUMPTION_QTY_MULTI_ENC", query = "update billing.t_package_consumption_service_detail as pcsd   "
				+ "set balance_package_quantity=(pcsd.balance_package_quantity + :qty) "
				+ "from billing.t_package_consumption_master as pcm  "
				+ "where pcsd.package_consumption_master_id = pcm.package_consumption_master_id "
				+ "and  pcm.package_id =:packageId  "
				+ "and  pcm.patient_id =:patientId  and pcm.visit_type=:visitTypeId "
				+ "and  pcm.package_type_id = 5 and to_date(to_char(now(),'DD-MM-YYYY'),'DD-MM-YYYY') between pcm.validity_from and pcm.validity_to  "
				+ "and  pcsd.item_id =:itemId "),

		@NamedNativeQuery(name = "PACKAGE_CONSUMPTION_LOG", query = " select * from  billing.package_consumption_service_order_log(:packageId,:patientId,:serviceId,:visitTypeId,:createdBy,:createdDate,:isOrderedOrCancellted);"),

		@NamedNativeQuery(name = "PACKAGE_CONSUMPTION_ITEMS_LOG", query = " select * from  billing.package_consumption_item_order_log(:packageId,:patientId,:itemId,:visitTypeId,:createdBy,:createdDate,:isOrderedOrCancellted);"),

		@NamedNativeQuery(name = "PACKAGE_CONSUMPTION_CANCEL_LOG", query = " select * from  billing.package_consumption_service_cancel_log(:packageId,:patientId,:serviceId,:visitTypeId,:visitId,:createdBy,:createdDate,:isOrderedOrCancellted);"),

		@NamedNativeQuery(name = "PACKAGE_CONSUMPTION_CANCEL_ME_LOG", query = " select * from  billing.package_consumption_service_me_cancel_log(:packageId,:patientId,:serviceId,:visitTypeId,:visitId,:createdBy,:createdDate,:isOrderedOrCancellted);"),

		@NamedNativeQuery(name = "PACKAGE_CONSUMPTION_CANCEL_ITEM_ME_LOG", query = " select * from  billing.package_consumption_item_me_cancel_log(:packageId,:patientId,:itemId,:visitTypeId,:visitId,:createdBy,:createdDate,:isOrderedOrCancellted);"),

		@NamedNativeQuery(name = "UPDATE_MULTI_ENC_VISIT", query = "update "
				+ "	billing.t_package_consumption_master " + "set "
				+ "	applicable_no_of_visit = applicable_no_of_visit - :visit, " + "	encounter_id = :encounterId, "
				+ "	is_visited = true " + "where " + "	package_consumption_master_id = :packageConsumptionMasterId"),

})
@Entity
@Table(name = "t_package_consumption_service_detail", schema = "billing")
@SequenceGenerator(name = "package_consumption_service_detail_seq", sequenceName = "billing.package_consumption_service_detail_seq", allocationSize = 1)
public class TPackageConsumptionServiceDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "package_consumption_service_detail_seq")
	@Column(name = "pacakge_consumption_service_detail_id")
	private int pacakgeConsumptionServiceDetailId;

	@Column(name = "package_consumption_master_id")
	private Integer packageConsumptionMasterId;

	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "package_quantity")
	private Integer packageQuantity;

	@Column(name = "balance_package_quantity")
	private Integer balancePackageQuantity;

	@Column(name = "apportioned_price")
	private Double apportionedPrice;

	@Column(name = "is_service_item")
	private Integer isServiceItem;

	@Column(name = "item_id")
	private Integer itemId;

	@Column(name = "package_eo_detail_id")
	private Integer packageEoDetailId;

	@Column(name = "is_inclusive_exclusive")
	private int isInclusiveExclusive;

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

	@Column(name = "status")
	private char status;

	@Column(name = "is_cap")
	private int isCap;

	@Column(name = "available_cap")
	private Double availableCap;

	@Column(name = "service_cap")
	private Double serviceCap;

	@Column(name = "is_groupwise_exlusion")
	private Integer isGroupwiseExlusion;

	@Column(name = "dept_id")
	private Integer deptId;

	@Column(name = "sub_dept_id")
	private Integer subDeptId;

	@Column(name = "product_category_id")
	private Integer productCategoryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", insertable = false, updatable = false, nullable = false)
	private ServiceMaster serviceMaster;

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
	}

	public int getPacakgeConsumptionServiceDetailId() {
		return pacakgeConsumptionServiceDetailId;
	}

	public void setPacakgeConsumptionServiceDetailId(int pacakgeConsumptionServiceDetailId) {
		this.pacakgeConsumptionServiceDetailId = pacakgeConsumptionServiceDetailId;
	}

	public Integer getPackageConsumptionMasterId() {
		return packageConsumptionMasterId;
	}

	public void setPackageConsumptionMasterId(Integer packageConsumptionMasterId) {
		this.packageConsumptionMasterId = packageConsumptionMasterId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getPackageQuantity() {
		return packageQuantity;
	}

	public void setPackageQuantity(Integer packageQuantity) {
		this.packageQuantity = packageQuantity;
	}

	public Integer getBalancePackageQuantity() {
		return balancePackageQuantity;
	}

	public void setBalancePackageQuantity(Integer balancePackageQuantity) {
		this.balancePackageQuantity = balancePackageQuantity;
	}

	public Double getApportionedPrice() {
		return apportionedPrice;
	}

	public void setApportionedPrice(Double apportionedPrice) {
		this.apportionedPrice = apportionedPrice;
	}

	public Integer getIsServiceItem() {
		return isServiceItem;
	}

	public void setIsServiceItem(Integer isServiceItem) {
		this.isServiceItem = isServiceItem;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getPackageEoDetailId() {
		return packageEoDetailId;
	}

	public void setPackageEoDetailId(Integer packageEoDetailId) {
		this.packageEoDetailId = packageEoDetailId;
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

	public ServiceMaster getServiceMaster() {
		return serviceMaster;
	}

	public void setServiceMaster(ServiceMaster serviceMaster) {
		this.serviceMaster = serviceMaster;
	}

	public int getIsInclusiveExclusive() {
		return isInclusiveExclusive;
	}

	public void setIsInclusiveExclusive(int isInclusiveExclusive) {
		this.isInclusiveExclusive = isInclusiveExclusive;
	}

	public int getIsCap() {
		return isCap;
	}

	public void setIsCap(int isCap) {
		this.isCap = isCap;
	}

	public Double getAvailableCap() {
		return availableCap;
	}

	public void setAvailableCap(Double availableCap) {
		this.availableCap = availableCap;
	}

	public Double getServiceCap() {
		return serviceCap;
	}

	public void setServiceCap(Double serviceCap) {
		this.serviceCap = serviceCap;
	}

	public Integer getIsGroupwiseExlusion() {
		return isGroupwiseExlusion;
	}

	public void setIsGroupwiseExlusion(Integer isGroupwiseExlusion) {
		this.isGroupwiseExlusion = isGroupwiseExlusion;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getSubDeptId() {
		return subDeptId;
	}

	public void setSubDeptId(Integer subDeptId) {
		this.subDeptId = subDeptId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

}
